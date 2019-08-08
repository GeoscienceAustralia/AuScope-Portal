package au.gov.geoscience.portal.server.services.methodmaker;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.Format;
import org.auscope.portal.core.test.PortalTestClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;

public class TestNvclVocabMethodMaker extends PortalTestClass {

    private NvclVocabMethodMaker methodMaker;

    @Before
    public void setUp() {
        methodMaker = new NvclVocabMethodMaker();
    }


    @Test
    public void testGetAllScalars() throws URISyntaxException {

        String url = "http://vocabs.ga/";

        String repository = "nvcl";
        Format format = Format.Html;
        int page = 12;
        int pageSize = 10;
        HttpRequestBase method = methodMaker.getAllScalars(url, repository, format, pageSize, page);

        String queryString = method.getURI().getQuery();
        String path = method.getURI().getPath();

        Assert.assertEquals(String.format("/%1$s/scalar.html", repository), path);
        Assert.assertTrue(queryString.contains(String.format("_page=%1$s", page)));
        Assert.assertTrue(queryString.contains(String.format("_pageSize=%1$s", pageSize)));
    }

    @Test
    public void testGetAllScalarsByLabel() throws URISyntaxException {
        String url = "http://vocabs.ga/";

        String repository = "nvcl";
        Format format = Format.Html;
        int page = 12;
        int pageSize = 10;
        String label = "label";
        HttpRequestBase method = methodMaker.getScalarsByLabel(url, repository, label, format, pageSize, page);

        String queryString = method.getURI().getQuery();
        String path = method.getURI().getPath();

        Assert.assertEquals(String.format("/%1$s/scalar.html", repository), path);
        Assert.assertTrue(queryString.contains(String.format("_page=%1$s", page)));
        Assert.assertTrue(queryString.contains(String.format("_pageSize=%1$s", pageSize)));
        Assert.assertTrue(queryString.contains(String.format("anylabel=%1$s", label)));
    }
}