package au.gov.geoscience.portal.services;

import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EarthResourceDownloadService extends BaseWFSService {

    @Autowired
    public EarthResourceDownloadService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker) {
        super(httpServiceCaller, wfsMethodMaker);

    }

    public InputStream downloadMineralOccurrenceView(String serviceUrl, String filter, int maxFeatures,
            String outputFormat) throws PortalServiceException, URISyntaxException {

        HttpRequestBase method = generateWFSRequest(serviceUrl,
                EarthResourceService.MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE, null, filter, maxFeatures, null,
                ResultType.Results, outputFormat);
        try {
            return httpServiceCaller.getMethodResponseAsStream(method);

        } catch (Exception ex) {
            throw new PortalServiceException(method, ex);
        }
    }

    public InputStream downloadMineView(String serviceUrl, String filter, int maxFeatures, String outputFormat)
            throws PortalServiceException, URISyntaxException {

        HttpRequestBase method = generateWFSRequest(serviceUrl, EarthResourceService.MINE_VIEW_FEATURE_TYPE, null,
                filter, maxFeatures, null, ResultType.Results, outputFormat);
        try {
            return httpServiceCaller.getMethodResponseAsStream(method);

        } catch (Exception ex) {
            throw new PortalServiceException(method, ex);
        }
    }

    public InputStream downloadCommodityResourceView(String serviceUrl, String filter, int maxFeatures,
            String outputFormat) throws PortalServiceException {
        String type = EarthResourceService.COMMODITY_RESOURCE_VIEW_FEATURE_TYPE;
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);

    }

    public InputStream downloadMine(String serviceUrl, String filter, int maxFeatures) throws PortalServiceException {

        String type = EarthResourceService.MINING_FEATURE_OCCURRENCE_FEATURE_TYPE;
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }

}
