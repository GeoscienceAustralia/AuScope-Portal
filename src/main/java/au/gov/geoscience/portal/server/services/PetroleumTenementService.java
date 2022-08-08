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

@Service
public class PetroleumTenementService extends BaseWFSService {
    /**
     * Creates a new instance of this class with the specified dependencies
     *
     * @param httpServiceCaller Will be used for making requests
     * @param wfsMethodMaker
     */
    @Autowired
    public PetroleumTenementService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker){
        super(httpServiceCaller, wfsMethodMaker);
    }

    public String getPetroleumTenementFilter(String name, String holder, FilterBoundingBox bbox, PetroleumTenementServiceProviderType petroleumTenementServiceProviderType) {
        PetroleumTenementFilter filter = new PetroleumTenementFilter(name, holder, petroleumTenementServiceProviderType);
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
