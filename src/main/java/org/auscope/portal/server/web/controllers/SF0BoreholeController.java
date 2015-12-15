package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.service.SF0BoreholeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling requests for the SF0 Borehole
 *
 * @author Florence Tan
 *
 */
@Controller
public class SF0BoreholeController extends BasePortalController {

    private SF0BoreholeService boreholeService;

    private CSWCacheService cswService;

    @Autowired
    public SF0BoreholeController(SF0BoreholeService sf0BoreholeService, CSWCacheService cswService) {
        this.boreholeService = sf0BoreholeService;
        this.cswService = cswService;
    }

    /**
     * Handles the borehole filter queries.
     *
     * @param serviceUrl
     *            the url of the service to query
     * @param mineName
     *            the name of the mine to query for
     * @param request
     *            the HTTP client request
     * @return a WFS response converted into KML
     * @throws Exception
     */
    @RequestMapping("/doBoreholeViewFilter.do")
    public ModelAndView doBoreholeFilter(String serviceUrl, String boreholeName, String custodian,
            String dateOfDrilling, int maxFeatures, String bbox) throws Exception {

        try {
            OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
            FilterBoundingBox box = FilterBoundingBox.attemptParseFromJSON(bbox, ogcServiceProviderType);
            WFSTransformedResponse response = this.boreholeService.getAllBoreholes(serviceUrl, boreholeName, custodian,
                    dateOfDrilling, maxFeatures, box);
            return generateJSONResponseMAV(true, response.getGml(), response.getTransformed(), response.getMethod());
        } catch (Exception e) {
            return this.generateExceptionResponse(e, serviceUrl);
        }
    }

    /**
     * Handles getting the style of the SF0 borehole filter queries. (If the bbox elements are specified, they will limit the output response to 200 records
     * implicitly)
     *
     * @param mineName
     *            the name of the mine to query for
     * @param bbox
     * @param maxFeatures
     * @throws Exception
     */
    @RequestMapping("/doBoreholeViewFilterStyle.do")
    public void doFilterStyle(
            HttpServletResponse response,
            @RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "serviceFilter", defaultValue = "") String serviceFilter,
            @RequestParam(required = false, value = "color", defaultValue = "") String color)
                    throws Exception {

        FilterBoundingBox bbox = null;
        // FilterBoundingBox
        // .attemptParseFromJSON(bboxJson);

        List<String> hyloggerBoreholeIDs = null;
        // AUS-2445
        // RA: we can't show WMS for NVCL for now because the way GeoServer filter WMS isn't very efficient and
        // it will cause services with a lot of scanned boreholes (e.g. SA) to run out of memory!
        // try {
        // // don't get hylogger IDs if this is only to populate the legend
        // if (!serviceUrl.isEmpty()) {
        // hyloggerBoreholeIDs = this.boreholeService
        // .discoverHyloggerBoreholeIDs(this.cswService,
        // new CSWRecordsHostFilter(serviceUrl));
        // }
        // } catch (Exception e) {
        // log.warn(String
        // .format("Error requesting list of hylogger borehole ID's from %1$s: %2$s",
        // serviceUrl, e));
        // log.debug("Exception:", e);
        // }

        String filter = this.boreholeService.getFilter(boreholeName,
                custodian, dateOfDrilling, maxFeatures, bbox,
                null);

        String hyloggerFilter = this.boreholeService.getFilter(boreholeName,
                custodian, dateOfDrilling, maxFeatures, bbox,
                hyloggerBoreholeIDs);

        String style = this.boreholeService.getStyle(filter, (color.isEmpty() ? "#2242c7" : color), hyloggerFilter,
                "#F87217");

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024,
                false);

        styleStream.close();
        outputStream.close();
    }

}
