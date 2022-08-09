package au.gov.geoscience.portal.server.services;
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
     * @param httpServiceCaller Will be used for making requests
     * @param wfsMethodMaker
     */
    @Autowired
    public PetroleumTenementService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker, VocabularyFilterService vocabularyFilterService){
        super(httpServiceCaller, wfsMethodMaker);
        this.vocabularyFilterService = vocabularyFilterService;
    }

    public String getPetroleumTenementFilter(String name, String holder, FilterBoundingBox bbox, String statusUri, String tenementTypeUri, String applicationDate) {
        Set<String> tenementTypeUris = new HashSet<>();
        if (tenementTypeUri != null && !tenementTypeUri.isEmpty()) {
            tenementTypeUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_TYPE_VOCABULARY_ID, tenementTypeUri);
        }
        Set<String> statusUris = new HashSet<>();
        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_STATUS_VOCABULARY_ID, statusUri);
        }
        PetroleumTenementFilter filter = new PetroleumTenementFilter(name, holder, statusUris, tenementTypeUris);
        return generateFilterString(filter, bbox);
    }

     /**
     * @param serviceUrl
     * @param name
     * @param holder
     * @param maxFeatures
     * @param bbox
     * @return
     * @throws PortalServiceException
     * @throws URISyntaxException
     */
    public WFSCountResponse getTenementCount(String serviceUrl, String name, String holder, int maxFeatures, FilterBoundingBox bbox) throws PortalServiceException, URISyntaxException {
        String filterString;
        PetroleumTenementFilter petroleumTenementFilter = new PetroleumTenementFilter(name, holder);
        if (bbox == null) {
            filterString = petroleumTenementFilter.getFilterStringAllRecords();
        } else {
            filterString = petroleumTenementFilter.getFilterStringBoundingBox(bbox);
        }
        HttpRequestBase method = null;
        method = generateWFSRequest(serviceUrl, "pt:PetroleumTenement", null, filterString, maxFeatures, null, WFSGetFeatureMethodMaker.ResultType.Hits);
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
    
    public InputStream getAllTenements(String serviceUrl, String type, String filter, int maxFeatures, String outputFormat) throws PortalServiceException {
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }
}
