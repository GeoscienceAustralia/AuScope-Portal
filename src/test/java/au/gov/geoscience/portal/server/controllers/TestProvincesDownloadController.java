package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.services.WFSService;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestProvincesDownloadController extends PortalTestClass {

    private ProvincesDownloadController provincesDownloadController;

    private WFSService wfsService;
    private WfsToCsvTransformer wfsToCsvTransformer;

    @Before
    public void setUp() throws Exception {

        this.wfsService = context.mock(WFSService.class);
        this.wfsToCsvTransformer = context.mock(WfsToCsvTransformer.class);

        this.provincesDownloadController = new ProvincesDownloadController(wfsService, wfsToCsvTransformer);
    }

    @Test
    public void testGeologicalProvincesDownload() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wfs";
        final String mockType = "AllProvinces";

        final HttpRequestBase mockHttpMethodBase = context.mock(HttpRequestBase.class);
        final WFSResponse mockWFSResponse = new WFSResponse("WFS Response", mockHttpMethodBase);
        final String csvResponse = "1,2,3,4";

        context.checking(new Expectations() {
            {
                oneOf(wfsService).getWfsResponse(mockServiceUrl, mockType, "", 20, null);
                will(returnValue(mockWFSResponse));
                allowing(mockHttpMethodBase).getURI();
                oneOf(wfsToCsvTransformer).convert(mockWFSResponse.getData(), mockServiceUrl);
                will(returnValue(csvResponse));
            }
        });

        provincesDownloadController.geologicalProvincesDownload(mockServiceUrl, mockType, "", 20, "csv");

    }
}