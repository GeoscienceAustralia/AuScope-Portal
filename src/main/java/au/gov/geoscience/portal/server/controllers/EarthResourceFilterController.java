package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.services.EarthResourceService;
import au.gov.geoscience.portal.services.filters.*;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class EarthResourceFilterController extends BasePortalController {

    private static String ER_PREFIX = "er";
    private static String ERL_PREFIX = "erl";

    private EarthResourceService earthResourceService;

    private NamespaceService namespaceService;

    @Autowired
    public EarthResourceFilterController(EarthResourceService earthResourceService, NamespaceService namespaceService) {
        this.earthResourceService = earthResourceService;
        this.namespaceService = namespaceService;
    }

    /**
     * @param response Response to return
     * @param mineName Name of the mine
     * @param status Status of the mine
     * @param bboxJson Bounding box of the query
     * @param maxFeatures Maximum number of features to return
     * @throws Exception
     */
    @RequestMapping("miningActivityFilterStyle.do")
    public void miningActivityFilterStyle(HttpServletResponse response,
                                @RequestParam("serviceUrl") String serviceUrl,
                                @RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
                                @RequestParam(required = false, value = "status", defaultValue = "") String status,
                                @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
                                @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        MiningActivityFilter filter = this.earthResourceService.getMiningActivityFilter(mineName, null);


        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/miningactivity.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), null);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    /**
     * @param serviceUrl
     * @param mineName
     * @param status
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/miningActivityFilterCount.do")
    public ModelAndView miningActivityFilterCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
            @RequestParam(required = false, value = "status", defaultValue = "") String status,
            @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSCountResponse response = this.earthResourceService.getMiningActivityCount(serviceUrl, mineName, bbox, maxFeatures);

            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }

    }

    /**
     * @param response Response to return
     * @param mineName Name of the mine
     * @param status Status of the mine
     * @param bboxJson Bounding box of the query
     * @param maxFeatures Maximum number of features to return
     * @throws Exception
     */
    @RequestMapping("mineFilterStyle.do")
    public void mineFilterStyle(HttpServletResponse response,
                                @RequestParam("serviceUrl") String serviceUrl,
                                @RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
                                @RequestParam(required = false, value = "status", defaultValue = "") String status,
                                @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
                                @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        MineFilter2 filter = this.earthResourceService.getMineFilter(mineName, status, null);


        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/mine.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), null);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    /**
     * @param serviceUrl
     * @param mineName
     * @param status
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/mineFilterCount.do")
    public ModelAndView mineFilterCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
            @RequestParam(required = false, value = "status", defaultValue = "") String status,
            @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSCountResponse response = this.earthResourceService.getMineCount(serviceUrl, mineName, status, bbox, maxFeatures);
            
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }
        
    }


    /**
     * @param response Response to return
     * @param name Name of the mineral occurrence
     * @param bboxJson Bounding box of the query
     * @param maxFeatures Maximum number of features to return
     * @throws Exception
     */
    @RequestMapping("minOccViewFilterStyle.do")
    public void minOccViewFilterStyle(HttpServletResponse response,
                                @RequestParam("serviceUrl") String serviceUrl,
                                @RequestParam(required = false, value = "name", defaultValue = "") String name,
                                @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
                                @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        MinOccViewFilter filter = this.earthResourceService.getMinOccViewFilter(name, null);


        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/minoccview.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), null);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    /**
     * @param serviceUrl
     * @param name Name of the mineral occurrence
     * @param bboxJson Bounding box of the query
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/minOccViewFilterCount.do")
    public ModelAndView minOccViewFilterCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name", defaultValue = "") String name,
            @RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSCountResponse response = this.earthResourceService.getMinOccViewCount(serviceUrl, name, bbox, maxFeatures);

            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }

    }
    
    /**
     * Handles the styling for EarthResourceML Lite MineralOccurrenceView
     * feature types
     *
     * @param response Response to return
     * @param name Name of the mine to query
     * @param commodityUri Commodity URI term
     * @param timescaleUri Timescale URI term
     * @param bboxJson Bounding box of the query
     * @param maxFeatures Maximum number of features to return
     *
     * @throws Exception
     */
    @RequestMapping("/mineralOccurrenceViewFilterStyle.do")
    public void mineralOccurrenceViewFilterStyle(HttpServletResponse response,
            @RequestParam(required = true, value = "serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "timescaleUri") String timescaleUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {


        MineralOccurrenceViewFilter2 mineralOccurrenceViewFilter = this.earthResourceService.getMineralOccurrenceViewFilter(name, commodityUri, timescaleUri,
                null);

        Filter filter = mineralOccurrenceViewFilter.getFilter();

        String ermlLiteNamespace = namespaceService.getNamespaceURI(serviceUrl, ERL_PREFIX);

        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/mineraloccurrenceview.sld");

        String style = FilterStyle.getStyle(stream, filter, ermlLiteNamespace);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);


        styleStream.close();
        outputStream.close();
    }

    /**
     * @param serviceUrl
     * @param name
     * @param commodityUri
     * @param timescaleUri
     * @param bboxJson Bounding box of the query
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/mineralOccurrenceViewFilterCount.do")
    public ModelAndView mineralOccurrenceViewFilterCount(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "timescaleUri") String timescaleUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);


        try {
            WFSCountResponse response = this.earthResourceService.getMineralOccurrenceViewCount(serviceUrl, name,
                    commodityUri, timescaleUri, bbox, maxFeatures);
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }

    }

    /**
     * @param response
     * @param name
     * @param statusUri
     * @param bboxJson
     * @param maxFeatures
     * @throws Exception
     */
    @RequestMapping("/mineViewFilterStyle.do")
    public void mineViewFilterStyle(HttpServletResponse response,
                                    @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "statusUri") String statusUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        MineViewFilter2 filter = this.earthResourceService.getMineViewFilter(name, statusUri, null);

        String ermlLiteNamespace = namespaceService.getNamespaceURI(serviceUrl, ERL_PREFIX);

        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/mineview.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), ermlLiteNamespace);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    /**
     * @param serviceUrl
     * @param name
     * @param statusUri
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/mineViewFilterCount.do")
    public ModelAndView mineViewFilterCount(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "statusUri") String statusUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSCountResponse response = this.earthResourceService.getMineViewCount(serviceUrl, name, statusUri, bbox,
                    maxFeatures);
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }

    }

    /**
     * Handles the styling for EarthResourceML Lite CommodityResourceView
     * feature types
     * 
     * @param name Name of the mineral occurrence to query
     * @param commodityUri Commodity URI from the vocabulary
     * @param maxFeatures Maximum number of features
     *
     * @throws Exception
     */
    @RequestMapping("/commodityResourceViewFilterStyle.do")
    public void commodityResourceViewFilterStyle(HttpServletResponse response,
                                                 @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "jorcCategoryUri") String jorcCategoryUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        CommodityResourceViewFilter2 filter = this.earthResourceService.getCommodityResourceViewFilter(name, commodityUri, jorcCategoryUri, null);

        String ermlLiteNamespace = namespaceService.getNamespaceURI(serviceUrl, ERL_PREFIX);

        InputStream stream = getClass().getResourceAsStream("/au/gov/geoscience/portal/sld/commodityresourceview.sld");

        String style = FilterStyle.getStyle(stream, filter.getFilter(), ermlLiteNamespace);

        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }


    /**
     * @param serviceUrl
     * @param name
     * @param commodityUri
     * @param jorcCategoryUri
     * @param bboxJson
     * @param maxFeatures
     * @return
     */
    @RequestMapping("/commodityResourceViewFilterCount.do")
    public ModelAndView commodityResourceViewFilterCount(@RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "commodityUri") String commodityUri,
            @RequestParam(required = false, value = "jorcCategoryUri") String jorcCategoryUri,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures) {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSCountResponse response = this.earthResourceService.getCommodityResourceViewCount(serviceUrl, name, commodityUri, jorcCategoryUri, bbox, maxFeatures);
            return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }

    }
}
