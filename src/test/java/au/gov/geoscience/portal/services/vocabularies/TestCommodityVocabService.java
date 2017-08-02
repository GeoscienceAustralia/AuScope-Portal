package au.gov.geoscience.portal.services.vocabularies;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.Format;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.View;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Resource;

import au.gov.geoscience.portal.services.vocabularies.CommodityVocabService;
import au.gov.geoscience.portal.services.vocabularies.VocabularyLookup;

public class TestCommodityVocabService extends PortalTestClass {

    private HttpRequestBase mockMethod = context.mock(HttpRequestBase.class, "mockMethod");
    private HttpRequestBase mockMethod2 = context.mock(HttpRequestBase.class, "mockMethod2");
    private HttpServiceCaller mockServiceCaller = context.mock(HttpServiceCaller.class);
    private SISSVoc3MethodMaker mockMethodMaker = context.mock(SISSVoc3MethodMaker.class);
    private String baseUrl = "http://example.org:8080/sissvoc/path";

    private CommodityVocabService commodityVocabService;

    @Before
    public void setup() {
        commodityVocabService = new CommodityVocabService(mockServiceCaller, mockMethodMaker, baseUrl);
        commodityVocabService.setPageSize(50);
    }

    @SuppressWarnings("unused")
    private boolean containsResourceUri(List<Resource> list, String uri) {
        for (Resource res : list) {
            if (res.getURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testGetCommodityConcepts() throws Exception {
        final InputStream rs1 = ResourceUtil
                .loadResourceAsStream("au/gov/geoscience/portal/vocab/CommodityCode_Concepts_MoreData.xml");
        final InputStream rs2 = ResourceUtil
                .loadResourceAsStream("au/gov/geoscience/portal/vocab/CommodityCode_Concepts_NoMoreData.xml");

        context.checking(new Expectations() {
            {

                oneOf(mockMethodMaker).getAllConcepts(baseUrl, CommodityVocabService.REPOSITORY_NAME, Format.Rdf,
                        View.concept, commodityVocabService.getPageSize(), 0);
                will(returnValue(mockMethod));
                oneOf(mockMethodMaker).getAllConcepts(baseUrl, CommodityVocabService.REPOSITORY_NAME, Format.Rdf,
                        View.concept, commodityVocabService.getPageSize(), 1);
                will(returnValue(mockMethod2));

                oneOf(mockServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(rs1, null)));
                oneOf(mockServiceCaller).getMethodResponseAsStream(mockMethod2);
                will(returnValue(new HttpClientInputStream(rs2, null)));

                oneOf(mockMethod).releaseConnection();
                oneOf(mockMethod2).releaseConnection();

            }
        });

        Map<String, String> result = commodityVocabService.getAllCommodityConcepts();
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());

        Assert.assertEquals("gold", result.get("http://resource.geosciml.org/classifier/cgi/commodity-code/gold"));
        Assert.assertEquals("uranium",
                result.get("http://resource.geosciml.org/classifier/cgi/commodity-code/uranium"));
    }
}
