package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.nvcldataservice.*;
import au.gov.geoscience.portal.server.services.BoreholeService;
import au.gov.geoscience.portal.server.services.BoreholeViewService;
import au.gov.geoscience.portal.server.services.NVCL2_0_DataService;
import au.gov.geoscience.portal.server.services.NVCLDataService;
import au.gov.geoscience.portal.server.services.methodmaker.NVCLDataServiceMethodMaker.PlotScalarGraphType;
import net.sf.json.JSONArray;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.csw.CSWRecordsHostFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.core.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for handling requests for the NVCL boreholes
 * 
 * @author Josh Vote
 *
 */
@Controller
public class NVCLController extends BasePortalController {

    private BoreholeService boreholeService;
    private NVCLDataService dataService;
    private NVCL2_0_DataService dataService2_0;
    private CSWCacheService cswService;
    private BoreholeViewService sf0BoreholeService;

    private int BUFFERSIZE = 1024 * 1024;

    @Autowired
    public NVCLController(BoreholeService boreholeService,
                          CSWCacheService cswService,
                          NVCLDataService dataService,
                          NVCL2_0_DataService dataService2_0,
                          BoreholeViewService sf0BoreholeService) {

        this.boreholeService = boreholeService;
        this.cswService = cswService;
        this.dataService = dataService;
        this.dataService2_0 = dataService2_0;
        this.sf0BoreholeService = sf0BoreholeService;
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
    @RequestMapping("/doBoreholeFilter.do")
    public ModelAndView doBoreholeFilter(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "onlyHylogger") String onlyHyloggerString,
            @RequestParam(required = false, value = "serviceFilter", defaultValue = "") String serviceFilter,
            @RequestParam(required = false, value = "outputFormat", defaultValue = "") String outputFormat)
                    throws Exception {

        String[] serviceFilterArray = serviceFilter.split(",");

        if (!serviceFilter.equals("") && !(HttpUtil.containHost(serviceUrl, serviceFilterArray))) {
            return this.generateJSONResponseMAV(false, null, "Not Queried");
        }

        boolean onlyHylogger = false;
        if (onlyHyloggerString != null && onlyHyloggerString.length() > 0) {
            if (onlyHyloggerString.equals("on")) {
                onlyHylogger = true;
            } else {
                onlyHylogger = Boolean.parseBoolean(onlyHyloggerString);
            }
        }

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        return doBoreholeFilter(serviceUrl, boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, onlyHylogger, outputFormat, false);
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
    @RequestMapping("/doNVCLFilter.do")
    public ModelAndView doNVCLFilter(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "custodian", defaultValue = "") String custodian,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "onlyHylogger") String onlyHyloggerString,
            @RequestParam(required = false, value = "serviceFilter", defaultValue = "") String serviceFilter,
            @RequestParam(required = false, value = "outputFormat", defaultValue = "") String outputFormat)
                    throws Exception {

        String[] serviceFilterArray = serviceFilter.split(",");

        if (!serviceFilter.equals("") && !(HttpUtil.containHost(serviceUrl, serviceFilterArray))) {
            return this.generateJSONResponseMAV(false, null, "Not Queried");
        }

        boolean onlyHylogger = false;
        if (onlyHyloggerString != null && onlyHyloggerString.length() > 0) {
            if (onlyHyloggerString.equals("on")) {
                onlyHylogger = true;
            } else {
                onlyHylogger = Boolean.parseBoolean(onlyHyloggerString);
            }
        }

        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson);
        
        return doBoreholeFilter(serviceUrl, boreholeName, custodian, dateOfDrilling, -1, bbox, onlyHylogger, outputFormat, false);
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
    public ModelAndView doBoreholeFilter(String serviceUrl, String boreholeName, String custodian,
            String dateOfDrilling, int maxFeatures, FilterBoundingBox bbox,
            boolean onlyHylogger, String outputFormat, boolean countOnly) throws Exception {
        List<String> hyloggerBoreholeIDs = null;
        if (onlyHylogger) {
            try {
                hyloggerBoreholeIDs = this.boreholeService.discoverHyloggerBoreholeIDs(this.cswService,
                        new CSWRecordsHostFilter(serviceUrl));
            } catch (Exception e) {
                log.warn(String
                        .format("Error requesting list of hylogger borehole ID's from %1$s: %2$s", serviceUrl, e));
                log.debug("Exception:", e);
                return generateJSONResponseMAV(false, null,
                        "Failure when identifying which boreholes have Hylogger data.");
            }

            if (hyloggerBoreholeIDs.size() == 0) {
                log.warn("No hylogger boreholes exist (or the services are missing)");
                return generateJSONResponseMAV(false, null, "Unable to identify any boreholes with Hylogger data.");
            }
        }

        try {
            if (countOnly) {
                int count = this.boreholeService.countAllBoreholes(serviceUrl, boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, hyloggerBoreholeIDs);
                return generateJSONResponseMAV(true, count, "");
            } else {
                WFSResponse response = this.boreholeService.getAllBoreholes(serviceUrl, boreholeName, custodian,
                		dateOfDrilling, maxFeatures, bbox, hyloggerBoreholeIDs, outputFormat);
                return generateNamedJSONResponseMAV(true, "gml", response.getData(), response.getMethod());
            }
        } catch (Exception e) {
            return this.generateExceptionResponse(e, serviceUrl);
        }
    }

    /**
     * Gets the list of datasets for given borehole from the specified NVCL dataservice url.
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param holeIdentifier
     *            The unique ID of a borehole
     * @return
     */
    @RequestMapping("getNVCLDatasets.do")
    public ModelAndView getNVCLDatasets(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("holeIdentifier") String holeIdentifier) {
        List<GetDatasetCollectionResponse> responseObjs = null;
        try {
            responseObjs = dataService.getDatasetCollection(serviceUrl, holeIdentifier);

            return generateJSONResponseMAV(true, responseObjs, "");
        } catch (Exception ex) {
            log.warn(String.format("Error requesting dataset collection for hole '%1$s' from %2$s: %3$s",
                    holeIdentifier, serviceUrl, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Gets the list of logs for given NVCL dataset from the specified NVCL dataservice url.
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param datasetId
     *            The unique ID of a dataset
     * @return
     */
    @RequestMapping("getNVCLLogs.do")
    public ModelAndView getNVCLLogs(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("datasetId") String datasetId,
            @RequestParam(required = false, value = "mosaicService") Boolean forMosaicService) {
        List<GetLogCollectionResponse> responseObjs = null;
        try {
            responseObjs = dataService.getLogCollection(serviceUrl, datasetId, forMosaicService);

            return generateJSONResponseMAV(true, responseObjs, "");
        } catch (Exception ex) {
            log.warn(String.format("Error requesting log collection for dataset '%1$s' from %2$s: %3$s", datasetId,
                    serviceUrl, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Gets the list of logs for given NVCL dataset from the specified NVCL dataservice url.
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param datasetId
     *            The unique ID of a dataset
     * @return
     */
    @RequestMapping("getNVCL2_0_Logs.do")
    public ModelAndView getNVCL2_0_Logs(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("datasetId") String datasetId,
            @RequestParam(required = false, value = "mosaicService") Boolean forMosaicService) {
        List<GetLogCollectionResponse> responseObjs = null;
        try {
            responseObjs = dataService2_0.getLogCollection(serviceUrl, datasetId, forMosaicService);

            return generateJSONResponseMAV(true, responseObjs, "");
        } catch (Exception ex) {
            log.warn(String.format("Error requesting log collection for dataset '%1$s' from %2$s: %3$s", datasetId,
                    serviceUrl, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Utility function for piping the contents of serviceResponse to servletResponse
     */
    private void writeStreamResponse(HttpServletResponse servletResponse, AbstractStreamResponse serviceResponse)
            throws IOException {
        InputStream serviceInputStream = serviceResponse.getResponse();
        OutputStream responseOutput = null;

        // write our response
        try {
            servletResponse.setContentType(serviceResponse.getContentType());
            responseOutput = servletResponse.getOutputStream();

            FileIOUtil.writeInputToOutputStream(serviceInputStream, responseOutput, BUFFERSIZE, true);
        } finally {
            if (responseOutput != null) {
                responseOutput.close();
            }
        }
    }

    /**
     * Proxies a NVCL Mosaic request for mosaic imagery. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param logId
     *            The unique ID of a log (from a getNVCLLogs.do request)
     * @return
     */
    @RequestMapping("getNVCLMosaic.do")
    public void getNVCLMosaic(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("logId") String logId,
            @RequestParam(required = false, value = "width") Integer width,
            @RequestParam(required = false, value = "startSampleNo") Integer startSampleNo,
            @RequestParam(required = false, value = "endSampleNo") Integer endSampleNo,
            HttpServletResponse response) throws Exception {

        // Make our request
        MosaicResponse serviceResponse = null;
        try {
            serviceResponse = dataService.getMosaic(serviceUrl, logId, width, startSampleNo, endSampleNo);
        } catch (Exception ex) {
            log.warn(String.format("Error requesting mosaic for logid '%1$s' from %2$s: %3$s", logId, serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a NVCL Mosaic request for mosaic imagery. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param logId
     *            The unique ID of a log (from a getNVCLLogs.do request)
     * @return
     */
    @RequestMapping("getNVCL2_0_Thumbnail.do")
    public void getNVCL2_0_Thumbnail(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("dataSetId") String dataSetId,
            @RequestParam("logId") String logId,
            @RequestParam(required = false, value = "width") Integer width,
            @RequestParam(required = false, value = "startSampleNo") Integer startSampleNo,
            @RequestParam(required = false, value = "endSampleNo") Integer endSampleNo,
            HttpServletResponse response) throws Exception {

        // Make our request
        TrayThumbNailResponse serviceResponse = null;
        try {
            serviceResponse = this.dataService2_0.getTrayThumbNail(dataSetId, serviceUrl, logId, width, startSampleNo,
                    endSampleNo);
        } catch (Exception ex) {
            log.warn(String.format("Error requesting mosaic for logid '%1$s' from %2$s: %3$s", logId, serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setContentType(serviceResponse.getContentType());
        // vt:we have to hack the response because the html response has relative url and when
        // the result is proxied, the service url becomes portal's url.
        String stringResponse = IOUtils.toString(serviceResponse.getResponse());
        stringResponse = stringResponse.replace("./Display_Tray_Thumb.html", serviceUrl + "Display_Tray_Thumb.html");
        if (!stringResponse.contains("style=\"max-width: 33%")) {
            stringResponse = stringResponse.replace("<img",
                    "<img style=\"max-width: 33%;height: auto;width: auto\\9;\" ");
        }

        FileIOUtil.writeInputToOutputStream(new ByteArrayInputStream(stringResponse.getBytes()),
                response.getOutputStream(), BUFFERSIZE, true);

    }

    /**
     * Proxies a NVCL Plot Scalar request. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of an NVCL Data service
     * @param logId
     *            The unique ID of a log (from a getNVCLLogs.do request)
     * @return
     */
    @RequestMapping("getNVCLPlotScalar.do")
    public void getNVCLPlotScalar(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("logId") String logId,
            @RequestParam(required = false, value = "startDepth") Integer startDepth,
            @RequestParam(required = false, value = "endDepth") Integer endDepth,
            @RequestParam(required = false, value = "width") Integer width,
            @RequestParam(required = false, value = "height") Integer height,
            @RequestParam(required = false, value = "samplingInterval") Double samplingInterval,
            @RequestParam(required = false, value = "graphType") Integer graphTypeInt,
            @RequestParam(value = "legend", defaultValue = "0") Integer legend,
            HttpServletResponse response) throws Exception {

        // Parse our graph type
        PlotScalarGraphType graphType = null;
        if (graphTypeInt != null) {
            switch (graphTypeInt) {
            case 1:
                graphType = PlotScalarGraphType.StackedBarChart;
                break;
            case 2:
                graphType = PlotScalarGraphType.ScatteredChart;
                break;
            case 3:
                graphType = PlotScalarGraphType.LineChart;
                break;
            default:
                log.warn("Invalid graphType: " + graphTypeInt);
                response.sendError(HttpStatus.SC_BAD_REQUEST);
                return;
            }
        }

        // Make our request
        PlotScalarResponse serviceResponse = null;
        try {
            serviceResponse = dataService.getPlotScalar(serviceUrl, logId, startDepth, endDepth, width, height,
                    samplingInterval, graphType, legend);
        } catch (Exception ex) {
            log.warn(String.format("Error requesting scalar plot for logid '%1$s' from %2$s: %3$s", logId, serviceUrl,
                    ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a CSV download request to a WFS. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of an observation and measurements URL (obtained from a getDatasetCollection response)
     * @param datasetId
     *            The dataset to download
     * @return
     */
    @RequestMapping("getNVCLCSVDownload.do")
    public void getNVCLCSVDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("datasetId") String datasetId,
            HttpServletResponse response) throws Exception {

        // Make our request
        CSVDownloadResponse serviceResponse = null;
        try {
            serviceResponse = dataService.getCSVDownload(serviceUrl, datasetId);
        } catch (Exception ex) {
            log.warn(String.format("Error requesting csw download for datasetId '%1$s' from %2$s: %3$s", datasetId,
                    serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setHeader("Content-Disposition", "attachment; filename=GETPUBLISHEDSYSTEMTSA.csv");
        writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a CSV download request to a WFS from a NVCL 2.0 service. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of an observation and measurements URL (obtained from a getDatasetCollection response)
     * @param datasetId
     *            The dataset to download
     * @return
     */
    @RequestMapping("getNVCL2_0_CSVDownload.do")
    public void getNVCL2_0_CSVDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("logIds") String[] logIds,
            HttpServletResponse response) throws Exception {

        // Make our request
        CSVDownloadResponse serviceResponse = null;
        try {
            serviceResponse = dataService2_0.getNVCL2_0_CSVDownload(serviceUrl, logIds);
        } catch (Exception ex) {
            log.warn(String.format("Error requesting csw download for logId '%1$s' from %2$s: %3$s", logIds,
                    serviceUrl, ex));
            log.debug("Exception:", ex);
            if (ex.getMessage().contains("404")) {
                String htmlMessage = "<html><head><title>Error 404</title></head>"
                        +
                        "<body><h1>HTTP Status 404 - </h1><p>You could be seeing this error because the service does not support this operation</p>"
                        +
                        "</body></html>";

                FileIOUtil.writeInputToOutputStream(new ByteArrayInputStream(htmlMessage.getBytes()),
                        response.getOutputStream(), BUFFERSIZE, true);
                return;
            } else {
                throw new ServletException(ex);
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=downloadScalar.csv");
        writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a NVCL TSG download request. Writes directly to the HttpServletResponse
     *
     * One of (but not both) datasetId and matchString must be specified
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @param datasetId
     *            [Optional] a dataset id chosen by user (list of dataset id can be obtained thru calling the get log collection service)
     * @param matchString
     *            [Optional] Its value is part or all of a proper drillhole name. The first dataset found to match in the database is downloaded
     * @param lineScan
     *            [Optional] yes or no. If no then the main image component is not downloaded. The default is yes.
     * @param spectra
     *            [Optional] yes or no. If no then the spectral component is not downloaded. The default is yes.
     * @param profilometer
     *            [Optional] yes or no. If no then the profilometer component is not downloaded. The default is yes.
     * @param trayPics
     *            [Optional] yes or no. If no then the individual tray pictures are not downloaded. The default is yes.
     * @param mosaicPics
     *            [Optional] yes or no. If no then the hole mosaic picture is not downloaded. The default is yes.
     * @param mapPics
     *            [Optional] yes or no. If no then the map pictures are not downloaded. The default is yes.
     * @return
     */
    @RequestMapping("getNVCLTSGDownload.do")
    public void getNVCLTSGDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("email") String email,
            @RequestParam(required = false, value = "datasetId") String datasetId,
            @RequestParam(required = false, value = "matchString") String matchString,
            @RequestParam(required = false, value = "lineScan") Boolean lineScan,
            @RequestParam(required = false, value = "spectra") Boolean spectra,
            @RequestParam(required = false, value = "profilometer") Boolean profilometer,
            @RequestParam(required = false, value = "trayPics") Boolean trayPics,
            @RequestParam(required = false, value = "mosaicPics") Boolean mosaicPics,
            @RequestParam(required = false, value = "mapPics") Boolean mapPics,
            HttpServletResponse response) throws Exception {

        // It's likely that the GUI (due to its construction) may send multiple email parameters
        // Spring condenses this into a single CSV string (which is bad)
        email = email.split(",")[0];

        // Make our request
        TSGDownloadResponse serviceResponse = null;
        try {
            serviceResponse = dataService.getTSGDownload(serviceUrl, email, datasetId, matchString, lineScan, spectra,
                    profilometer, trayPics, mosaicPics, mapPics);
        } catch (Exception ex) {
            if (ex.getMessage().contains("404 Not Found")) {
                FileIOUtil.writeInputToOutputStream(NVCLController.get404HTMLError(), response.getOutputStream(),
                        BUFFERSIZE, true);
                return;
            }
            log.warn(String.format("Error requesting tsg download from %1$s: %2$s", serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setContentType(serviceResponse.getContentType());
        String stringResponse = IOUtils.toString(serviceResponse.getResponse());
        stringResponse = stringResponse.replace("downloadtsg.html", serviceUrl + "downloadtsg.html");

        FileIOUtil.writeInputToOutputStream(new ByteArrayInputStream(stringResponse.getBytes()),
                response.getOutputStream(), BUFFERSIZE, true);

    }

    /**
     * Proxies a NVCL TSG status request. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @return
     */
    @RequestMapping("getNVCLTSGDownloadStatus.do")
    public void getNVCLTSGDownloadStatus(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("email") String email,
            HttpServletResponse response) throws Exception {

        // Make our request
        TSGStatusResponse serviceResponse = null;
        try {
            serviceResponse = dataService.checkTSGStatus(serviceUrl, email);
        } catch (Exception ex) {
            if (ex.getMessage().contains("404 Not Found")) {
                FileIOUtil.writeInputToOutputStream(NVCLController.get404HTMLError(), response.getOutputStream(),
                        BUFFERSIZE, true);
                return;
            }
            log.warn(String.format("Error requesting tsg status from %1$s: %2$s", serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setContentType(serviceResponse.getContentType());
        String stringResponse = IOUtils.toString(serviceResponse.getResponse());
        stringResponse = stringResponse.replace("downloadtsg.html", serviceUrl + "downloadtsg.html");
        stringResponse = stringResponse.replace("href", " target='_blank' href");

        FileIOUtil.writeInputToOutputStream(new ByteArrayInputStream(stringResponse.getBytes()),
                response.getOutputStream(), BUFFERSIZE, true);

        // writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a NVCL WFS download request. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @param boreholeId
     *            selected borehole id (use as feature id for filtering purpose)
     * @param omUrl
     *            The valid url for the Observations and Measurements WFS
     * @param typeName
     *            The url parameter for the wfs request
     * @return
     */
    @RequestMapping("getNVCLWFSDownload.do")
    public void getNVCLWFSDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("email") String email,
            @RequestParam("boreholeId") String boreholeId,
            @RequestParam("omUrl") String omUrl,
            @RequestParam("typeName") String typeName,
            HttpServletResponse response) throws Exception {

        // Make our request
        WFSDownloadResponse serviceResponse = null;
        try {
            serviceResponse = dataService.getWFSDownload(serviceUrl, email, boreholeId, omUrl, typeName);
        } catch (Exception ex) {
            if (ex.getMessage().contains("404 Not Found")) {
                FileIOUtil.writeInputToOutputStream(NVCLController.get404HTMLError(), response.getOutputStream(),
                        BUFFERSIZE, true);
                return;
            }
            log.warn(String
                    .format("Error requesting %1$s download from omUrl '%2$s' for borehole '%3$s' and nvcl dataservice %4$s: %5$s",
                            typeName, omUrl, boreholeId, serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        writeStreamResponse(response, serviceResponse);
    }

    /**
     * Proxies a NVCL WFS status request. Writes directly to the HttpServletResponse
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @return
     */
    @RequestMapping("getNVCLWFSDownloadStatus.do")
    public void getNVCLWFSDownloadStatus(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam("email") String email,
            HttpServletResponse response) throws Exception {

        // Make our request
        WFSStatusResponse serviceResponse = null;
        try {
            serviceResponse = dataService.checkWFSStatus(serviceUrl, email);
        } catch (Exception ex) {
            if (ex.getMessage().contains("404 Not Found")) {
                FileIOUtil.writeInputToOutputStream(NVCLController.get404HTMLError(), response.getOutputStream(),
                        BUFFERSIZE, true);
                return;
            }
            log.warn(String.format("Error requesting wfs status from %1$s: %2$s", serviceUrl, ex));
            log.debug("Exception:", ex);
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        writeStreamResponse(response, serviceResponse);
    }

    private static InputStream get404HTMLError() throws IOException {
        InputStream input = NVCLController.class.getResourceAsStream("/htmlerror/NVCL404Response.htm");
        return input;
    }

    /**
     * Proxies a NVCL getAlgorithms request. Returns a JSON response
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @return
     */
    @RequestMapping("getNVCLAlgorithms.do")
    public ModelAndView getNVCLWFSDownloadStatus(@RequestParam("serviceUrl") String serviceUrl) throws Exception {
        try {
            List<AlgorithmOutputResponse> algorithms = dataService2_0.getAlgorithms(serviceUrl);
            return generateJSONResponseMAV(true, algorithms, "");
        } catch (Exception ex) {
            log.warn("Unable to fetch NVCL algorithms for " + serviceUrl, ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Proxies a NVCL getAlgorithms request. Returns a JSON response
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @return
     */
    @RequestMapping("getNVCLClassifications.do")
    public ModelAndView getNVCLWFSDownloadStatus(@RequestParam("serviceUrl") String serviceUrl, @RequestParam("algorithmOutputId") int algorithmOutputId) throws Exception {
        try {
            List<AlgorithmOutputClassification> classifications = dataService2_0.getClassifications(serviceUrl, algorithmOutputId);
            return generateJSONResponseMAV(true, classifications, "");
        } catch (Exception ex) {
            log.warn("Unable to fetch NVCL classifications for " + serviceUrl + " and algorithmOutputId " + algorithmOutputId, ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Submits an NVCL processing job to the remote analytical services
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/submitSF0NVCLProcessingJob.do")
    public ModelAndView submitSF0NVCLProcessingJob(
            @RequestParam("email") String email,
            @RequestParam("jobName") String jobName,

            @RequestParam("wfsUrl") String[] wfsUrls,

            @RequestParam(required = false, value = "boreholeName", defaultValue = "") String boreholeName,
            @RequestParam(required = false, value = "dateOfDrilling", defaultValue = "") String dateOfDrilling,
            @RequestParam(required = false, value = "bbox") String bboxJson,

            @RequestParam("algorithmOutputId") int algorithmOutputId,
            @RequestParam("classification") String classification,
            @RequestParam("startDepth") int startDepth,
            @RequestParam("endDepth") int endDepth,
            @RequestParam("operator") String operator,
            @RequestParam("value") String value,
            @RequestParam("units") String units,
            @RequestParam("span") int span)
            throws Exception {

        String filterString = null;
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson);
        filterString = sf0BoreholeService.getFilter(boreholeName, "", dateOfDrilling, -1, bbox, null, true);

        try {
            boolean result = this.dataService2_0.submitProcessingJob(email, jobName, wfsUrls, filterString, algorithmOutputId, classification, startDepth, endDepth, operator, value, units, span);
            return generateJSONResponseMAV(result);
        } catch (Exception ex) {
            log.error("Unable to submit processing job: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Returns an array of JSON AnalyticalJobStatus objects describing job status responses for a given email
     * @param email
     * @return
     */
    @RequestMapping("/checkNVCLProcessingJob.do")
    public ModelAndView checkNVCLProcessingJob(@RequestParam("email") String email) {
        try {
            List<AnalyticalJobStatus> statuses = this.dataService2_0.checkProcessingJobs(email);
            return generateJSONResponseMAV(true, statuses, "");
        } catch (Exception ex) {
            log.error("Unable to check NVCL processing jobs: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Returns an object containing passing, failing and erroring borehole ID's for a given processing job
     * @param email
     * @return
     */
    @RequestMapping("/getNVCLProcessingResults.do")
    public ModelAndView getNVCLProcessingResults(@RequestParam("jobId") String jobId) {
        try {
            AnalyticalJobResults results = this.dataService2_0.getProcessingResults(jobId);
            return generateJSONResponseMAV(true, Arrays.asList(results), "");
        } catch (Exception ex) {
            log.error("Unable to check NVCL processing jobs: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Proxies a CSV download request to a WFS from a NVCL 2.0 service. Parses the response into a series of 1m averaged bins.
     *
     * @param serviceUrl
     *            The URL of an observation and measurements URL (obtained from a getDatasetCollection response)
     * @param datasetId
     *            The dataset to download
     * @return
     */
    @RequestMapping("getNVCL2_0_JSONDataBinned.do")
    public ModelAndView getNVCL2_0_JSONDataBinned(@RequestParam("serviceUrl") String serviceUrl,
                                                  @RequestParam("logIds") String[] logIds) throws Exception {

        //Make our request
        try {
            String responseStr = dataService2_0.getNVCL2_0_JSONDownsampledData(serviceUrl, logIds);
            return generateJSONResponseMAV(true, responseStr, "");

        } catch (Exception ex) {
            log.warn(String.format("Error requesting json download for logId '%1$s' from %2$s: %3$s", logIds,serviceUrl, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }


    /**
     * Fetches NVCL TSG Jobs data
     * @param jobId
     *          job id of data to be downloaded
     * @param boreholeId
     *          borehole id of data to be downloaded
     */
    @RequestMapping("getNVCL2_0_JobsScalarBinned.do")
    public ModelAndView getNVCL2_0_JobsScalarBinned(@RequestParam("jobIds") String[] jobIds, @RequestParam("boreholeId") String boreholeId) {

        //Make our request
        try {
            BinnedCSVResponse response = dataService2_0.getNVCL2_0_JobsScalarBinned(jobIds, boreholeId, 1.0);

            return generateJSONResponseMAV(true, Arrays.asList(response), "");

        } catch (Exception ex) {
            log.warn(String.format("Error requesting scalar csv download from NVCL job for boreholeId '%1$s': %2$s", boreholeId, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }

    /**
     * Request for TSG job id and job name for a borehole id
     *
     * @param serviceUrl
     *          The URL of job id request
     * @param boreholeId
    boreholeId of the job
     *
     * @return
     */
    @RequestMapping("getNVCL2_0_TsgJobsByBoreholeId.do")
    public ModelAndView getNVCL2_0_TsgJobsByBoreholeId(@RequestParam("boreholeId") String boreholeId) throws Exception {
        //Make our request
        try {
            JSONArray jsonResponse = dataService2_0.getNVCL2_0_getTsgJobsByBoreholeId(boreholeId);
            return generateJSONResponseMAV(true, jsonResponse, "");

        } catch (Exception ex) {
            log.warn(String.format("Error requesting TSG job id for borehole id '%1$s': %2$s", boreholeId, ex));
            log.debug("Exception:", ex);
            return generateJSONResponseMAV(false);
        }
    }
}
