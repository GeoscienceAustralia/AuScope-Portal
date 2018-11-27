package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.MineralTenementServiceProviderType;
import org.auscope.portal.server.web.service.MineralTenementService;
import org.auscope.portal.xslt.ArcGISToMineralTenement;
import org.auscope.portal.xslt.WfsToCsvTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

@Controller
public class MineralTenementController extends BasePortalController {

    private MineralTenementService mineralTenementService;
    private WMSService mineralTenementWMSService;
    
    private ArcGISToMineralTenement arcGISToMineralTenementTransformer;
    private WfsToCsvTransformer csvTransformer;

    public static final String MINERAL_TENEMENT_TYPE = "mt:MineralTenement";
    public static final String ARCGIS_MINERAL_TENEMENT_TYPE = "MineralTenement";

    private static final String ENCODING = "ISO-8859-1";
	private static final int BUFFERSIZE = 1024 * 1024;
    
    @Autowired
    public MineralTenementController(MineralTenementService mineralTenementService, WMSService wmsService, ArcGISToMineralTenement arcGISToMineralTenement, WfsToCsvTransformer wfsToCsvTransformer) {
        this.mineralTenementService = mineralTenementService;
        this.mineralTenementWMSService = wmsService;
        this.arcGISToMineralTenementTransformer = arcGISToMineralTenement;
        this.csvTransformer = wfsToCsvTransformer;
    }
    
    
    @RequestMapping("/getAllMineralTenementFeatures.do")
    public ModelAndView getAllMineralTenementFeatures(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "tenementName") String tenementName,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
                    throws Exception {

        // The presence of a bounding box causes us to assume we will be using this GML for visualizing on a map
        // This will in turn limit the number of points returned to 200

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        WFSResponse response = null;
        try {
            response = this.mineralTenementService.getAllTenements(serviceUrl, tenementName, owner,
                    maxFeatures, bbox, null);

            
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }
        
        log.warn("GML: " + response.getData());
        
        
        return generateJSONResponseMAV(true, "gml", response.getData(), response.getMethod());
    }
    
    @RequestMapping("/getMineralTenementFeatureInfo.do")
	public void getMineralTenementFeatureInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("serviceUrl") String serviceUrl, @RequestParam("lat") String latitude,
			@RequestParam("lng") String longitude, @RequestParam("QUERY_LAYERS") String queryLayers,
			@RequestParam("x") String x, @RequestParam("y") String y, @RequestParam("BBOX") String bbox,
			@RequestParam("WIDTH") String width, @RequestParam("HEIGHT") String height,
			@RequestParam("INFO_FORMAT") String infoFormat, @RequestParam("SLD_BODY") String sldBody,
			@RequestParam(value = "postMethod", defaultValue = "false") Boolean postMethod,
			@RequestParam("version") String version,
			@RequestParam(value = "feature_count", defaultValue = "0") String feature_count) throws Exception {

		String[] bboxParts = bbox.split(",");
		double lng1 = Double.parseDouble(bboxParts[0]);
		double lng2 = Double.parseDouble(bboxParts[2]);
		double lat1 = Double.parseDouble(bboxParts[1]);
		double lat2 = Double.parseDouble(bboxParts[3]);
		
		String featureInfoString = this.mineralTenementWMSService.getFeatureInfo(serviceUrl, infoFormat, queryLayers,
				"EPSG:3857", Math.min(lng1, lng2), Math.min(lat1, lat2), Math.max(lng1, lng2), Math.max(lat1, lat2),
				Integer.parseInt(width), Integer.parseInt(height), Double.parseDouble(longitude),
				Double.parseDouble(latitude), (int) (Double.parseDouble(x)), (int) (Double.parseDouble(y)), "", sldBody,
				postMethod, version, feature_count, true);

		Document xmlDocument = getDocumentFromString(featureInfoString);
		
		String responseString = "";
		if (xmlDocument.getDocumentElement().getLocalName().equals("FeatureInfoResponse")) {
			responseString = this.arcGISToMineralTenementTransformer.convert(featureInfoString, serviceUrl);
		} else {
			responseString = featureInfoString;
		};

		InputStream responseStream = new ByteArrayInputStream(responseString.getBytes());
		FileIOUtil.writeInputToOutputStream(responseStream, response.getOutputStream(), BUFFERSIZE, true);
	}
    
    @RequestMapping("/getMineralTenementCount.do")
    public ModelAndView getMineralTenementCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "tenementName") String tenementName,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
                    throws Exception {

        // The presence of a bounding box causes us to assume we will be using this GML for visualizing on a map
        // This will in turn limit the number of points returned to 200
        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        WFSCountResponse response = null;
        try {
            response = this.mineralTenementService.getTenementCount(serviceUrl, tenementName, owner,
                    maxFeatures, bbox);

            
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }
      
        
        return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
    }
    

    @RequestMapping("/doMineralTenementDownload.do")
    public ModelAndView doMineralTenementDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures") Integer maxFeatures,
            @RequestParam(required = false, value = "outputFormat") String outputFormat) throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
    	
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        
        if (ogcServiceProviderType == OgcServiceProviderType.ArcGis) {
        	outputFormat = "text/xml; subtype=gml/3.1.1";
        }
        
        WFSResponse wfsResponse = this.mineralTenementService.getAllTenements(serviceUrl, name, owner, maxFeatures, bbox, outputFormat);
        String response ;
        
        if (ogcServiceProviderType == OgcServiceProviderType.ArcGis) {
        	response = this.csvTransformer.convert(wfsResponse.getData(),serviceUrl);
        } else {
        	response = wfsResponse.getData();
        }
        
        return generateNamedJSONResponseMAV(true, "gml", response, wfsResponse.getMethod());

    }

    /**
     * Handles getting the style of the mineral tenement filter queries. (If the bbox elements are specified, they will limit the output response to 200 records
     * implicitly)
     *
     * @param serviceUrl
     * @param name
     * @param tenementTypeUri
     * @param owner
     * @param statusUri
     * @throws Exception
     */
    @RequestMapping("/getMineralTenementStyle.do")
    public void getMineralTenementStyle(
            @RequestParam(required = false, value = "serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "tenementTypeUri") String tenementTypeUri,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "tenementStatusUri") String tenementStatusUri,
            @RequestParam(required = false, value = "endDate") String endDate,
            HttpServletResponse response) throws Exception {

        // Vt: wms shouldn't need the bbox because it is tiled.
        FilterBoundingBox bbox = null;
        
        MineralTenementServiceProviderType mineralTenementServiceProviderType = MineralTenementServiceProviderType.parseUrl(serviceUrl);
        
        String filter = this.mineralTenementService.getMineralTenementFilter(name, tenementTypeUri, owner, tenementStatusUri, endDate,
                bbox, mineralTenementServiceProviderType);

        String style = this.getPolygonStyle(filter, mineralTenementServiceProviderType.featureType() , mineralTenementServiceProviderType.fillColour(), mineralTenementServiceProviderType.borderColour());

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    public String getPolygonStyle(String filter, String name, String color, String borderColor) {

        String style = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<StyledLayerDescriptor version=\"1.0.0\" " +
                "xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" " +
                "xmlns=\"http://www.opengis.net/sld\" " +
                "xmlns:mt=\"http://xmlns.geoscience.gov.au/mineraltenementml/1.0\" " +
                "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
                "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                "xmlns:ows=\"http://www.opengis.net/ows\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
                "<NamedLayer>" +
                "<Name>" + name + "</Name>" +
                "<UserStyle>" +
                "<Title>Default style</Title>" +
                "<Abstract>A green default style</Abstract>" +
                "<Name>mineralTenementStyle</Name>" +
                "<FeatureTypeStyle>" +
                "<Rule>" +
                "<Name>Polygon for mineral tenement</Name>" +
                "<Title>Mineral Tenement</Title>" +
                "<Abstract>green fill with a lighter green outline 1 pixel in width</Abstract>" +
                filter +
                "<PolygonSymbolizer>" +
                "<Fill>" +
                "<CssParameter name=\"fill\">" + color + "</CssParameter>" +
                "<CssParameter name=\"fill-opacity\">1</CssParameter>" +
                "</Fill>" +
                "<Stroke>" +
                "<CssParameter name=\"stroke\">" + borderColor + "</CssParameter>" +
                "<CssParameter name=\"stroke-width\">1</CssParameter>" +
                "</Stroke>" +
                "</PolygonSymbolizer>" +
                "</Rule>" +
                "</FeatureTypeStyle>" +
                "</UserStyle>" +
                "</NamedLayer>" +
                "</StyledLayerDescriptor>";
        return style;
    }

    private Document getDocumentFromString(String responseString)
			throws Exception {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(responseString.getBytes(ENCODING)));
	}
}
