package au.gov.geoscience.portal.server.controllers;
import org.auscope.portal.core.test.PortalTestClass;
import org.junit.Before;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import au.gov.geoscience.portal.server.services.PetroleumTenementService;
import au.gov.geoscience.portal.server.services.filters.PetroleumTenementFilter;
import javax.servlet.http.HttpServletResponse;
import org.auscope.portal.core.test.jmock.ReadableServletOutputStream;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.auscope.portal.core.services.WMSService;
import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;

public class TestPetroleumTenementController extends PortalTestClass {
    private PetroleumTenementController petroleumTenementController;
    private HttpServletResponse response;
    private PetroleumTenementService mockPetroleumTenementService;
    private WMSService mockWmsService;
    private WfsToCsvTransformer mockCsvTransformer;
    @Before
    public void setUp() {
        this.mockWmsService = context.mock(WMSService.class);
        this.mockCsvTransformer = context.mock(WfsToCsvTransformer.class);
        this.mockPetroleumTenementService = context.mock(PetroleumTenementService.class);
        this.petroleumTenementController = new PetroleumTenementController(mockWmsService, mockCsvTransformer, mockPetroleumTenementService);
        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testPetroleumTenementFilterStyle() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String holder = "BHPBilliton Limited";
        final String tenementTypeUri = null;
        final String statusUri = null;
        final String applicationDate = null;
        final String filterString = new PetroleumTenementFilter(name, holder, null, null).getFilterStringAllRecords();
        final ReadableServletOutputStream os = new ReadableServletOutputStream();
        String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/petroleumTenementTest.sld");
        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getPetroleumTenementFilter(name, holder, null, statusUri, tenementTypeUri, applicationDate);
                will(returnValue(filterString));
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });
        petroleumTenementController.petroleumTenementFilterStyle(mockServiceUrl, name, holder, statusUri, tenementTypeUri, applicationDate, response);
        String writtenData = new String(os.getDataWritten());
        Assert.assertNotNull(writtenData);
        Assert.assertTrue(xmlStringEquals(mockSld, writtenData, true, true));
    }

    @Test
    public void testPetroleumTenementFilterCount() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String holder = "BHPBilliton Limited";
        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl,  name, holder, 0, null);
                will(returnValue(new WFSCountResponse(10)));
            }
        });
        ModelAndView mav = petroleumTenementController.getPetroleumTenementCount(mockServiceUrl, name, holder, null, 0);
        Assert.assertTrue((Boolean) mav.getModel().get("success"));
        Assert.assertEquals(10, mav.getModel().get("data"));
    }

    @Test
    public void testFailedPetroleumTenementFilterCount() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String holder = "BHPBilliton Limited";
        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl,  name, holder, 0, null);
                will(throwException(new PortalServiceException("Mock Exception")));
            }
        });
        ModelAndView mav = petroleumTenementController.getPetroleumTenementCount(mockServiceUrl, name, holder, null, 0);
        Assert.assertFalse((Boolean) mav.getModel().get("success"));
    }
}
