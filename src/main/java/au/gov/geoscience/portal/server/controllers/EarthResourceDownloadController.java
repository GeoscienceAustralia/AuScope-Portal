package au.gov.geoscience.portal.server.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.util.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.gov.geoscience.portal.services.EarthResourceDownloadService;
import au.gov.geoscience.portal.services.EarthResourceService;

/**
 * Controller for downloading data from EarthResourceML and EarthResourceML lite services.
 * 
 */
@Controller
public class EarthResourceDownloadController extends BasePortalController {

    private EarthResourceService earthResourceService;
    private EarthResourceDownloadService earthResourceDownloadService;

    @Autowired
    public EarthResourceDownloadController(EarthResourceService earthResourceService,
            EarthResourceDownloadService earthResourceDownloadService) {
        this.earthResourceService = earthResourceService;
        this.earthResourceDownloadService = earthResourceDownloadService;
    }

    /**
     * Handles EarthResourceML Mine download queries.
     * 
     * @param serviceUrl
     *             The URL of the service to query. 
     * @param mineName
     *              The mine name - wildcarded query.
     * @param status
     *              The operating status of the mine according to a controlled vocabulary.
     * @param bboxJson
     *              The bounding box in a JSON formatted string. 
     * @param maxFeatures
     *              The maximum number of features requested.
     * @param startIndex
     *              The start index for the query - useful for pagination.
     * @param outputFormat
     *              The requested output format.
     * @param response
     *              The HTTP servlet response.
     * @throws IOException
     */
    @RequestMapping("/mineDownload.do")
    public void mineDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "mineName") String mineName,
            @RequestParam(required = false, value = "status") String status,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            @RequestParam(required = false, value = "startIndex") String startIndex,
            @RequestParam(required = false, value = "outputFormat") String outputFormat, HttpServletResponse response)
            throws IOException {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        String filter = this.earthResourceService.getMineFilter(mineName, status, bbox);

        if (maxFeatures == 0 && (bbox != null || startIndex != null)) {
            maxFeatures = 200;
        }

        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();
        File file = null;
        try {
            InputStream results = this.earthResourceDownloadService.downloadMine(serviceUrl, filter, maxFeatures);

            file = FileIOUtil.writeStreamToFileTemporary(results, "APT_MFD", ".xml", true);
            FileInputStream in = new FileInputStream(file);
            FileIOUtil.writeInputToOutputStream(in, outputStream, 8 * 1024, true);

        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            FileIOUtil.writeExceptionToXMLStream(e, outputStream, false, serviceUrl);

        } finally {
            // outputStream.close();
            // VT : do not attempt to close the outputStream
            // http://stackoverflow.com/questions/1159168/should-one-call-close-on-httpservletresponse-getoutputstream-getwriter
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * Handles EarthResourceML Lite MineralOcurrenceView downloads.
     *
     * @param serviceUrl
     *              The URL of the service to query.
     * @param name
     *              The mineral occurrence name - wildcarded query.
     * @param commodityUri
     *              The URI for the commodity code according to a controlled vocabulary.
     * @param timescaleUri
     *              The URI for the geological timescale according to a controlled vocabulary.
     * @param bboxJson
     *              The bounding box in a JSON formatted string. 
     * @param outputFormat
     *              The requested output format.
     * @param maxFeatures
     *              The maximum number of features requested.
     * @param response
     *              The HTTP servlet response.
     * @throws IOException
     * @throws PortalServiceException
     * @throws URISyntaxException
     */
    @RequestMapping("mineralOccurrenceViewDownload.do")
    public void mineralOccurrenceViewDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "timescaleUri") String timescaleUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            HttpServletResponse response) throws IOException, PortalServiceException, URISyntaxException {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        String filter = this.earthResourceService.getMineralOccurrenceViewFilter(name, commodityUri, timescaleUri,
                bbox);
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.earthResourceDownloadService.downloadMineralOccurrenceView(serviceUrl, filter,
                maxFeatures, outputFormat);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();
    }

    /**
     * Handles EarthResourceML Lite MineView downloads.
     * 
     * @param serviceUrl
     *              The URL of the service to query.
     * @param name
     *              The mine name - wildcarded query.
     * @param statusUri
     *              The operating status of the mine according to a controlled vocabulary.
     * @param bboxJson
     *              The bounding box in a JSON formatted string. 
     * @param outputFormat
     *              The requested output format.
     * @param maxFeatures
     *              The maximum number of features requested.
     * @param response
     *              The HTTP servlet response.
     * @throws IOException
     * @throws PortalServiceException
     * @throws URISyntaxException
     */
    @RequestMapping("mineViewDownload.do")
    public void mineViewDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "statusUri") String statusUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            HttpServletResponse response) throws IOException, PortalServiceException, URISyntaxException {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        String filter = this.earthResourceService.getMineViewFilter(name, statusUri, bbox);
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.earthResourceDownloadService.downloadMineView(serviceUrl, filter, maxFeatures,
                outputFormat);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();
    }

    /**
     * Handles EarthResourceML Lite CommodityResourceView downloads.
     * 
     * @param serviceUrl
     *              The URL of the service to query.
     * @param name
     *              The mineral occurrence name  - wildcarded query.
     * @param commodityUri
     *              The URI for the commodity code according to a controlled vocabulary.
     * @param jorcCategoryUri
     *              The URI for the JORC category according to a controlled vocabulary.
     * @param bboxJson
     *              The bounding box in a JSON formatted string. 
     * @param outputFormat
     *              The requested output format.
     * @param maxFeatures
     *              The maximum number of features requested.
     * @param response
     *              The HTTP servlet response.
     * @throws IOException
     * @throws PortalServiceException
     */
    @RequestMapping("commodityResourceViewDownload.do")
    public void commodityResourceViewDownload(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "jorcCategoryUri") String jorcCategoryUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            HttpServletResponse response) throws IOException, PortalServiceException {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        String filter = this.earthResourceService.getCommodityResourceViewFilter(name, commodityUri, jorcCategoryUri,
                bbox);
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.earthResourceDownloadService.downloadCommodityResourceView(serviceUrl, filter,
                maxFeatures, outputFormat);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();
    }

}
