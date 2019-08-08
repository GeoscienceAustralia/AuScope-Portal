package au.gov.geoscience.portal.server.services.methodmaker;

import org.junit.Assert;
import org.auscope.portal.core.test.PortalTestClass;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * Unit tests for the NVCLDataServiceMethodMaker
 *
 * Tests according to the specification at https://twiki.auscope.org/wiki/CoreLibrary/WebServicesDevelopment
 *
 * @author Josh Vote
 *
 */
public class TestNVCL2_0_DataServiceMethodMaker extends PortalTestClass {

    final String serviceUrl = "https://test.url/NVCL-data-service";
    final String holeIdentifier = "hole-identifier";
    final String logIdentifier = "log-identifier";
    final String datasetId = "dataset-id";
    final String email = "user@email-notadomain.com";
    private NVCL2_0_DataServiceMethodMaker methodMaker;

    @Before
    public void setup() {
        methodMaker = new NVCL2_0_DataServiceMethodMaker();
    }

    private void assertContainsURLParam(URI uri, String paramName, String paramValue) throws Exception {
        Assert.assertTrue(uri.getQuery().contains(String.format("%1$s=%2$s", paramName, paramValue)));
    }

    private void assertDoesntContainURLParam(URI uri, String paramName) throws Exception {
        Assert.assertFalse(uri.getQuery().contains(paramName));
    }

    @Test
    public void testParamValidity_GetLogCollection() throws Exception {
        //Mandatory only
        URI uri = methodMaker.getLogCollectionMethod(serviceUrl, datasetId, null).getURI();
        assertContainsURLParam(uri, "datasetid", datasetId);
        assertDoesntContainURLParam(uri, "mosaicsvc");

        //Optional Params
        uri = methodMaker.getLogCollectionMethod(serviceUrl, datasetId, true).getURI();
        assertContainsURLParam(uri, "datasetid", datasetId);
        assertContainsURLParam(uri, "mosaicsvc", "yes");

        uri = methodMaker.getLogCollectionMethod(serviceUrl, datasetId, false).getURI();
        assertContainsURLParam(uri, "datasetid", datasetId);
        assertContainsURLParam(uri, "mosaicsvc", "no");
    }

    @Test
    public void testParamValidity_getTrayThumbNailMethodMaker() throws Exception {
        //Mandatory only
        URI uri = methodMaker.getTrayThumbNailMethodMaker(datasetId, serviceUrl, logIdentifier, null, null, null)
                .getURI();
        assertContainsURLParam(uri, "datasetid", datasetId);
        assertContainsURLParam(uri, "logid", logIdentifier);
        assertDoesntContainURLParam(uri, "width");
        assertDoesntContainURLParam(uri, "startsampleno");
        assertDoesntContainURLParam(uri, "endsampleno");

    }

    /**
     * Ensure we don't allow a download request with no ID/filter
     * 
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testGetDownloadCSVMethod() throws Exception {
        String[] logids = {"id1", "id2"};
        methodMaker.getDownloadCSVMethod(null, logids);
    }

}
