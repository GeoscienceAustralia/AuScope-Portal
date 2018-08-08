package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.service.RockPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RockPropertyController extends BasePortalController {

	private static String SCALAR_RESULTS = "ga_rock_properties_wfs:scalar_results";
	private static String REMANENT_MAGNETISATION = "ga_rock_properties_wfs:remanent_magnetisation";

	private RockPropertyService rockPropertiesService;
	private WMSService wmsService;

	protected static int BUFFERSIZE = 1024 * 1024;

	@Autowired
	public RockPropertyController(RockPropertyService rockPropertiesService, WMSService wmsService) {
		this.rockPropertiesService = rockPropertiesService;
		this.wmsService = wmsService;
	}


	@RequestMapping(value = "/rockPropertiesGetFeatureInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public void rockPropertiesGetFeatureInfo(HttpServletRequest request,
													 HttpServletResponse response,
													 @RequestParam("serviceUrl") String serviceUrl,
													 @RequestParam("lat") String latitude,
													 @RequestParam("lng") String longitude,
													 @RequestParam("QUERY_LAYERS") String queryLayers,
													 @RequestParam("x") String x,
													 @RequestParam("y") String y,
													 @RequestParam("BBOX") String bbox,
													 @RequestParam("WIDTH") String width,
													 @RequestParam("HEIGHT") String height,
													 @RequestParam("INFO_FORMAT") String infoFormat,
													 @RequestParam(value = "SLD_BODY", defaultValue = "") String sldBody,
													 @RequestParam(value = "postMethod", defaultValue = "false") Boolean postMethod,
													 @RequestParam("version") String version,
													 @RequestParam(value = "feature_count", defaultValue = "0") String feature_count) throws PortalServiceException, IOException {


		String[] bboxParts = bbox.split(",");
		double lng1 = Double.parseDouble(bboxParts[0]);
		double lng2 = Double.parseDouble(bboxParts[2]);
		double lat1 = Double.parseDouble(bboxParts[1]);
		double lat2 = Double.parseDouble(bboxParts[3]);

		String styles = "point";

		String responseString = wmsService.getFeatureInfo(serviceUrl, infoFormat, queryLayers, "EPSG:3857",
				Math.min(lng1, lng2), Math.min(lat1, lat2), Math.max(lng1, lng2), Math.max(lat1, lat2),
				Integer.parseInt(width), Integer.parseInt(height), Double.parseDouble(longitude),
				Double.parseDouble(latitude),
				(int) (Double.parseDouble(x)), (int) (Double.parseDouble(y)), styles, sldBody, postMethod, version,
				feature_count, true);
		//VT: Ugly hack for the GA wms layer in registered tab as its font is way too small at 80.
		//VT : GA style sheet also mess up the portal styling of tables as well.
		if (responseString.contains("table, th, td {")) {
			responseString = responseString.replaceFirst("table, th, td \\{",
					".ausga table, .ausga th, .ausga td {");
			responseString = responseString.replaceFirst("th, td \\{", ".ausga th, .ausga td {");
			responseString = responseString.replaceFirst("th \\{", ".ausga th {");
			responseString = responseString.replace("<table", "<table class='ausga'");
		}

		InputStream responseStream = new ByteArrayInputStream(responseString.getBytes());
		FileIOUtil.writeInputToOutputStream(responseStream, response.getOutputStream(), BUFFERSIZE, true);
	}

	@RequestMapping("/getRockPropertyFeatures.do")
	public ModelAndView getRockPropertyFeatures(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "rockProperty") String rockProperty,
			@RequestParam(required = false, value = "bbox") String bboxJson,

			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
		WFSTransformedResponse response = null;

		try {
			response = this.rockPropertiesService.getRockProperties(serviceUrl, rockProperty, maxFeatures, bbox);
		} catch (Exception e) {
			return this.generateExceptionResponse(e, serviceUrl);
		}

		return generateJSONResponseMAV(response.getSuccess(), response.getGml(), response.getTransformed(),
				response.getMethod());
	}
	
	@RequestMapping("/getRockPropertyCount.do")
	public ModelAndView getRockPropertyCount(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "rockProperty") String rockProperty,
			@RequestParam(required = false, value = "bbox") String bboxJson,

			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
		WFSCountResponse response = null;

		try {
			response = this.rockPropertiesService.getRockPropertyCount(serviceUrl, rockProperty, maxFeatures, bbox);
		} catch (Exception e) {
			return this.generateExceptionResponse(e, serviceUrl);
		}

		return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
	}

	@RequestMapping("/getBulkDensityDownload.do")
	public void getBulkDensityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "bulk density";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getChargeabilityDownload.do")
	public void getChargeabilityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "chargeability";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getElectricalConductivityDownload.do")
	public void getElectricalConductivityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "electrical conductivity";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getElectricalResistivityDownload.do")
	public void getElectricalResistivityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "electrical resistivity";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getGammaBulkDensityDownload.do")
	public void getGammaBulkDensityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "gamma bulk density";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getKoenigsbergerRatioDownload.do")
	public void getKoenigsbergerRatioDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "koenigsberger ratio";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getMagneticSusceptibilityDownload.do")
	public void getMagneticSusceptibilityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "magnetic susceptibility";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getNaturalGammaDownload.do")
	public void getNaturalGammaDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "natural gamma";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getPorosityDownload.do")
	public void getPorosityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "porosity";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getRemanentMagnetisationDownload.do")
	public void getRemanentMagnetisationDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "remanent magnetisation";
		getRockPropertyDownload(serviceUrl, REMANENT_MAGNETISATION, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getSonicVelocityDownload.do")
	public void getSonicVelocityDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "sonic velocity";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	@RequestMapping("/getTemperatureDownload.do")
	public void getTemperatureDownload(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
					throws Exception {
		String rockProperty = "temperature";
		getRockPropertyDownload(serviceUrl, SCALAR_RESULTS, rockProperty, bboxJson, outputFormat, response);
	}

	public void getRockPropertyDownload(String serviceUrl, String featureType, String rockProperty, String bboxJson,
			String outputFormat, HttpServletResponse response) throws Exception {
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
		String filter = this.rockPropertiesService.getRockPropertyFilter(rockProperty, bbox);
				System.out.println("Filter String: "+ filter);
		String contentType;
		if (outputFormat.equals("csv")) {
			contentType = "text/csv";
		} else {
			contentType = "text/xml";
		}
		
		response.setContentType(contentType);
		OutputStream outputStream = response.getOutputStream();

		InputStream results = this.rockPropertiesService.downloadRockProperties(serviceUrl, featureType, filter, outputFormat);
		FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
		outputStream.close();
	}

}
