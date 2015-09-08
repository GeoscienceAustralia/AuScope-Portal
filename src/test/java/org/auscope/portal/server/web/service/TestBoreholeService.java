package org.auscope.portal.server.web.service;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

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
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.xslt.WfsToKmlTransformer;
import org.auscope.portal.gsml.BoreholeFilter;
import org.auscope.portal.nvcl.NVCLNamespaceContext;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class TestBoreholeService.
 */
public class TestBoreholeService extends PortalTestClass {

    /** The service. */
    private BoreholeService service;

    /** The mock http service caller. */
    private HttpServiceCaller mockHttpServiceCaller = context.mock(HttpServiceCaller.class);

    /** The mock method maker. */
    private WFSGetFeatureMethodMaker mockMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);

    /** The mock HTTP method */
    private HttpRequestBase mockMethod = context.mock(HttpRequestBase.class);

    /** The mock GML transformer */
    private WfsToKmlTransformer mockGmlToKml = context.mock(WfsToKmlTransformer.class);

    /** The Constant GETSCANNEDBOREHOLEXML. */
    private static final String GETSCANNEDBOREHOLEXML = "org/auscope/portal/nvcl/GetScannedBorehole.xml";

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
        service = new BoreholeService(mockHttpServiceCaller, mockMethodMaker, mockGmlToKml);
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
        final String kmlString = "kmlString";
        final List<String> restrictedIds = null;

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsml:Borehole")),
                        with(any(String.class)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));

                oneOf(mockGmlToKml).convert(gmlString, serviceURL);
                will(returnValue(kmlString));
            }
        });

        WFSTransformedResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling,
                maxFeatures, bbox, restrictedIds);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getGml());
        Assert.assertEquals(kmlString, result.getTransformed());
        Assert.assertSame(mockMethod, result.getMethod());
    }

    /**
     * Test get all boreholes no bbox and with year only in the dateOfDrilling field.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetAllBoreholesDrillYearOnly() throws Exception {
        final FilterBoundingBox bbox = null;
        final String serviceURL = "http://example.com";
        final int maxFeatures = 45;
        final String boreholeName = "borehole-name";
        final String custodian = "custodian";
        final String dateOfDrilling = "2011";
        final String gmlString = "xmlString";
        final String kmlString = "kmlString";
        final List<String> restrictedIds = null;

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsml:Borehole")),
                        with(any(String.class)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));

                oneOf(mockGmlToKml).convert(gmlString, serviceURL);
                will(returnValue(kmlString));
            }
        });

        WFSTransformedResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling,
                maxFeatures, bbox, restrictedIds);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getGml());
        Assert.assertEquals(kmlString, result.getTransformed());
        Assert.assertSame(mockMethod, result.getMethod());
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
        final String kmlString = "kmlString";
        final List<String> restrictedIds = null;

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsml:Borehole")),
                        with(any(String.class)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));
                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));

                oneOf(mockGmlToKml).convert(gmlString, serviceURL);
                will(returnValue(kmlString));
            }
        });

        WFSTransformedResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling,
                maxFeatures, bbox, restrictedIds);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getGml());
        Assert.assertEquals(kmlString, result.getTransformed());
        Assert.assertSame(mockMethod, result.getMethod());
    }

    /**
     * Test that filter style will return a style layer descriptor with correct feature type name and geom.
     */
    @Test
    public void testFilterStyle() throws Exception {
        final String nameFilter = "filterBob";
        final String custodianFilter = "filterCustodian";
        final String filterDate = "1986-10-09";
        final int maxFeatures = 10;
        final FilterBoundingBox bbox = null;

        String filter = service.getFilter(nameFilter, custodianFilter,
                filterDate, maxFeatures, bbox, null);

        String style = service.getStyle(filter, "#2242c7", null, null);
        Assert.assertNotNull(style);
        Assert.assertThat(style, Matchers.containsString("gsml:Borehole"));
        Assert.assertTrue(style.contains(service.getGeometryName()));

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
        final String kmlString = "kmlString";
        final List<String> restrictedIds = Arrays.asList("id1", "id2", "id3");
        final String filterString = (new BoreholeFilter(boreholeName, custodian, dateOfDrilling, restrictedIds))
                .getFilterStringAllRecords();

        context.checking(new Expectations() {
            {
                oneOf(mockMethodMaker).makePostMethod(with(equal(serviceURL)), with(equal("gsml:Borehole")),
                        with(equal(filterString)), with(equal(maxFeatures)), with(any(String.class)),
                        with(equal(ResultType.Results)), with(equal((String) null)), with(equal((String) null)));
                will(returnValue(mockMethod));

                oneOf(mockHttpServiceCaller).getMethodResponseAsString(with(any(HttpRequestBase.class)));
                will(returnValue(gmlString));

                oneOf(mockGmlToKml).convert(gmlString, serviceURL);
                will(returnValue(kmlString));
            }
        });

        WFSTransformedResponse result = service.getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling,
                maxFeatures, null, restrictedIds);
        Assert.assertNotNull(result);
        Assert.assertEquals(gmlString, result.getGml());
        Assert.assertEquals(kmlString, result.getTransformed());
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
