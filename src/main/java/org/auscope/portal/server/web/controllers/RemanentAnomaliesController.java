package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.service.RemanentAnomaliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RemanentAnomaliesController extends BasePortalController {

    private RemanentAnomaliesService remanentAnomaliesService;

    public static final String REMANENT_ANOMALIES_TYPE = "RemAnom:Anomaly";

    @Autowired
    public RemanentAnomaliesController(RemanentAnomaliesService remanentAnomaliesService) {
        this.remanentAnomaliesService = remanentAnomaliesService;
    }

    @RequestMapping("/doRemanentAnomaliesDownload.do")
    public void doRemanentAnomaliesDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            HttpServletResponse response) throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        String filter = this.remanentAnomaliesService.getRemanentAnomaliesFilter(name, bbox);

        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.remanentAnomaliesService.downloadWFS(serviceUrl, REMANENT_ANOMALIES_TYPE, filter,
                null);
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
    @RequestMapping("/getRemanentAnomaliesStyle.do")
    public void doRemanentAnomaliesStyle(
            @RequestParam(required = false, value = "serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            HttpServletResponse response) throws Exception {

        // Vt: wms shouldn't need the bbox because it is tiled.
        FilterBoundingBox bbox = null;
        // String stylefilter=this.remanentAnomaliesService.getRemanentAnomaliesWithStyling(name); //VT:get filter from service

        String filter = this.remanentAnomaliesService.getRemanentAnomaliesFilter(name, bbox); // VT:get filter from service

        String style = this.getStyle(filter, "#0000FF");

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
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
                + REMANENT_ANOMALIES_TYPE
                + "</Name>"
                + "<UserStyle>"
                + "<Name>portal-style</Name>"
                + "<Title>portal-style</Title>"
                + "<Abstract>portal-style</Abstract>"
                + "<IsDefault>1</IsDefault>"
                + "<FeatureTypeStyle>"
                + "<Rule>"
                + "<Name>Anomaly</Name>"
                + filter
                + "<PointSymbolizer>"
                + "<Graphic>"
                + "<Mark>"
                + "<WellKnownName>square</WellKnownName>"
                + "<Fill>"
                + "<CssParameter name=\"fill\">"
                + color
                + "</CssParameter>"
                + "</Fill>"
                + "</Mark>"
                + "<Size>8</Size>"
                + "</Graphic>"
                + "</PointSymbolizer>"
                + "</Rule>"
                + "</FeatureTypeStyle>"
                + "</UserStyle>" + "</NamedLayer>" + "</StyledLayerDescriptor>";

        return style;
    }

}
