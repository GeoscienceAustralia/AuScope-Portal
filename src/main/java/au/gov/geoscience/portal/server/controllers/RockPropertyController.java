package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.services.RockPropertyService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
                                             @RequestParam(value = "STYLES", defaultValue = "") String styles,
                                             @RequestParam(value = "postMethod", defaultValue = "false") Boolean postMethod,
                                             @RequestParam("version") String version,
                                             @RequestParam(value = "feature_count", defaultValue = "0") String feature_count) throws PortalServiceException, IOException {



        String[] bboxParts = bbox.split(",");
        double lng1 = Double.parseDouble(bboxParts[0]);
        double lng2 = Double.parseDouble(bboxParts[2]);
        double lat1 = Double.parseDouble(bboxParts[1]);
        double lat2 = Double.parseDouble(bboxParts[3]);

        List<NameValuePair> vendorParams = new ArrayList<>();
        vendorParams.add(new BasicNameValuePair("buffer","75"));

        String responseString = wmsService.getFeatureInfo(serviceUrl, infoFormat, queryLayers, "EPSG:3857",
                Math.min(lng1, lng2), Math.min(lat1, lat2), Math.max(lng1, lng2), Math.max(lat1, lat2),
                Integer.parseInt(width), Integer.parseInt(height), Double.parseDouble(longitude),
                Double.parseDouble(latitude),
                (int) (Double.parseDouble(x)), (int) (Double.parseDouble(y)), null, sldBody, postMethod, version,
                feature_count, true, vendorParams);

        InputStream responseStream = new ByteArrayInputStream(responseString.getBytes());
        FileIOUtil.writeInputToOutputStream(responseStream, response.getOutputStream(), BUFFERSIZE, true);
    }


    @RequestMapping("/getRockPropertyCount.do")
    public ModelAndView getRockPropertyCount(@RequestParam("serviceUrl") String serviceUrl,
                                             @RequestParam(required = false, value = "typeName") String typeName,
                                             @RequestParam(required = false, value = "bbox") String bboxJson,
                                             @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        WFSCountResponse response = null;

        try {
            response = this.rockPropertiesService.getRockPropertyCount(serviceUrl, typeName, maxFeatures, bbox);
        } catch (Exception e) {
            return this.generateExceptionResponse(e, serviceUrl);
        }

        return generateJSONResponseMAV(true, response.getNumberOfFeatures(), "");
    }

    @RequestMapping("/getRockPropertiesDownload.do")
    public void getRockPropertiesDownload(@RequestParam("serviceUrl") String serviceUrl,
                                       @RequestParam(required = false, value = "typeName") String typeName,
                                       @RequestParam(required = false, value = "bbox") String bboxJson,
                                       @RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
            throws Exception {
        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);

        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        String contentType;
        if (outputFormat != null && outputFormat.equals("csv")) {
            contentType = "text/csv";
        } else {
            contentType = "text/xml";
        }

        response.setContentType(contentType);
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.rockPropertiesService.downloadRockProperties(serviceUrl, typeName, bbox, outputFormat);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();
    }

}