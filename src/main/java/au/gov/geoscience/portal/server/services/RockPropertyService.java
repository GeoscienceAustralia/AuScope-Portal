package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.contexts.RockPropertyNamespaceContext;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.SimpleBBoxFilter;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.xslt.WfsToKmlTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class RockPropertyService extends BaseWFSService {


    @Autowired
    public RockPropertyService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker methodMaker) {
        super(httpServiceCaller, methodMaker);
        this.wfsMethodMaker.setNamespaces(new RockPropertyNamespaceContext());

    }

    public WFSCountResponse getRockPropertyCount(String serviceUrl, String rockProperty, int maxFeatures,
                                                 FilterBoundingBox bbox) throws Exception {
        SimpleBBoxFilter filter = new SimpleBBoxFilter();
        String filterString = filter.getFilterStringBoundingBox(bbox);
        HttpRequestBase method = generateWFSRequest(serviceUrl, rockProperty, null, filterString, maxFeatures, null,
                ResultType.Hits);
        return getWfsFeatureCount(method);
    }

    /**
     * @param serviceUrl
     * @param type
     * @param filterString
     * @param outputFormat
     * @return
     * @throws Exception
     */
    public InputStream downloadRockProperties(String serviceUrl, String type, FilterBoundingBox bbox, String outputFormat)
            throws Exception {
        HttpRequestBase method = null;
        try {
            SimpleBBoxFilter filter = new SimpleBBoxFilter();
            String filterString = filter.getFilterStringBoundingBox(bbox);
            method = generateWFSRequest(serviceUrl, type, null, filterString, null, null, ResultType.Results,
                    outputFormat);
            return httpServiceCaller.getMethodResponseAsStream(method);

        } catch (Exception ex) {
            throw new PortalServiceException(method, "Error when attempting to download from:" + serviceUrl, ex);
        }

    }

}