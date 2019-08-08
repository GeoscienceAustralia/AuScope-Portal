package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.filters.CommodityResourceViewFilter;
import au.gov.geoscience.portal.server.services.filters.MinOccViewFilter;
import au.gov.geoscience.portal.server.services.filters.MineViewFilter;
import au.gov.geoscience.portal.server.services.filters.MineralOccurrenceViewFilter;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TestEarthResourceDownloadService extends PortalTestClass {

    private EarthResourceDownloadService earthResourceDownloadService;

    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker wfsGetFeatureMethodMaker;

    private HttpRequestBase mockMethod;

    @Before
    public void setUp() {
        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.wfsGetFeatureMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);

        this.earthResourceDownloadService = new EarthResourceDownloadService(httpServiceCaller, wfsGetFeatureMethodMaker);

        this.mockMethod = context.mock(HttpRequestBase.class);
    }

    @Test
    public void testDownloadMinOccView() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String filter = new MinOccViewFilter("*Isa*").getFilterStringAllRecords();
        String type = EarthResourceService.MIN_OCC_VIEW_FEATURE_TYPE;
        int maxFeatures = 10;
        String outputFormat = "xml";

        String xml = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/services/minOccView.xml");
        InputStream stream = IOUtils.toInputStream(xml);

        context.checking(new Expectations() {
            {
                oneOf(wfsGetFeatureMethodMaker).makePostMethod(serviceUrl, type, filter, maxFeatures, BaseWFSService.DEFAULT_SRS,  WFSGetFeatureMethodMaker.ResultType.Results, null, null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(stream, null)));
            }
        });

        InputStream download = earthResourceDownloadService.downloadMinOccView(serviceUrl, filter, maxFeatures, outputFormat);
    }

    @Test
    public void testDownloadMineralOccurrenceView() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String filter = new MineralOccurrenceViewFilter("*Isa*", "http://vocabs.ga/commodity/gold", "http://vocabs.ga/timescale/archean").getFilterStringAllRecords();
        String type = EarthResourceService.MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE;
        int maxFeatures = 10;
        String outputFormat = "xml";

        String xml = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/services/minOccView.xml");
        InputStream stream = IOUtils.toInputStream(xml);

        context.checking(new Expectations() {
            {
                oneOf(wfsGetFeatureMethodMaker).makePostMethod(serviceUrl, type, filter, maxFeatures, BaseWFSService.DEFAULT_SRS,  WFSGetFeatureMethodMaker.ResultType.Results, null, null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(stream, null)));
            }
        });
        InputStream download =  earthResourceDownloadService.downloadMineralOccurrenceView(serviceUrl, filter, maxFeatures, outputFormat);
    }

    @Test
    public void testDownloadCommodityResourceView() throws PortalServiceException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String filter = new CommodityResourceViewFilter("*Isa*", "http://vocabs.ga/commodity/gold", "http://vocabs.ga/category/proved-reserve").getFilterStringAllRecords();
        String type = EarthResourceService.COMMODITY_RESOURCE_VIEW_FEATURE_TYPE;
        int maxFeatures = 10;
        String outputFormat = "xml";

        String xml = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/services/minOccView.xml");
        InputStream stream = IOUtils.toInputStream(xml);

        context.checking(new Expectations() {
            {
                oneOf(wfsGetFeatureMethodMaker).makePostMethod(serviceUrl, type, filter, maxFeatures, BaseWFSService.DEFAULT_SRS,  WFSGetFeatureMethodMaker.ResultType.Results, null, null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(stream, null)));
            }
        });
        InputStream download = earthResourceDownloadService.downloadCommodityResourceView(serviceUrl, filter, maxFeatures, outputFormat);
    }
    @Test
    public void testDownloadMineView() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String filter = new MineViewFilter("*Isa*", "http://vocabs.ga/mine-status/operating").getFilterStringAllRecords();
        String type = EarthResourceService.MINE_VIEW_FEATURE_TYPE;
        int maxFeatures = 10;
        String outputFormat = "xml";

        String xml = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/services/minOccView.xml");
        InputStream stream = IOUtils.toInputStream(xml);

        context.checking(new Expectations() {
            {
                oneOf(wfsGetFeatureMethodMaker).makePostMethod(serviceUrl, type, filter, maxFeatures, BaseWFSService.DEFAULT_SRS,  WFSGetFeatureMethodMaker.ResultType.Results, null, null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(stream, null)));
            }
        });
        InputStream download = earthResourceDownloadService.downloadMineView(serviceUrl, filter, maxFeatures, outputFormat);
    }
}