package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.contexts.RockPropertyNamespaceContext;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.xslt.WfsToKmlTransformer;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.auscope.portal.core.services.BaseWFSService.DEFAULT_SRS;
import static org.junit.Assert.*;

public class TestRockPropertyService extends PortalTestClass {

    private RockPropertyService rockPropertyService;

    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker methodMaker;
    private HttpRequestBase mockMethod;
    private RockPropertyNamespaceContext mockNamespaceContext;

    @Before
    public void setUp() {

        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.methodMaker = context.mock(WFSGetFeatureMethodMaker.class);
        this.mockMethod = context.mock(HttpRequestBase.class);

        this.mockNamespaceContext = context.mock(RockPropertyNamespaceContext.class);

        context.checking(new Expectations() {
            {
                oneOf(methodMaker).setNamespaces(with(any(RockPropertyNamespaceContext.class)));
            }
        });
        this.rockPropertyService = new RockPropertyService(httpServiceCaller, methodMaker);

    }


    @Test
    public void testGetRockPropertyCount() throws Exception {
        String serviceUrl = "http://portal.ga/wfs";
        String rockProperty = "BulkDensity";

        FilterBoundingBox bbox = null;

        InputStream stream = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/rockPropertyFeatureCount.xml");

        context.checking(new Expectations() {
            {
                oneOf(methodMaker).makePostMethod(serviceUrl, rockProperty, "", 0, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null,
                    null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(stream, null)));
                allowing(mockMethod).releaseConnection();
            }
        });

        WFSCountResponse response = rockPropertyService.getRockPropertyCount(serviceUrl, rockProperty, 0, bbox);

        Assert.assertEquals(19827, response.getNumberOfFeatures());
    }

    @Test
    public void testDownloadRockProperties() throws Exception {
        String serviceUrl = "http://portal.ga/wfs";
        String rockProperty = "BulkDensity";
        FilterBoundingBox bbox = null;
        context.checking(new Expectations() {
            {
                oneOf(methodMaker).makePostMethod(serviceUrl, rockProperty, "", 0, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Results, "csv",
                        null);
                will(returnValue(mockMethod));
                oneOf(httpServiceCaller).getMethodResponseAsStream(mockMethod);
            }
        });

       rockPropertyService.downloadRockProperties(serviceUrl, rockProperty, bbox, "csv");
    }
}