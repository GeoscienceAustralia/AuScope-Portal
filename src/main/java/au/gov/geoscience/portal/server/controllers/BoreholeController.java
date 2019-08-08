package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.nvcldataservice.AnalyticalJobResults;
import au.gov.geoscience.portal.server.services.BoreholeService;
import au.gov.geoscience.portal.server.services.BoreholeService.Mark;
import au.gov.geoscience.portal.server.services.BoreholeViewService;
import au.gov.geoscience.portal.server.services.NVCL2_0_DataService;
import org.apache.commons.lang3.StringUtils;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class BoreholeController extends BasePortalController {

	private BoreholeViewService boreholeService;

	private CSWCacheService cswService;
	private NamespaceService namespaceService;
    private NVCL2_0_DataService nvclDataService;


    @Autowired
	public BoreholeController(BoreholeViewService boreholeViewService, CSWCacheService cswService, NVCL2_0_DataService nvclDataService, NamespaceService namespaceService) {
		this.boreholeService = boreholeViewService;
		this.cswService = cswService;
        this.nvclDataService = nvclDataService;
		this.namespaceService = namespaceService;
	}

    /**
     * @param serviceUrl
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/nationalVirtualCoreLibraryFilterCount.do")
    public ModelAndView nationalVirtualCoreLibraryFilterCount(
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

        String gsmlpNameSpace = namespaceService.getNamespaceURI(serviceUrl, "gsmlp");

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
     *          The URL of the service to download from
     * @param boreholeName
     *          Name filter for borehole, will be wildcarded from the request
     * @param custodian
     *          The custodian of the borehole
     * @param dateOfDrilling
     *          The start or end date of drilling
     * @param nvclCollection
     * @param maxFeatures
     * @param bbox
     * @param outputFormat
     * @return
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
     * Handles the borehole count.
     *
     * @param serviceUrl
     *            the url of the service to query
     * @param boreholeName
     *            the name of the borhole to query for
     * @param dateOfDrilling
     *            the date of drilling for the borehole, usually given as a year
     * @param maxFeatures
     *            the maximum number of features to return
     * @param bbox
     *          the bounding box of the request
     * @return a count of the number of features filtered by the given parameters
     */
    @RequestMapping("/doBoreholeViewCount.do")
    public ModelAndView doBoreholeViewCount(
            @RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") Integer maxFeatures,
            @RequestParam(required = false, value = "bbox") String bbox) {

        try {
            FilterBoundingBox box = FilterBoundingBox.attemptParseFromJSON(bbox);
            WFSCountResponse response = this.boreholeService.getBoreholesCount(serviceUrl, boreholeName, null,
                    dateOfDrilling, false, maxFeatures, box);
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
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
            @RequestParam(required = false, value = "serviceUrl", defaultValue = "") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") Integer maxFeatures,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "serviceFilter", defaultValue = "") String serviceFilter,
            @RequestParam(required = false, value = "color", defaultValue = "") String color,
            @RequestParam(required = false, value = "ids") String ids,
            @RequestParam(required = false, value = "showNoneHylogged", defaultValue = "false") Boolean showNoneHylogged )

            throws Exception {

        FilterBoundingBox bbox = null;

        List<String> boreholeIdentifiers = null;
        if (ids != null && !ids.isEmpty()) {
            boreholeIdentifiers = Arrays.asList(ids.split(","));
        }

        List<String> hyloggerBoreholeIDs = null;

        String filter = this.boreholeService.getFilter(boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, null, boreholeIdentifiers, null);

        Boolean justNVCL = showNoneHylogged;

        String hyloggerFilter = this.boreholeService.getFilter(boreholeName,
                custodian, dateOfDrilling, maxFeatures, bbox,
                hyloggerBoreholeIDs);

        String gsmlpNameSpace = namespaceService.getNamespaceURI(serviceUrl, "gsmlp");
        String style = this.boreholeService.getStyle(filter, (color.isEmpty() ? "#2242c7" : color), hyloggerFilter,
                "#F87217", BoreholeService.Styles.ALL_BOREHOLES,gsmlpNameSpace);



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
