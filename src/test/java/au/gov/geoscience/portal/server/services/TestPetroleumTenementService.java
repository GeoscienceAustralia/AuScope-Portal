package au.gov.geoscience.portal.server.services;
import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.filters.PetroleumTenementFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import static org.auscope.portal.core.services.BaseWFSService.DEFAULT_SRS;

public class TestPetroleumTenementService extends PortalTestClass {
    private PetroleumTenementService petroleumTenementService;
    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker wfsMethodMaker;
    private HttpRequestBase method;

    @Before
    public void setUp() {
        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.wfsMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);
        this.petroleumTenementService = new PetroleumTenementService(httpServiceCaller, wfsMethodMaker);
        this.method = context.mock(HttpRequestBase.class);
    }

    @Test
    public void testGetPetroleumTenementFilter() {
        String name = "Name";
        String holder = "BHPBilliton Limited";
        FilterBoundingBox bbox = null;
        PetroleumTenementServiceProviderType providerType = PetroleumTenementServiceProviderType.GeoServer;
        String result = petroleumTenementService.getPetroleumTenementFilter(name, holder, bbox, providerType);
        Assert.notNull(result);
    }

    @Test
    public void testGetTenementCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "Name";
        String owner = "BHPBilliton Limited";
        int maxFeatures = 0;
        FilterBoundingBox bbox = null;
        String filterString = new PetroleumTenementFilter(name, owner).getFilterStringBoundingBox(bbox);

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/petroleumTenementCount.xml");

        context.checking(new Expectations() {
                             {
                                 oneOf(wfsMethodMaker).makePostMethod(serviceUrl, "pt:PetroleumTenement", filterString, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);
                                 will(returnValue(method));
                                 oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                                 will(returnValue(new HttpClientInputStream(in, null)));
                                 oneOf(method).releaseConnection();
                             }
                         }
        );

        petroleumTenementService.getTenementCount(serviceUrl, name, owner, maxFeatures, bbox);
    }
}
