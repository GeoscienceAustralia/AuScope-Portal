package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.contexts.NVCLNamespaceContext;
import au.gov.geoscience.portal.server.services.filters.BoreholeViewFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.csw.CSWRecordsHostFilter;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.csw.AbstractCSWOnlineResource;
import org.auscope.portal.core.services.responses.csw.AbstractCSWOnlineResource.OnlineResourceType;
import org.auscope.portal.core.services.responses.csw.CSWOnlineResourceImpl;
import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * The Class TestBoreholeService.
 */
public class TestSF0BoreholeService extends PortalTestClass {

    /** The service. */
    private BoreholeViewService service;

    /** The mock http service caller. */
    private HttpServiceCaller mockHttpServiceCaller = context.mock(HttpServiceCaller.class);

    /** The mock method maker. */
    private WFSGetFeatureMethodMaker mockMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);

    /** The mock HTTP method */
    private HttpRequestBase mockMethod = context.mock(HttpRequestBase.class);

    /** The Constant GETSCANNEDBOREHOLEXML. */
    private static final String GETSCANNEDBOREHOLEXML = "au/gov/geoscience/portal/server/services/GetScannedBorehole.xml";

    /** The Constant HOLEIDS. */
    private static final String[] HOLEIDS = new String[] {"WTB5", "GSDD006", "GDDH7"};

    /**
     * Setup.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        service = new BoreholeViewService(mockHttpServiceCaller, mockMethodMaker);
    }

    /**
     * Test get all boreholes no bbox.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetAllBoreholesNoBbox() throws Exception {
        final FilterBoundingBox bbox = null;
        final String serviceURL = "http://example.com";
        final int maxFeatures = 45;
        final String boreholeName = "borehole-name";
        final String custodian = "custodian";
        final String dateOfDrilling = "2011-01-01";
        final String gmlString = "xmlString";

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsmlp:BoreholeView")),
                        with(any(String.class)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));
            }
        });

        WFSResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian,
        		dateOfDrilling, maxFeatures, bbox);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getData());
        Assert.assertSame(mockMethod, result.getMethod());
    }

    /**
     * Test that SF0 filter style will return a style layer descriptor with correct feature type name
     */
    @Test
    public void testSF0FilterStyle() throws Exception {
        final String nameFilter = "filterBob";
        final String custodianFilter = "filterCustodian";
        final String dateOfDrilling = "1986-10-09";
        final int maxFeatures = 10;
        final FilterBoundingBox bbox = null;

        String filter = service.getFilter(nameFilter, custodianFilter, dateOfDrilling, maxFeatures, bbox, null);

        String style = service.getStyle(filter, null, "#2242c7","http://xmlns.geosciml.org/geosciml-portrayal/4.0", BoreholeService.Styles.ALL_BOREHOLES);
        Assert.assertNotNull(style);
        Assert.assertThat(style, Matchers.containsString("gsmlp:BoreholeView"));

    }

    /**
     * Test get all boreholes bbox.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetAllBoreholesBbox() throws Exception {
        final FilterBoundingBox bbox = new FilterBoundingBox("mySrs", new double[] {0, 1}, new double[] {2, 3});
        final String serviceURL = "http://example.com";
        final int maxFeatures = 45;
        final String boreholeName = "borehole-name";
        final String custodian = "custodian";
        final String dateOfDrilling = "2011-01-01";
        final String gmlString = "xmlString";

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsmlp:BoreholeView")),
                        with(any(String.class)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));
                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));
            }
        });

        WFSResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian,
        		dateOfDrilling, maxFeatures, bbox);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getData());
        Assert.assertSame(mockMethod, result.getMethod());
    }

    /**
     * Test get restricted boreholes bbox.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetRestrictedBoreholesBbox() throws Exception {
        final String serviceURL = "http://example.com";
        final int maxFeatures = 45;
        final String boreholeName = "asda";
        final String custodian = "shaksdhska";
        final String dateOfDrilling = "2010-01-02";
        final String gmlString = "xmlString";

        final String filterString = (new BoreholeViewFilter(boreholeName, custodian, dateOfDrilling, null, null, null))
                .getFilterStringAllRecords();

        context.checking(new Expectations() {
            {
                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsmlp:BoreholeView")),
                        with(equal(filterString)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));
            }
        });

        WFSResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian,
        		dateOfDrilling, maxFeatures, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getData());
        Assert.assertSame(mockMethod, result.getMethod());
    }

    /**
     * Tests that the service correctly parses a response from an NVCL WFS.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetHyloggerIDs() throws Exception {
        final CSWRecord mockRecord1 = context.mock(CSWRecord.class, "mockRecord1"); //good record
        final CSWRecord mockRecord2 = context.mock(CSWRecord.class, "mockRecord2"); //has the wrong wfs
        final CSWRecord mockRecord3 = context.mock(CSWRecord.class, "mockRecord3"); //has no wfs
        final CSWCacheService mockCSWService = context.mock(CSWCacheService.class);
        final CSWRecordsHostFilter hostFilter = new CSWRecordsHostFilter("");
        final AbstractCSWOnlineResource mockRecord1Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.1.resource.1"), "wfs", "dne", "description");
        final AbstractCSWOnlineResource mockRecord1Resource2 = new CSWOnlineResourceImpl(new URL(
                "http://record.1.resource.2"), "wfs", NVCLNamespaceContext.PUBLISHED_DATASETS_TYPENAME, "description");

        final AbstractCSWOnlineResource mockRecord2Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.2.resource.1"), "wfs", "dne", "description");

        final String successResponse = ResourceUtil.loadResourceAsString(GETSCANNEDBOREHOLEXML);

        context.checking(new Expectations() {
            {
                oneOf(mockCSWService).getWFSRecords();
                will(returnValue(Arrays.asList(mockRecord1, mockRecord2, mockRecord3)));

                oneOf(mockRecord1).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord1Resource1, mockRecord1Resource2}));

                oneOf(mockRecord2).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord2Resource1}));

                oneOf(mockRecord3).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {}));

                oneOf(mockMethodMaker).makeGetMethod(mockRecord1Resource2.getLinkage().toString(),
                        mockRecord1Resource2.getName(), (Integer) null, null);
                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(successResponse));
            }
        });

        List<String> restrictedIDs = service.discoverHyloggerBoreholeIDs(mockCSWService, hostFilter);
        Assert.assertNotNull(restrictedIDs);
        Assert.assertArrayEquals(HOLEIDS, restrictedIDs.toArray(new String[restrictedIDs.size()]));
    }

    /**
     * Tests that the service correctly parses a response from an NVCL WFS (even when there is an error).
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetHyloggerIDsWithError() throws Exception {
        final CSWRecord mockRecord1 = context.mock(CSWRecord.class, "mockRecord1"); //will return failure
        final CSWRecord mockRecord2 = context.mock(CSWRecord.class, "mockRecord2"); //good record
        final CSWCacheService mockCSWService = context.mock(CSWCacheService.class);
        final HttpRequestBase mockRecord1Method = context.mock(HttpRequestBase.class, "rec1method");
        final HttpRequestBase mockRecord2Method = context.mock(HttpRequestBase.class, "rec2method");
        final CSWRecordsHostFilter hostFilter = new CSWRecordsHostFilter("");
        final AbstractCSWOnlineResource mockRecord1Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.1.resource.1"), "wfs", NVCLNamespaceContext.PUBLISHED_DATASETS_TYPENAME, "description");
        final AbstractCSWOnlineResource mockRecord2Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.2.resource.1"), "wfs", NVCLNamespaceContext.PUBLISHED_DATASETS_TYPENAME, "description");

        final String successResponse = ResourceUtil.loadResourceAsString(GETSCANNEDBOREHOLEXML);

        context.checking(new Expectations() {
            {
                oneOf(mockCSWService).getWFSRecords();
                will(returnValue(Arrays.asList(mockRecord1, mockRecord2)));

                oneOf(mockRecord1).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord1Resource1}));

                oneOf(mockRecord2).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord2Resource1}));

                oneOf(mockMethodMaker).makeGetMethod(mockRecord1Resource1.getLinkage().toString(),
                        mockRecord1Resource1.getName(), (Integer) null, null);
                will(returnValue(mockRecord1Method));

                oneOf(mockMethodMaker).makeGetMethod(mockRecord2Resource1.getLinkage().toString(),
                        mockRecord2Resource1.getName(), (Integer) null, null);
                will(returnValue(mockRecord2Method));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(mockRecord1Method);
                will(throwException(new Exception("I'm an exception!")));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(mockRecord2Method);
                will(returnValue(successResponse));
            }
        });

        List<String> restrictedIDs = service.discoverHyloggerBoreholeIDs(mockCSWService, hostFilter);
        Assert.assertNotNull(restrictedIDs);
        Assert.assertArrayEquals(HOLEIDS, restrictedIDs.toArray(new String[restrictedIDs.size()]));
    }

    /**
     * Tests that the service correctly parses a response from an NVCL WFS.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetHyloggerWithOWSError() throws Exception {
        final CSWRecord mockRecord1 = context.mock(CSWRecord.class, "mockRecord1"); //good record
        final CSWRecord mockRecord2 = context.mock(CSWRecord.class, "mockRecord2"); //has the wrong wfs
        final CSWRecord mockRecord3 = context.mock(CSWRecord.class, "mockRecord3"); //has no wfs
        final CSWCacheService mockCSWService = context.mock(CSWCacheService.class);
        final CSWRecordsHostFilter hostFilter = new CSWRecordsHostFilter("");
        final AbstractCSWOnlineResource mockRecord1Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.1.resource.1"), "wfs", "dne", "description");
        final AbstractCSWOnlineResource mockRecord1Resource2 = new CSWOnlineResourceImpl(new URL(
                "http://record.1.resource.2"), "wfs", NVCLNamespaceContext.PUBLISHED_DATASETS_TYPENAME, "description");

        final AbstractCSWOnlineResource mockRecord2Resource1 = new CSWOnlineResourceImpl(new URL(
                "http://record.2.resource.1"), "wfs", "dne", "description");

        final String owsErrorResponse = ResourceUtil
                .loadResourceAsString("org/auscope/portal/core/test/responses/ows/OWSExceptionSample1.xml");

        context.checking(new Expectations() {
            {
                oneOf(mockCSWService).getWFSRecords();
                will(returnValue(Arrays.asList(mockRecord1, mockRecord2, mockRecord3)));

                oneOf(mockRecord1).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord1Resource1, mockRecord1Resource2}));

                oneOf(mockRecord2).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {mockRecord2Resource1}));

                oneOf(mockRecord3).getOnlineResourcesByType(hostFilter, OnlineResourceType.WFS);
                will(returnValue(new AbstractCSWOnlineResource[] {}));

                oneOf(mockMethodMaker).makeGetMethod(mockRecord1Resource2.getLinkage().toString(),
                        mockRecord1Resource2.getName(), (Integer) null, null);
                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(owsErrorResponse));
            }
        });

        List<String> restrictedIDs = service.discoverHyloggerBoreholeIDs(mockCSWService, hostFilter);
        Assert.assertNotNull(restrictedIDs);
        Assert.assertEquals(0, restrictedIDs.size());
    }

}
