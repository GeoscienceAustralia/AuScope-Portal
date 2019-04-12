package au.gov.geoscience.portal.services;

import au.gov.geoscience.portal.server.controllers.VocabularyController;
import au.gov.geoscience.portal.services.filters.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.IFilter;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Service
public class EarthResourceService extends BaseWFSService {

    private static String ER_PREFIX = "er";
    private static String ERL_PREFIX = "erl";

    public static final String MINING_FEATURE_OCCURRENCE_FEATURE_TYPE = "er:MiningFeatureOccurrence";
    public static final String MIN_OCC_VIEW_FEATURE_TYPE = "mo:MinOccView";


    public static final String MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE = "erl:MineralOccurrenceView";
    public static final String MINE_VIEW_FEATURE_TYPE = "erl:MineView";
    public static final String COMMODITY_RESOURCE_VIEW_FEATURE_TYPE = "erl:CommodityResourceView";

    private VocabularyFilterService vocabularyFilterService;

    private NamespaceService namespaceService;




    @Autowired
    public EarthResourceService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker, VocabularyFilterService vocabularyFilterService, NamespaceService namespaceService) {
        super(httpServiceCaller, wfsMethodMaker);
        this.vocabularyFilterService = vocabularyFilterService;
        this.namespaceService = namespaceService;
    }

    /**
     * @param mineName
     * @param status
     * @param bbox
     * @return
     */
    public MineFilter2 getMineFilter(String mineName, String status, FilterBoundingBox bbox) {
        MineFilter2 filter = new MineFilter2(mineName, status, bbox);
        return filter;
    }

    /**
     * @param serviceUrl
     * @param mineName
     * @param status
     * @param bbox
     * @param maxFeatures
     * @return
     * @throws URISyntaxException
     * @throws PortalServiceException
     */
    public WFSCountResponse getMineCount(String serviceUrl, String mineName, String status, FilterBoundingBox bbox,
            int maxFeatures) throws URISyntaxException, PortalServiceException, TransformerException {

        MineFilter2 filter = new MineFilter2(mineName, status, bbox);

        String filterString = filter.generateFilterStringAllRecords();

        HttpRequestBase method = generateWFSRequest(serviceUrl, MINING_FEATURE_OCCURRENCE_FEATURE_TYPE, null,
                filterString, maxFeatures, null, ResultType.Hits);
        return getWfsFeatureCount(method);
    }

    /**
     * @param mineName
     * @param bbox
     * @return
     */
    public MiningActivityFilter getMiningActivityFilter(String mineName, FilterBoundingBox bbox) {
        MiningActivityFilter filter = new MiningActivityFilter(mineName, bbox);
        return filter;
    }

    /**
     * @param serviceUrl
     * @param mineName
     * @param bbox
     * @param maxFeatures
     * @return
     * @throws URISyntaxException
     * @throws PortalServiceException
     * @throws TransformerException
     */
    public WFSCountResponse getMiningActivityCount(String serviceUrl, String mineName, FilterBoundingBox bbox,
                                         int maxFeatures) throws URISyntaxException, PortalServiceException, TransformerException {

        MiningActivityFilter filter = new MiningActivityFilter(mineName, bbox);

        String filterString = filter.generateFilterStringAllRecords();

        HttpRequestBase method = generateWFSRequest(serviceUrl, MINING_FEATURE_OCCURRENCE_FEATURE_TYPE, null,
                filterString, maxFeatures, null, ResultType.Hits);
        return getWfsFeatureCount(method);
    }


    /**
     * @param name
     * @param bbox
     * @return
     */
    public MinOccViewFilter getMinOccViewFilter(String name, FilterBoundingBox bbox) {
        MinOccViewFilter filter = new MinOccViewFilter(name, bbox);
        return filter;
    }

    public WFSCountResponse getMinOccViewCount(String serviceUrl, String name, FilterBoundingBox bbox,
                                         int maxFeatures) throws URISyntaxException, PortalServiceException, TransformerException {

        MinOccViewFilter filter = new MinOccViewFilter(name, bbox);

        String filterString = filter.generateFilterStringAllRecords();

        HttpRequestBase method = generateWFSRequest(serviceUrl, MIN_OCC_VIEW_FEATURE_TYPE, null,
                filterString, maxFeatures, null, ResultType.Hits);
        return getWfsFeatureCount(method);
    }

    /**
     * @param name Name of the mineral occurrence to filter
     * @param commodityUri Commodity URI to filter, hierarchical query of the vocabulary
     * @param timescaleUri Geologic timescale  URI to filter, hierarchical query of the vocabulary
     * @param bbox Bounding box
     * @return
     * @throws PortalServiceException
     * @throws URISyntaxException
     */
    public MineralOccurrenceViewFilter2 getMineralOccurrenceViewFilter(String name, String commodityUri, String timescaleUri,
                                                   FilterBoundingBox bbox) throws PortalServiceException, URISyntaxException {
        Set<String> timescaleUris = new HashSet<>();
        Set<String> commodityUris = new HashSet<>();
        if (timescaleUri != null && !timescaleUri.isEmpty()) {
            timescaleUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TIMESCALE_VOCABULARY_ID, timescaleUri);
        }
        if (commodityUri != null && !commodityUri.isEmpty()) {
            commodityUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.COMMODITY_VOCABULARY_ID, commodityUri);
        }
       return new MineralOccurrenceViewFilter2(name, commodityUris, timescaleUris, bbox);

    }


    /**
     * @param serviceUrl
     * @param name
     * @param commodityUri
     * @param timescaleUri
     * @param bbox
     * @param maxFeatures
     * @return
     * @throws URISyntaxException
     * @throws PortalServiceException
     */
    public WFSCountResponse getMineralOccurrenceViewCount(String serviceUrl, String name, String commodityUri,
                                                          String timescaleUri, FilterBoundingBox bbox, int maxFeatures)
            throws URISyntaxException, PortalServiceException {

        String ermlLiteNamespace = namespaceService.getNamespaceURI(serviceUrl, ERL_PREFIX);

        Set<String> timescaleUris = new HashSet<>();
        Set<String> commodityUris = new HashSet<>();

        if (timescaleUri != null && !timescaleUri.isEmpty()) {
            timescaleUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TIMESCALE_VOCABULARY_ID, timescaleUri);
        }

        if (commodityUri != null && !commodityUri.isEmpty()) {
            commodityUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.COMMODITY_VOCABULARY_ID, commodityUri);
        }
        MineralOccurrenceViewFilter2 filter = new MineralOccurrenceViewFilter2(name, commodityUris, timescaleUris, bbox);

        String filterString = filter.generateFilterStringAllRecords();
        HttpRequestBase method = generateWFSRequest(serviceUrl, MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE, null,
                filterString, maxFeatures, null, ResultType.Hits);

        return getWfsFeatureCount(method);
    }

    /**
     * @param name Name or partial name of the mine to filter
     * @param statusUri
     * @param bbox
     * @return OGC Filter string
     */
    public MineViewFilter2 getMineViewFilter(String name, String statusUri, FilterBoundingBox bbox) {
        Set<String> statusUris = new HashSet<>();

        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.MINE_STATUS_VOCABULARY_ID, statusUri);
        }

        return new MineViewFilter2(name, statusUris, bbox);

    }

    /**
     * Returns a count of the features that meet the specified parameters for the query
     *
     * @param serviceUrl
     * @param name
     * @param statusUri
     * @param bbox
     * @param maxFeatures
     * @return
     * @throws URISyntaxException
     * @throws PortalServiceException
     */
    public WFSCountResponse getMineViewCount(String serviceUrl, String name, String statusUri, FilterBoundingBox bbox,
                                             int maxFeatures) throws URISyntaxException, PortalServiceException {

        Set<String> statusUris = new HashSet<>();

        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.MINE_STATUS_VOCABULARY_ID, statusUri);
        }

        MineViewFilter2 filter = new MineViewFilter2(name, statusUris, bbox);
        String filterString = filter.generateFilterStringAllRecords();
        HttpRequestBase method = generateWFSRequest(serviceUrl, MINE_VIEW_FEATURE_TYPE, null, filterString, maxFeatures,
                null, ResultType.Hits);

        return getWfsFeatureCount(method);

    }

    /**
     * @param mineralOccurrenceName
     * @param commodityUri
     * @param jorcCategoryUri
     * @param bbox
     * @return
     */
    public CommodityResourceViewFilter2 getCommodityResourceViewFilter(String mineralOccurrenceName, String commodityUri, String jorcCategoryUri,
                                                                       FilterBoundingBox bbox) {
        Set<String> commodityUris = new HashSet<>();

        if (commodityUri != null && !commodityUri.isEmpty()) {
            commodityUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.COMMODITY_VOCABULARY_ID, commodityUri);
        }

        CommodityResourceViewFilter2 filter = new CommodityResourceViewFilter2(mineralOccurrenceName, commodityUris, jorcCategoryUri, bbox);
        return filter;
    }


    /**
     * @param serviceUrl
     * @param mineralOccurrenceName
     * @param commodityUri
     * @param jorcCategoryUri
     * @param bbox
     * @param maxFeatures
     * @return
     * @throws URISyntaxException
     * @throws PortalServiceException
     */
    public WFSCountResponse getCommodityResourceViewCount(String serviceUrl, String mineralOccurrenceName,
                                                          String commodityUri, String jorcCategoryUri, FilterBoundingBox bbox, int maxFeatures)
            throws URISyntaxException, PortalServiceException {
        Set<String> commodityUris = new HashSet<>();

        if (commodityUri != null && !commodityUri.isEmpty()) {
            commodityUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.COMMODITY_VOCABULARY_ID, commodityUri);
        }

        CommodityResourceViewFilter2 filter = new CommodityResourceViewFilter2(mineralOccurrenceName, commodityUris, jorcCategoryUri, bbox);
        String filterString = filter.generateFilterStringAllRecords();
        HttpRequestBase method = generateWFSRequest(serviceUrl, COMMODITY_RESOURCE_VIEW_FEATURE_TYPE, null,
                filterString, maxFeatures, null, ResultType.Hits);

        return getWfsFeatureCount(method);
    }


    /**
     * @param filter
     * @param bbox
     * @return
     */
    public static String generateFilterString(IFilter filter, FilterBoundingBox bbox) {
        String filterString = null;
        if (bbox == null) {
            filterString = filter.getFilterStringAllRecords();
        } else {
            filterString = filter.getFilterStringBoundingBox(bbox);
        }

        return filterString;
    }



}
