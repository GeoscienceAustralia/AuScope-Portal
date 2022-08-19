package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.filters.PetroleumTenementFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.IFilter;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.auscope.portal.core.services.VocabularyFilterService;
import au.gov.geoscience.portal.server.controllers.VocabularyController;

@Service
public class PetroleumTenementService extends BaseWFSService {
    private final VocabularyFilterService vocabularyFilterService;

    /**
     * Creates a new instance of this class with the specified dependencies
     *
     * @param httpServiceCaller - will be used for making requests
     * @param wfsMethodMaker    - for generating Web Feature Service requests
     */
    @Autowired
    public PetroleumTenementService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker, VocabularyFilterService vocabularyFilterService) {
        super(httpServiceCaller, wfsMethodMaker);
        this.vocabularyFilterService = vocabularyFilterService;
    }

    /**
     * @param name            -  tenement name
     * @param holder          - tenement holder
     * @param statusUri       - tenement status URL
     * @param tenementTypeUri - tenement type URL
     * @param bbox            - bounding box
     * @return
     */
    public String getPetroleumTenementFilter(String serviceUrl, String name, String holder, FilterBoundingBox bbox, String statusUri, String tenementTypeUri, PetroleumTenementServiceProviderType serviceProviderType) {
        Set<String> tenementTypeUris = new HashSet<>();
        if (tenementTypeUri != null && !tenementTypeUri.isEmpty()) {
            tenementTypeUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.PETROLEUM_TENEMENT_TYPE_VOCABULARY_ID, tenementTypeUri);
        }
        Set<String> statusUris = new HashSet<>();
        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.PETROLEUM_TENEMENT_STATUS_VOCABULARY_ID, statusUri);
        }
        PetroleumTenementFilter filter = new PetroleumTenementFilter(serviceUrl, name, holder, statusUris, tenementTypeUris, serviceProviderType);
        return generateFilterString(filter, bbox, serviceProviderType);
    }

    /**
     * @param serviceUrl  - geoserver service url
     * @param name        - tenement name
     * @param holder      - tenement holder
     * @param maxFeatures - max number of features
     * @param bbox        - bounding box
     * @return - returns the tenement count
     * @throws PortalServiceException - PortalServiceException
     * @throws URISyntaxException     - URISyntaxException
     */
    public WFSCountResponse getTenementCount(String serviceUrl, String name, String holder, int maxFeatures, FilterBoundingBox bbox, PetroleumTenementServiceProviderType serviceProviderType) throws PortalServiceException, URISyntaxException {
        String filterString;
        PetroleumTenementFilter petroleumTenementFilter = new PetroleumTenementFilter(serviceUrl, name, holder, serviceProviderType);
        if (bbox == null) {
            filterString = petroleumTenementFilter.getFilterStringAllRecords();
        } else {
            filterString = petroleumTenementFilter.getFilterStringBoundingBox(bbox);
        }
        HttpRequestBase method = generateWFSRequest(serviceUrl, serviceProviderType.featureType(), null, filterString, maxFeatures, null, WFSGetFeatureMethodMaker.ResultType.Hits);
        return getWfsFeatureCount(method);
    }

    /**
     * @param filter - IFilter
     * @param bbox   - bounding box
     * @return - returns an OGC filter XML string
     */
    public static String generateFilterString(PetroleumTenementFilter filter, FilterBoundingBox bbox, PetroleumTenementServiceProviderType serviceProviderType) {
        String filterString;
        if (bbox == null) {
            filterString = filter.getFilterStringAllRecords();
        } else {
            filterString = filter.setFilterStringBoundingBoxServiceType(bbox, serviceProviderType);
        }
        return filterString;
    }

    public InputStream getAllTenements(String serviceUrl, String type, String filter, int maxFeatures, String outputFormat) throws PortalServiceException {
        if (outputFormat.equalsIgnoreCase("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }
}
