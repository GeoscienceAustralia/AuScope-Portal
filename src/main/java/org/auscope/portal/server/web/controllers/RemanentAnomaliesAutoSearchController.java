package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.service.RemanentAnomaliesAutoSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RemanentAnomaliesAutoSearchController extends BasePortalController {
    private RemanentAnomaliesAutoSearchService remanentAnomaliesAutoSearchService;

    public static final String REMANENT_ANOMALIESAUTOSEARCH_TYPE = "RemAnomAutoSearch:AutoSearchAnomalies";

    @Autowired
    public RemanentAnomaliesAutoSearchController(
            RemanentAnomaliesAutoSearchService remanentAnomaliesAutoSearchService) {
        this.remanentAnomaliesAutoSearchService = remanentAnomaliesAutoSearchService;
    }

    @RequestMapping("/doRemanentAnomaliesAutoSearchDownload.do")
    public void doRemanentAnomaliesAutoSearchDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            HttpServletResponse response) throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        String filter = this.remanentAnomaliesAutoSearchService.getRemanentAnomaliesAutoSearchFilter(bbox);

        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.remanentAnomaliesAutoSearchService.downloadWFS(serviceUrl,
                REMANENT_ANOMALIESAUTOSEARCH_TYPE, filter, null);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();

    }

    /**
     * Handles getting the style of the Remanent Anomalies filter queries. (If the bbox elements are specified, they will limit the output response to 200
     * records implicitly)
     *
     * @param serviceUrl
     * @param name
     * @throws Exception
     */
    @RequestMapping("/getRemanentAnomaliesAutoSearchStyle.do")
    public void doRemanentAnomaliesAutoSearchStyle(
            @RequestParam(required = false, value = "serviceUrl") String serviceUrl,
            HttpServletResponse response) throws Exception {

        // Vt: wms shouldn't need the bbox because it is tiled.
        FilterBoundingBox bbox = null;
        // String stylefilter=this.remanentAnomaliesService.getRemanentAnomaliesWithStyling(name); //VT:get filter from service

        String filter = this.remanentAnomaliesAutoSearchService.getRemanentAnomaliesAutoSearchFilter(bbox); // VT:get filter from service

        String style = this.getStyle(filter, "#0000FF");

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    public String getStyle(String filter, String color) {

        String style = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<StyledLayerDescriptor version=\"1.0.0\" "
                + "xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd http://remanentanomalies.csiro.au/schemas/anomaly.xsd\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns=\"http://www.opengis.net/sld\" xmlns:RemAnom=\"http://remanentanomalies.csiro.au\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<NamedLayer>" + "<Name>"
                + REMANENT_ANOMALIESAUTOSEARCH_TYPE
                + "</Name>"
                + "<UserStyle>"
                + "<Name>portal-style</Name>"
                + "<Title>portal-style</Title>"
                + "<Abstract>portal-style</Abstract>"
                + "<IsDefault>1</IsDefault>"
                + "<FeatureTypeStyle>"
                + "<Rule>"
                + "<Name>Rotation 0-30</Name>"
                + "<Title>Rotation between 0 and 30 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 0 and 30 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>0</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>30</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#0000FF</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#0000FF</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "<Rule>"
                + "<Name>Rotation 30-60</Name>"
                + "<Title>Rotation between 30 and 60 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 30 and 60 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>30</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>60</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#00FFFF</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#00FFFF</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "<Rule>"
                + "<Name>Rotation 60-90</Name>"
                + "<Title>Rotation between 60 and 90 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 60 and 90 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>60</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>90</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#00FF00</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#00FF00</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "<Rule>"
                + "<Name>Rotation 90-120</Name>"
                + "<Title>Rotation between 90 and 120 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 90 and 120 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>90</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>120</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#FFFF00</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#FFFF00</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "<Rule>"
                + "<Name>Rotation 120-150</Name>"
                + "<Title>Rotation between 120 and 150 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 120 and 150 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>120</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>150</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#FFA500</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#FFA500</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "<Rule>"
                + "<Name>Rotation 150-180</Name>"
                + "<Title>Rotation between 150 and 180 degrees</Title>"
                + "<Abstract>Rotation of the magnetisation direction away from IGRF between 150 and 180 degrees</Abstract>"
                + "<ogc:Filter>"
                + "<ogc:And>"
                + "<ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>150</ogc:Literal>"
                + "</ogc:PropertyIsGreaterThanOrEqualTo>"
                + "<ogc:PropertyIsLessThan>"
                + "<ogc:PropertyName>rotation_from_igrf</ogc:PropertyName>"
                + "<ogc:Literal>180</ogc:Literal>"
                + "</ogc:PropertyIsLessThan>"
                + "</ogc:And>"
                + "</ogc:Filter>"
                + "<PolygonSymbolizer>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">#FF0000</CssParameter>"
                + "<CssParameter name=\"fill-opacity\">0.5</CssParameter>"
                + "</Fill>"
                + "<Stroke>"
                + "<CssParameter name=\"stroke\">#FF0000</CssParameter>"
                + "<CssParameter name=\"stroke-width\">1</CssParameter>"
                + "</Stroke>"
                + "</PolygonSymbolizer>"
                + "</Rule>"
                + "</FeatureTypeStyle>"
                + "</UserStyle>" + "</NamedLayer>" + "</StyledLayerDescriptor>";

        return style;
    }
}
