package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.MineralTenementServiceProviderType;
import au.gov.geoscience.portal.server.controllers.VocabularyController;
import au.gov.geoscience.portal.server.services.filters.MineralTenementFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.IFilter;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Service
public class MineralTenementService extends BaseWFSService {

    private final VocabularyFilterService vocabularyFilterService;

    /**
     * Creates a new instance of this class with the specified dependencies
     *
     * @param httpServiceCaller Will be used for making requests
     * @param wfsMethodMaker
     */
    @Autowired
    public MineralTenementService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker, VocabularyFilterService vocabularyFilterService) {
        super(httpServiceCaller, wfsMethodMaker);
        this.vocabularyFilterService = vocabularyFilterService;
    }

    public String getMineralTenementFilter(String name, String typeUri, String owner, String statusUri, String endDate,
    FilterBoundingBox bbox, MineralTenementServiceProviderType mineralTenementServiceProviderType) {

        Set<String> typeUris = new HashSet<>();

        if (typeUri != null && !typeUri.isEmpty()) {
            typeUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_TYPE_VOCABULARY_ID, typeUri);
        }

        Set<String> statusUris = new HashSet<>();
        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_STATUS_VOCABULARY_ID, statusUri);
        }
        MineralTenementFilter filter = new MineralTenementFilter(name, owner, statusUris, typeUris, mineralTenementServiceProviderType);
        return generateFilterString(filter, bbox);
    }

    /**
     * @param serviceUrl
     * @param tenementName
     * @param owner
     * @param maxFeatures
     * @param bbox
     * @return
     * @throws PortalServiceException
     * @throws URISyntaxException
     */
    public WFSCountResponse getTenementCount(String serviceUrl, String name, String owner, int maxFeatures,
                                             FilterBoundingBox bbox) throws PortalServiceException, URISyntaxException {
        String filterString;
        MineralTenementFilter mineralTenementFilter = new MineralTenementFilter(name, owner);
        if (bbox == null) {
            filterString = mineralTenementFilter.getFilterStringAllRecords();
        } else {
            filterString = mineralTenementFilter.getFilterStringBoundingBox(bbox);
        }

        HttpRequestBase method = null;

        method = generateWFSRequest(serviceUrl, "mt:MineralTenement", null, filterString, maxFeatures, null,
                WFSGetFeatureMethodMaker.ResultType.Hits);
        return getWfsFeatureCount(method);

    }


    /**
     * Returns an OGC filter XML string
     *
     * @param filter
     * @param bbox
     * @return
     */
    public static String generateFilterString(IFilter filter, FilterBoundingBox bbox) {
        String filterString;
        if (bbox == null) {
            filterString = filter.getFilterStringAllRecords();
        } else {
            filterString = filter.getFilterStringBoundingBox(bbox);
        }

        return filterString;
    }


}
