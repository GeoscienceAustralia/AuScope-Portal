package au.gov.geoscience.portal.server.controllers;


import au.gov.geoscience.portal.server.services.SeismicSurveyService;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.view.ViewCSWRecordFactory;
import org.auscope.portal.core.view.ViewKnownLayerFactory;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;


public class TestSeismicSurveyController extends PortalTestClass {

    private SeismicSurveyController mockSeismicSurveyController;

    private SeismicSurveyService mockSeismicSurveyService;

    private ViewCSWRecordFactory mockViewCSWRecordFactory;
    private ViewKnownLayerFactory mockKnownLayerFactory;
    private HttpServiceCaller mockServiceCaller;

    @Before
    public void setUp() {

        this.mockSeismicSurveyService = context.mock(SeismicSurveyService.class);
        this.mockViewCSWRecordFactory = context.mock(ViewCSWRecordFactory.class);
        this.mockKnownLayerFactory = context.mock(ViewKnownLayerFactory.class);
        this.mockServiceCaller = context.mock(HttpServiceCaller.class);

        this.mockSeismicSurveyController = new SeismicSurveyController(this.mockSeismicSurveyService,
                this.mockViewCSWRecordFactory, this.mockKnownLayerFactory, this.mockServiceCaller);
    }

    @Test
    public void testGetSeismicCSWRecord() throws Exception {
        final String serviceUrl = "http://www.ga.gov.au/metadata-gateway/metadata/record/gcat_1234";
        final String eCatUrl = "https://ecat.ga.gov.au/geonetwork/srv/eng/csw";
        final String fileIdentifier = "file-id-abc123";

        final CSWRecord mockCSWRecord = new CSWRecord(fileIdentifier);

        final String recordNumber = "1234";

        context.checking(new Expectations() {
            {

                oneOf(mockSeismicSurveyService).getCSWRecord(recordNumber, eCatUrl);
                will(returnValue(mockCSWRecord));
                oneOf(mockViewCSWRecordFactory).toView(mockCSWRecord);

            }
        });

        ModelAndView response = mockSeismicSurveyController.getSeismicCSWRecord(serviceUrl);

    }
}