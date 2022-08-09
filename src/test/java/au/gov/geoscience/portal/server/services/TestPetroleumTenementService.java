package au.gov.geoscience.portal.server.services;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import org.auscope.portal.core.services.VocabularyFilterService;
import au.gov.geoscience.portal.server.controllers.VocabularyController;
import static org.auscope.portal.core.services.BaseWFSService.DEFAULT_SRS;
public class TestPetroleumTenementService extends PortalTestClass {
    private PetroleumTenementService petroleumTenementService;
    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker wfsMethodMaker;
    private HttpRequestBase method;
    private VocabularyFilterService vocabularyFilterService;

    @Before
    public void setUp() {
        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.wfsMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);
        this.vocabularyFilterService = context.mock(VocabularyFilterService.class);
        this.petroleumTenementService = new PetroleumTenementService(httpServiceCaller, wfsMethodMaker, vocabularyFilterService);
        this.method = context.mock(HttpRequestBase.class);
    }

    @Test
    public void testGetPetroleumTenementFilter() {
        String name = "Name";
        String holder = "BHPBilliton Limited";
        String statusUri = "http://vocabs.ga/tenement-type/exploration";
        String typeUri = "http://vocabs.ga/tenement-type/exploration";
        FilterBoundingBox bbox = null;
        Set<String> typeUris = new HashSet();
        typeUris.add(typeUri);
        Set<String> statusUris = new HashSet();
        statusUris.add(statusUri);
        context.checking(new Expectations() {
            {
                oneOf(vocabularyFilterService).getAllNarrower(VocabularyController.TENEMENT_TYPE_VOCABULARY_ID, typeUri);
                will(returnValue(typeUris));
                oneOf(vocabularyFilterService).getAllNarrower(VocabularyController.TENEMENT_STATUS_VOCABULARY_ID, statusUri);
                will(returnValue(statusUris));
            }
        });
        petroleumTenementService.getPetroleumTenementFilter(name, holder, bbox, statusUri, typeUri);
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
        });
        petroleumTenementService.getTenementCount(serviceUrl, name, owner, maxFeatures, bbox);
    }
}
