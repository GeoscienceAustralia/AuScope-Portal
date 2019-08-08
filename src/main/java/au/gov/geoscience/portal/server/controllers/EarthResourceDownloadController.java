package au.gov.geoscience.portal.server.controllers;



import au.gov.geoscience.portal.server.services.EarthResourceDownloadService;
import au.gov.geoscience.portal.server.services.EarthResourceService;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.util.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

@Controller
public class EarthResourceDownloadController extends BasePortalController {

    private EarthResourceService earthResourceService;
    private EarthResourceDownloadService earthResourceDownloadService;

    @Autowired
    public EarthResourceDownloadController(EarthResourceService earthResourceService, EarthResourceDownloadService earthResourceDownloadService) {
        this.earthResourceService = earthResourceService;
        this.earthResourceDownloadService = earthResourceDownloadService;
    }


    @RequestMapping("minOccViewFilterDownload.do")
    public void minOccViewFilterDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            HttpServletResponse response)
            throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson,ogcServiceProviderType);

        String filter = this.earthResourceService.getMinOccViewFilter(name, bbox);
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();

        InputStream results = this.earthResourceDownloadService.downloadMinOccView(serviceUrl,
                filter, maxFeatures, outputFormat);
        FileIOUtil.writeInputToOutputStream(results, outputStream, 8 * 1024, true);
        outputStream.close();

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

    @RequestMapping("commodityResourceViewDownload.do")
    void commodityResourceViewDownload(@RequestParam("serviceUrl") String serviceUrl,
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
