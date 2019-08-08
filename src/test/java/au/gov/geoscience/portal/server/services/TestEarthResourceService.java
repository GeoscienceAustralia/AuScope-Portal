package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.filters.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import static au.gov.geoscience.portal.server.controllers.VocabularyController.*;
import static au.gov.geoscience.portal.server.services.EarthResourceService.*;
import static org.auscope.portal.core.services.BaseWFSService.DEFAULT_SRS;
import static org.junit.Assert.*;

public class TestEarthResourceService extends PortalTestClass {

    private EarthResourceService earthResourceService;

    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker methodMaker;
    private VocabularyFilterService vocabularyFilterService;

    private HttpRequestBase method;

    @Before
    public void setUp() {
        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.methodMaker = context.mock(WFSGetFeatureMethodMaker.class);
        this.vocabularyFilterService = context.mock(VocabularyFilterService.class);


        this.earthResourceService = new EarthResourceService(httpServiceCaller, methodMaker, vocabularyFilterService);

        this.method = context.mock(HttpRequestBase.class);
    }


    @Test
    public void testGetMinOccViewFilter() {
        String name = "*isa*";
        FilterBoundingBox bbox = null;

        String filter = new MinOccViewFilter(name).getFilterStringAllRecords();

        String resultFilter = this.earthResourceService.getMinOccViewFilter(name, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetMinOccViewCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";
        FilterBoundingBox bbox = null;
        int maxFeatures = 0;

        String filter = new MinOccViewFilter(name).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/minOccViewCount.xml");

        context.checking(new Expectations() {
            {
                oneOf(methodMaker).makePostMethod(serviceUrl, MIN_OCC_VIEW_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getMinOccViewCount(serviceUrl, name, bbox, maxFeatures);

        Assert.assertEquals(16716, count.getNumberOfFeatures());
    }

    @Test
    public void testGetMineFilter() {
        String name = "*isa*";
        String status = "operating";
        FilterBoundingBox bbox = null;

        String filter = new MineFilter(name, status).getFilterStringAllRecords();

        String resultFilter = this.earthResourceService.getMineFilter(name, status, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetMineCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";
        String status = "operating";
        FilterBoundingBox bbox = null;
        int maxFeatures = 0;


        String filter = new MineFilter(name, status).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/mineCount.xml");

        context.checking(new Expectations() {
            {
                oneOf(methodMaker).makePostMethod(serviceUrl, MINING_FEATURE_OCCURRENCE_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getMineCount(serviceUrl, name, status, bbox, maxFeatures);

        Assert.assertEquals(969, count.getNumberOfFeatures());
    }

    @Test
    public void testGetMiningActivityFilter() {
        String name = "*isa*";
        FilterBoundingBox bbox = null;

        String filter = new MiningActivityFilter(name).getFilterStringAllRecords();

        String resultFilter = this.earthResourceService.getMiningActivityFilter(name, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetMiningActivityCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";
        FilterBoundingBox bbox = null;
        int maxFeatures = 0;

        String filter = new MiningActivityFilter(name).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/minOccViewCount.xml");

        context.checking(new Expectations() {
            {
                oneOf(methodMaker).makePostMethod(serviceUrl, MINING_FEATURE_OCCURRENCE_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getMiningActivityCount(serviceUrl, name, bbox, maxFeatures);

        Assert.assertEquals(16716, count.getNumberOfFeatures());
    }

    @Test
    public void testGetMineralOccurrenceViewFilter() throws PortalServiceException, URISyntaxException {
        String name = "*isa*";
        String commodityUri = "http://vocabs.ga/commodity/gold";
        String timescaleUri = "http://vocabs.ga/timescale/gold";

        Set<String> commodityUris = new HashSet<>();
        commodityUris.add(commodityUri);

        Set<String> timescaleUris = new HashSet<>();
        timescaleUris.add(timescaleUri);

        FilterBoundingBox bbox = null;

        String filter = new MineralOccurrenceViewFilter(name, commodityUris, timescaleUris).getFilterStringAllRecords();

        context.checking(new Expectations() {
            {
                oneOf(vocabularyFilterService).getAllNarrower(TIMESCALE_VOCABULARY_ID, timescaleUri);
                will(returnValue(timescaleUris));
                oneOf(vocabularyFilterService).getAllNarrower(COMMODITY_VOCABULARY_ID, commodityUri);
                will(returnValue(commodityUris));
            }
        });

        String resultFilter = this.earthResourceService.getMineralOccurrenceViewFilter(name, commodityUri, timescaleUri, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetMineralOccurrenceViewCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";
        String commodityUri = "http://vocabs.ga/commodity/gold";
        String timescaleUri = "http://vocabs.ga/timescale/gold";

        Set<String> commodityUris = new HashSet<>();
        commodityUris.add(commodityUri);

        Set<String> timescaleUris = new HashSet<>();
        timescaleUris.add(timescaleUri);

        FilterBoundingBox bbox = null;
        int maxFeatures = 0;

        String filter = new MineralOccurrenceViewFilter(name, commodityUris, timescaleUris).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/mineralOccurrenceViewCount.xml");

        context.checking(new Expectations() {
            {
                oneOf(vocabularyFilterService).getAllNarrower(TIMESCALE_VOCABULARY_ID, timescaleUri);
                will(returnValue(timescaleUris));
                oneOf(vocabularyFilterService).getAllNarrower(COMMODITY_VOCABULARY_ID, commodityUri);
                will(returnValue(commodityUris));
                oneOf(methodMaker).makePostMethod(serviceUrl, MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getMineralOccurrenceViewCount(serviceUrl, name, commodityUri, timescaleUri, bbox, maxFeatures);

        Assert.assertEquals(7391, count.getNumberOfFeatures());
    }

    @Test
    public void testGetMineViewFilter() {
        String name = "*isa*";
        String statusUri = "http://vocabs.ga/status/operating";
        FilterBoundingBox bbox = null;

        Set<String> statusUris = new HashSet<>();
        statusUris.add(statusUri);
        String filter = new MineViewFilter(name, statusUris).getFilterStringAllRecords();
        context.checking(new Expectations() {
            {

                oneOf(vocabularyFilterService).getAllNarrower(MINE_STATUS_VOCABULARY_ID, statusUri);
                will(returnValue(statusUris));
            }
        });

        String resultFilter = this.earthResourceService.getMineViewFilter(name, statusUri, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetMineViewCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";
        String statusUri = "http://vocabs.ga/status/operating";
        FilterBoundingBox bbox = null;
        int maxFeatures = 0;

        Set<String> statusUris = new HashSet<>();
        statusUris.add(statusUri);
        String filter = new MineViewFilter(name, statusUris).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/mineViewCount.xml");

        context.checking(new Expectations() {
            {

                oneOf(vocabularyFilterService).getAllNarrower(MINE_STATUS_VOCABULARY_ID, statusUri);
                will(returnValue(statusUris));
                oneOf(methodMaker).makePostMethod(serviceUrl, MINE_VIEW_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getMineViewCount(serviceUrl, name, statusUri, bbox, maxFeatures);

        Assert.assertEquals(7422, count.getNumberOfFeatures());
    }

    @Test
    public void testGetCommodityResourceViewFilter() {
        String name = "*isa*";
        String commodityUri = "http://vocabs.ga/commodity/gold";
        String jorcCategoryUri = "http://vocabs.ga/category/proved-reserve";

        FilterBoundingBox bbox = null;

        Set<String> commodityUris = new HashSet<>();
        commodityUris.add(commodityUri);

        String filter = new CommodityResourceViewFilter(name, commodityUris, jorcCategoryUri).getFilterStringAllRecords();

        context.checking(new Expectations() {
            {

                oneOf(vocabularyFilterService).getAllNarrower(COMMODITY_VOCABULARY_ID, commodityUri);
                will(returnValue(commodityUris));
            }
        });

        String resultFilter = this.earthResourceService.getCommodityResourceViewFilter(name, commodityUri, jorcCategoryUri, bbox);

        Assert.assertEquals(filter, resultFilter);
    }

    @Test
    public void testGetCommodityResourceViewCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "*isa*";

        String commodityUri = "http://vocabs.ga/commodity/gold";
        String jorcCategoryUri = "http://vocabs.ga/category/proved-reserve";

        FilterBoundingBox bbox = null;
        int maxFeatures = 0;

        Set<String> commodityUris = new HashSet<>();
        commodityUris.add(commodityUri);

        String filter = new CommodityResourceViewFilter(name, commodityUris, jorcCategoryUri).getFilterStringAllRecords();

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/commodityResourceViewCount.xml");

        context.checking(new Expectations() {
            {

                oneOf(vocabularyFilterService).getAllNarrower(COMMODITY_VOCABULARY_ID, commodityUri);
                will(returnValue(commodityUris));
                oneOf(methodMaker).makePostMethod(serviceUrl, COMMODITY_RESOURCE_VIEW_FEATURE_TYPE, filter, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);

                will(returnValue(method));
                oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                will(returnValue(new HttpClientInputStream(in, null)));
                oneOf(method).releaseConnection();

            }
        });

        WFSCountResponse count = this.earthResourceService.getCommodityResourceViewCount(serviceUrl, name, commodityUri, jorcCategoryUri, bbox, maxFeatures);

        Assert.assertEquals(10801, count.getNumberOfFeatures());
    }

}
