package au.gov.geoscience.portal.server.services;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URISyntaxException;

@Service
public class EarthResourceDownloadService extends BaseWFSService {
    /**
     * Creates a new instance of this class with the specified dependencies
     *
     * @param httpServiceCaller Will be used for making requests
     * @param wfsMethodMaker
     */
    @Autowired
    public EarthResourceDownloadService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker) {
        super(httpServiceCaller, wfsMethodMaker);
    }

    public InputStream downloadMinOccView(String serviceUrl, String filter, int maxFeatures, String outputFormat) throws URISyntaxException, PortalServiceException {
        String type = EarthResourceService.MIN_OCC_VIEW_FEATURE_TYPE;
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }

    public InputStream downloadMineralOccurrenceView(String serviceUrl, String filter, int maxFeatures, String outputFormat) throws PortalServiceException, URISyntaxException {
        String type = EarthResourceService.MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE;
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }

    public InputStream downloadCommodityResourceView(String serviceUrl, String filter, int maxFeatures, String outputFormat) throws PortalServiceException {
        String type = EarthResourceService.COMMODITY_RESOURCE_VIEW_FEATURE_TYPE;
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }


    public InputStream downloadMineView(String serviceUrl, String filter, int maxFeatures, String outputFormat) throws PortalServiceException, URISyntaxException {
        String type = EarthResourceService.MINE_VIEW_FEATURE_TYPE;
        if (outputFormat.toUpperCase().equals("CSV")) {
            return downloadCSV(serviceUrl, type, filter, maxFeatures);
        }
        return downloadWFS(serviceUrl, type, filter, maxFeatures);
    }
}
