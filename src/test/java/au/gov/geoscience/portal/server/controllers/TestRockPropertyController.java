package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.services.RockPropertyService;
import org.apache.commons.io.IOUtils;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.test.jmock.ReadableServletOutputStream;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public class TestRockPropertyController extends PortalTestClass {

    private RockPropertyController rockPropertyController;

    private RockPropertyService mockRockPropertyService;
    private WMSService mockWMSService;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.mockRockPropertyService = context.mock(RockPropertyService.class);
        this.mockWMSService = context.mock(WMSService.class);
        this.rockPropertyController = new RockPropertyController(mockRockPropertyService, mockWMSService);

        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testRockPropertyCount() throws Exception {

        String serviceUrl = "http://portal.ga/wfs";
        String typeName = "BulkDensity";
        int maxFeatures = 10;

        context.checking(new Expectations() {
            {

                oneOf(mockRockPropertyService).getRockPropertyCount(serviceUrl, typeName, maxFeatures, null);
                will(returnValue(new WFSCountResponse(maxFeatures)));
            }
        });

        ModelAndView mav = rockPropertyController.getRockPropertyCount(serviceUrl, typeName, null, maxFeatures);


        Assert.assertTrue((Boolean) mav.getModel().get("success"));
        Assert.assertEquals(maxFeatures, mav.getModel().get("data"));


    }


    @Test
    public void testFailedRockPropertyCount() throws Exception {

        String serviceUrl = "http://portal.ga/wfs";
        String typeName = "BulkDensity";
        int maxFeatures = 10;

        context.checking(new Expectations() {
            {

                oneOf(mockRockPropertyService).getRockPropertyCount(serviceUrl, typeName, maxFeatures, null);
                will(throwException(new Exception()));
            }
        });

        ModelAndView mav = rockPropertyController.getRockPropertyCount(serviceUrl, typeName, null, maxFeatures);

        Assert.assertFalse((Boolean) mav.getModel().get("success"));

    }

    @Test
    public void testRockPropertiesDownload() throws Exception {

        final String mockCsv = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/rockPropertiesBulkDensity.csv");

        final InputStream inputStream = IOUtils.toInputStream(mockCsv);

        final String serviceUrl = "http://portal.ga/wfs";
        final String typeName = "BulkDensity";
        final String outputFormat = "csv";

        final ReadableServletOutputStream out = new ReadableServletOutputStream();

        context.checking(new Expectations() {
            {
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(out));
                oneOf(mockRockPropertyService).downloadRockProperties(serviceUrl, typeName, null, outputFormat);
                will(returnValue(inputStream));
            }
        });

        rockPropertyController.getRockPropertiesDownload(serviceUrl, typeName, null, outputFormat, response);

        Assert.assertEquals(mockCsv, new String(out.getDataWritten()));

    }
}