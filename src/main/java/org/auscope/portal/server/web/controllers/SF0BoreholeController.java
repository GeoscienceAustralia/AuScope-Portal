package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import au.gov.geoscience.portal.server.controllers.FilterStyle;
import au.gov.geoscience.portal.services.filters.BoreholeViewFilter;
import org.apache.commons.lang3.StringUtils;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.domain.nvcldataservice.AnalyticalJobResults;
import org.auscope.portal.server.web.service.BoreholeService;
import org.auscope.portal.server.web.service.BoreholeService.Mark;
import org.auscope.portal.server.web.service.NVCL2_0_DataService;
import org.auscope.portal.server.web.service.SF0BoreholeService;
import org.opengis.filter.Filter;
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
	private GsmlpNameSpaceTable gsmlpNameSpaceTable;
    private NVCL2_0_DataService nvclDataService;


    @Autowired
	public SF0BoreholeController(SF0BoreholeService sf0BoreholeService, CSWCacheService cswService,  NVCL2_0_DataService nvclDataService) {
		this.boreholeService = sf0BoreholeService;
		this.cswService = cswService;
        this.nvclDataService = nvclDataService;
		GsmlpNameSpaceTable _gsmlpNameSpaceTable = new GsmlpNameSpaceTable();
		this.gsmlpNameSpaceTable = _gsmlpNameSpaceTable;

	}

    /**
     * @param serviceUrl
     * @param mineName
     * @param status
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/nationalVirtualCoreLibraryFilterCount.do")
    public ModelAndView mineFilterCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "nvclCollection", defaultValue = "") Boolean nvclCollection,
            @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {


        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson);

        try {
            WFSCountResponse response = this.boreholeService.getNVCLCount(serviceUrl, boreholeName, dateOfDrilling, nvclCollection, bbox, maxFeatures);
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
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
    @RequestMapping("/nationalVirtualCoreLibraryFilterStyle.do")
    public void doNvclV2FilterStyle(
            HttpServletResponse response,
            @RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "serviceFilter", defaultValue = "") String serviceFilter,
            @RequestParam(required = false, value = "color", defaultValue = "") String color,
            @RequestParam(required = false, value = "analyticsJobId") String analyticsJobId,
            @RequestParam(required = false, value = "failIds") String failIds,
            @RequestParam(required = false, value = "errorIds") String errorIds)

            throws Exception {

        FilterBoundingBox bbox = null;
        List<String> hyloggerBoreholeIDs = null;

        List<String> filterNames = new ArrayList<String>();
        List<String> filterColors = new ArrayList<String>();
        List<String> filters = new ArrayList<String>();
        List<Mark> filterMarks = new ArrayList<Mark>();
        String gsmlpNameSpace = gsmlpNameSpaceTable.getGsmlpNameSpace(serviceUrl);
        if (StringUtils.isNotEmpty(analyticsJobId)) {
            //Generate a style for displaying pass/fail/error holes
            AnalyticalJobResults analyticsResults = nvclDataService.getProcessingResults(analyticsJobId);

            if (!analyticsResults.getErrorBoreholes().isEmpty()) {
                filterNames.add("Error Boreholes");
                filterColors.add("#ff0000");
                filters.add(this.boreholeService.getFilter(boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, null, analyticsResults.getErrorBoreholes(), true));
                filterMarks.add(Mark.X);
            }

            if (!analyticsResults.getFailBoreholes().isEmpty()) {
                filterNames.add("Fail Boreholes");
                filterColors.add("#8390C6");
                filters.add(this.boreholeService.getFilter(boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, null, analyticsResults.getFailBoreholes(), true));
                filterMarks.add(Mark.CIRCLE);
            }

            if (!analyticsResults.getPassBoreholes().isEmpty()) {
                filterNames.add("Pass Boreholes");
                filterColors.add(color.isEmpty() ? "#2242c7" : color);
                filters.add(this.boreholeService.getFilter(boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, null, analyticsResults.getPassBoreholes(), true));
                filterMarks.add(Mark.TRIANGLE);
            }
        } else {
            if (this.boreholeService.namespaceSupportsHyloggerFilter(gsmlpNameSpace)) {
                filters.add(this.boreholeService.getFilter(boreholeName,
                        custodian, dateOfDrilling, maxFeatures, bbox,
                        hyloggerBoreholeIDs, true));
                filterColors.add("#6578ff");
                filterNames.add("Hylogged");
                filterMarks.add(Mark.CIRCLE);
            }
        }

        response.setContentType("text/xml");

        String style = this.boreholeService.getStyle(filterNames, filters, filterColors, filterMarks, gsmlpNameSpace);
        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
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
    @RequestMapping("/doNVCLBoreholeViewCSVDownload.do")
    public ModelAndView doNVCLBoreholeViewCSVDownload(
            @RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "nvclCollection", defaultValue = "") Boolean nvclCollection,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") Integer maxFeatures,
            @RequestParam(required = false, value = "bbox") String bbox,
            @RequestParam(required = false, value = "outputFormat") String outputFormat) throws Exception {

        try {
            FilterBoundingBox box = FilterBoundingBox.attemptParseFromJSON(bbox);
            WFSResponse response = this.boreholeService.getAllBoreholes(serviceUrl, boreholeName, custodian,
                    dateOfDrilling, nvclCollection, maxFeatures, box, outputFormat);
            return generateNamedJSONResponseMAV(true, "gml", response.getData(), response.getMethod());
        } catch (Exception e) {
            return this.generateExceptionResponse(e, serviceUrl);
        }
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
	public ModelAndView doBoreholeFilter(
			@RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
			@RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
			@RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
			@RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") Integer maxFeatures,
			@RequestParam(required = false, value = "bbox") String bbox,
			@RequestParam(required = false, value = "outputFormat") String outputFormat) throws Exception {

		try {
			FilterBoundingBox box = FilterBoundingBox.attemptParseFromJSON(bbox);
			WFSResponse response = this.boreholeService.getAllBoreholes(serviceUrl, boreholeName, custodian,
					dateOfDrilling, maxFeatures, box, outputFormat);
			return generateNamedJSONResponseMAV(true, "gml", response.getData(), response.getMethod());
		} catch (Exception e) {
			return this.generateExceptionResponse(e, serviceUrl);
		}
	}

	/**
     * Handles getting the style of the SF0 borehole filter queries. (If the bbox elements are specified, they will limit the output response to 200 records
     * implicitly)
     *
     * @param maxFeatures
     * @throws Exception
     */
    @RequestMapping("/doBoreholeViewFilterStyle.do")
    public void doFilterStyle(
            HttpServletResponse response,
            @RequestParam(value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") Integer maxFeatures,
            @RequestParam(required = false, value = "bbox") String bboxJson)

            throws Exception {

        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson);

        BoreholeViewFilter filter = this.boreholeService.getFilter(boreholeName, dateOfDrilling, bbox);


        String gsmlpNameSpace = gsmlpNameSpaceTable.getGsmlpNameSpace(serviceUrl);

        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/boreholeview.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), null);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024,
                false);

        styleStream.close();
        outputStream.close();
    }

    /**
     * This controller method is for forcing the internal cache of GsmlpNameSpaceTable to invalidate and update.
     *
     * @return
     */

    @RequestMapping("/updateGsmlpNSCache.do")
    public ModelAndView updateGsmlpNSCache() throws Exception {
        try {
            if (gsmlpNameSpaceTable != null )
                gsmlpNameSpaceTable.clearCache();
            return generateJSONResponseMAV(true);
        } catch (Exception e) {
            log.warn(String.format("Error updating GsmlpNS cache: %1$s", e));
            log.debug("Exception:", e);
            return generateJSONResponseMAV(false);
        }
    }
}
