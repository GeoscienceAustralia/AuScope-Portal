package au.gov.geoscience.portal.server.services;

import java.io.InputStream;

import org.auscope.portal.core.server.http.HttpClientInputStream;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for GetCapabilitiesService
 *
 * @author Josh Vote
 *
 */
public class TestSeismicSurveyService extends PortalTestClass {

    private SeismicSurveyService service;
    private HttpServiceCaller mockServiceCaller;



    @Before
    public void setup() throws Exception {
        mockServiceCaller = context.mock(HttpServiceCaller.class);

        service = new SeismicSurveyService(mockServiceCaller);
        
    }

    /**
     * Test that the function is able to actually load CSW records from multiple services
     *
     * @throws Exception
     */
    @Test
    public void testGetCSWRecord() throws Exception {
        final InputStream is1 = ResourceUtil
                .loadResourceAsStream("au/gov/geoscience/portal/server/services/SeismicSurvey.xml");
        final String mockRecordNumber = "1111";
        final String mockUrl = "http://www.example.org";

        context.checking(new Expectations() {
            {

            	oneOf(mockServiceCaller).getMethodResponseAsStream(with(aHttpMethodBase(null, mockUrl, null)));
            	will(returnValue(new HttpClientInputStream(is1, null)));
               
            }
        });

        CSWRecord record = service.getCSWRecord(mockRecordNumber, mockUrl);
        Assert.assertEquals("Canberra", record.getContact().getContactInfo().getAddressCity());
        Assert.assertEquals("ACT", record.getContact().getContactInfo().getAddressAdministrativeArea());
        Assert.assertEquals("ce415703-fb0a-1a1c-e044-00144fdd4fa6", record.getFileIdentifier());
        Assert.assertEquals(1, record.getOnlineResources().length);

    }

}
