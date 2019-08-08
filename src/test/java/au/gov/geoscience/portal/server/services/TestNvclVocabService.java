package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.methodmaker.NvclVocabMethodMaker;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.jena.rdf.model.Model;
import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.Format;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class TestNvclVocabService extends PortalTestClass {
    private HttpRequestBase mockMethod = context.mock(HttpRequestBase.class, "mockMethod");
    private HttpRequestBase mockMethod2 = context.mock(HttpRequestBase.class, "mockMethod2");
    private HttpServiceCaller mockServiceCaller = context.mock(HttpServiceCaller.class);
    private NvclVocabMethodMaker mockMethodMaker = context.mock(NvclVocabMethodMaker.class);
    private String baseUrl = "http://example.org:8080/sissvoc/path";

    private NvclVocabService service;

    @Before
    public void setup() {
        service = new NvclVocabService(mockServiceCaller, mockMethodMaker, baseUrl);
        service.setPageSize(50);
    }

    /**
     * Tests that iterating a repository works as expected - returning all scalars
     *
     * @throws Exception
     */
    @Test
    public void testGetScalars() throws Exception {
        final InputStream rs1 = ResourceUtil
                .loadResourceAsStream("org/auscope/portal/erml/vocab/NVCL_Concepts_MoreData.xml");
        final InputStream rs2 = ResourceUtil
                .loadResourceAsStream("org/auscope/portal/erml/vocab/NVCL_Concepts_NoMoreData.xml");

        context.checking(new Expectations() {
            {
                oneOf(mockMethodMaker).getAllScalars(baseUrl, NvclVocabService.NVCL_VOCAB_REPOSITORY, Format.Rdf,
                        service.getPageSize(), 0);
                will(returnValue(mockMethod));
                oneOf(mockMethodMaker).getAllScalars(baseUrl, NvclVocabService.NVCL_VOCAB_REPOSITORY, Format.Rdf,
                        service.getPageSize(), 1);
                will(returnValue(mockMethod2));

                oneOf(mockServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(rs1, null)));
                oneOf(mockServiceCaller).getMethodResponseAsStream(mockMethod2);
                will(returnValue(new HttpClientInputStream(rs2, null)));

                oneOf(mockMethod).releaseConnection();
                oneOf(mockMethod2).releaseConnection();
            }
        });

        Model result = service.getAllScalarConcepts();
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
    }

    /**
     * Tests that iterating a repository by label works as expected - returning only a single term
     *
     * @throws Exception
     */
    @Test
    public void testGetScalarsByLabel() throws Exception {
        final String label = "TSA_S_Mineral2";
        final InputStream rs1 = ResourceUtil
                .loadResourceAsStream("org/auscope/portal/erml/vocab/NVCL_Concepts_NoMoreData.xml");

        context.checking(new Expectations() {
            {
                oneOf(mockMethodMaker).getScalarsByLabel(baseUrl, NvclVocabService.NVCL_VOCAB_REPOSITORY, label,
                        Format.Rdf, service.getPageSize(), 0);
                will(returnValue(mockMethod));

                oneOf(mockServiceCaller).getMethodResponseAsStream(mockMethod);
                will(returnValue(new HttpClientInputStream(rs1, null)));

                oneOf(mockMethod).releaseConnection();
            }
        });

        List<String> defns = service.getScalarDefinitionsByLabel(label);
        Assert.assertNotNull(defns);
        Assert.assertEquals(1, defns.size());
        Assert.assertEquals("definition 2", defns.get(0));
    }
}
