package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.service.MineralTenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MineralTenementController extends BasePortalController {

    private MineralTenementService mineralTenementService;

    public static final String MINERAL_TENEMENT_TYPE = "mt:MineralTenement";

    @Autowired
    public MineralTenementController(MineralTenementService mineralTenementService) {
        this.mineralTenementService = mineralTenementService;
    }

    @RequestMapping("/doMineralTenementDownload.do")
    public void doMineralTenementDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("name") String name,
            @RequestParam(required = false, value = "tenementType") String tenementType,
            @RequestParam("owner") String owner,
            @RequestParam(required = false, value = "size") String size,
            @RequestParam(required = false, value = "endDate") String endDate,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            HttpServletResponse response) throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        String filter = this.mineralTenementService.getMineralTenementFilter(name, tenementType, owner, size, endDate,
                bbox); // VT:get filter from service

        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.mineralTenementService.downloadWFS(serviceUrl, MINERAL_TENEMENT_TYPE, filter, null);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();

    }

    /**
     * Handles getting the style of the mineral tenement filter queries. (If the bbox elements are specified, they will limit the output response to 200 records
     * implicitly)
     *
     * @param serviceUrl
     * @param name
     * @param tenementType
     * @param owner
     * @param status
     * @throws Exception
     */
    @RequestMapping("/getMineralTenementStyle.do")
    public void doMineFilterStyle(
            @RequestParam(required = false, value = "serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "tenementType") String tenementType,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "size") String size,
            @RequestParam(required = false, value = "endDate") String endDate,
            HttpServletResponse response) throws Exception {

        // Vt: wms shouldn't need the bbox because it is tiled.
        FilterBoundingBox bbox = null;
        String stylefilter = this.mineralTenementService.getMineralTenementWithStyling(name, tenementType, owner, size,
                endDate); // VT:get filter from service

        String filter = this.mineralTenementService.getMineralTenementFilter(name, tenementType, owner, size, endDate,
                bbox); // VT:get filter from service

        String style = this.getPolygonStyle(stylefilter, filter, MINERAL_TENEMENT_TYPE, "#00FF00", "#00FF00");

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    public String getPolygonStyle(String stylefilter, String filter, String name, String color, String borderColor) {

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
                "<FeatureTypeStyle>" +
                "<Rule>" +
                "<Name>Polygon for mineral tenement</Name>" +
                "<Title>Mineral Tenement</Title>" +
                "<Abstract>50% transparent green fill with a red outline 1 pixel in width</Abstract>" +
                filter +
                "<PolygonSymbolizer>" +
                "<Fill>" +
                "<CssParameter name=\"fill\">" + color + "</CssParameter>" +
                "<CssParameter name=\"fill-opacity\">0.6</CssParameter>" +
                "</Fill>" +
                "<Stroke>" +
                "<CssParameter name=\"stroke\">" + borderColor + "</CssParameter>" +
                "<CssParameter name=\"stroke-width\">1</CssParameter>" +
                "</Stroke>" +
                "</PolygonSymbolizer>" +
                "</Rule>" +
                // "<Rule>" +
                // "<Name>Polygon for mineral tenement</Name>" +
                // "<Title>Active Tenement</Title>" +
                // "<Abstract>50% transparent green fill with a red outline 1 pixel in width</Abstract>" +
                // stylefilter +
                // "<PolygonSymbolizer>" +
                // "<Fill>" +
                // "<CssParameter name=\"fill\">" + color + "</CssParameter>" +
                // "<CssParameter name=\"fill-opacity\">0.6</CssParameter>" +
                // "</Fill>" +
                // "<Stroke>" +
                // "<CssParameter name=\"stroke\">" + borderColor + "</CssParameter>" +
                // "<CssParameter name=\"stroke-width\">1</CssParameter>" +
                // "</Stroke>" +
                // "</PolygonSymbolizer>" +
                // "</Rule>" +
                "</FeatureTypeStyle>" +
                "</UserStyle>" +
                "</NamedLayer>" +
                "</StyledLayerDescriptor>";
        return style;
    }

}
