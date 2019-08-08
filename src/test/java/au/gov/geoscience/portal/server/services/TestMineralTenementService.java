package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.MineralTenementServiceProviderType;
import au.gov.geoscience.portal.server.controllers.VocabularyController;
import au.gov.geoscience.portal.server.services.filters.MineralTenementFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.VocabularyFilterService;
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

import static org.auscope.portal.core.services.BaseWFSService.DEFAULT_SRS;

public class TestMineralTenementService extends PortalTestClass {

    private MineralTenementService mineralTenementService;

    private HttpServiceCaller httpServiceCaller;
    private WFSGetFeatureMethodMaker wfsMethodMaker;
    private VocabularyFilterService vocabularyFilterService;
    private HttpRequestBase method;

    @Before
    public void setUp() {
        this.httpServiceCaller = context.mock(HttpServiceCaller.class);
        this.wfsMethodMaker = context.mock(WFSGetFeatureMethodMaker.class);
        this.vocabularyFilterService = context.mock(VocabularyFilterService.class);
        this.mineralTenementService = new MineralTenementService(httpServiceCaller, wfsMethodMaker, vocabularyFilterService);

        this.method = context.mock(HttpRequestBase.class);
    }

    @Test
    public void testGetMineralTenementFilter() {
        String name = "Name";
        String typeUri = "http://vocabs.ga/tenement-type/exploration";
        String owner = "BHPBilliton Limited";
        String statusUri = "http://vocabs.ga/tenement-type/exploration";
        String endDate = null;
        FilterBoundingBox bbox = null;
        MineralTenementServiceProviderType providerType = MineralTenementServiceProviderType.GeoServer;

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
                         }

        );

        mineralTenementService.getMineralTenementFilter(name, typeUri, owner, statusUri, endDate, bbox, providerType);
    }

    @Test
    public void testGetTenementCount() throws PortalServiceException, URISyntaxException, IOException {
        String serviceUrl = "http://portal.ga/wfs";
        String name = "Name";
        String owner = "BHPBilliton Limited";
        int maxFeatures = 0;
        FilterBoundingBox bbox = null;
        String filterString = new MineralTenementFilter(name, owner).getFilterStringBoundingBox(bbox);

        InputStream in = ResourceUtil.loadResourceAsStream("au/gov/geoscience/portal/server/services/mineralTenementCount.xml");

        context.checking(new Expectations() {
                             {
                                 oneOf(wfsMethodMaker).makePostMethod(serviceUrl, "mt:MineralTenement", filterString, maxFeatures, DEFAULT_SRS, WFSGetFeatureMethodMaker.ResultType.Hits, null, null);
                                 will(returnValue(method));
                                 oneOf(httpServiceCaller).getMethodResponseAsStream(method);
                                 will(returnValue(new HttpClientInputStream(in, null)));
                                 oneOf(method).releaseConnection();
                             }
                         }
        );

        mineralTenementService.getTenementCount(serviceUrl, name, owner, maxFeatures, bbox);
    }

}