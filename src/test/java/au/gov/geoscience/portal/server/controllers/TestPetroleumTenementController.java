package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.test.PortalTestClass;
import org.junit.After;
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

public class TestPetroleumTenementController extends PortalTestClass {
    private PetroleumTenementController petroleumTenementController;
    private HttpServletResponse response;
    private PetroleumTenementService mockPetroleumTenementService;

    @Before
    public void setUp() {
        this.mockPetroleumTenementService = context.mock(PetroleumTenementService.class);
        this.petroleumTenementController = new PetroleumTenementController(mockPetroleumTenementService);
        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testPetroleumTenementFilterStyle() throws Exception {
        final String name = "Tenement";
        final String holder = "BHPBilliton Limited";
        final String tenementTypeUri = null;
        final String statusUri = null;
        final String filterString = new PetroleumTenementFilter(name, holder, null, null).getFilterStringAllRecords();
        final ReadableServletOutputStream os = new ReadableServletOutputStream();
        String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/petroleumTenementTest.sld");
        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getPetroleumTenementFilter(name, holder, null, statusUri, tenementTypeUri);
                will(returnValue(filterString));
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });
        petroleumTenementController.petroleumTenementFilterStyle(name, holder, statusUri, tenementTypeUri, response);
        String writtenData = new String(os.getDataWritten());
        Assert.assertNotNull(writtenData);
        Assert.assertTrue(xmlStringEquals(mockSld, writtenData, true, true));
    }

    @Test
    public void testEscapeModulusOperators() {
        String modifiedHolder = petroleumTenementController.escapeModulusOperators("BHPBilliton Limited 100%");
        Assert.assertEquals("BHPBilliton Limited 100%%", modifiedHolder);
        modifiedHolder = petroleumTenementController.escapeModulusOperators("Holder - IMPERIAL OIL & GAS A PTY LI"
                + "MITED (100.0%), Manager - IMPERIAL OIL & GAS A PTY LIMITED");
        Assert.assertEquals("Holder - IMPERIAL OIL & GAS A PTY LI"
                + "MITED (100.0%%), Manager - IMPERIAL OIL & GAS A PTY LIMITED", modifiedHolder);
        modifiedHolder = petroleumTenementController.escapeModulusOperators("Holder - MINERALS AUSTRALIA PTY LTD"
                + "(50.0%), Holder - JACARANDA MINERALS PTY LTD (50.0%), Manager - MINERALS AUSTRALIA PTY LTD");
        Assert.assertEquals("Holder - MINERALS AUSTRALIA PTY LTD"
                + "(50.0%%), Holder - JACARANDA MINERALS PTY LTD (50.0%%), Manager - MINERALS AUSTRALIA PTY LTD", modifiedHolder);
    }

    @Test
    public void testPetroleumTenementFilterCount() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String holder = "BHPBilliton Limited";
        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl, name, holder, 0, null);
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
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl, name, holder, 0, null);
                will(throwException(new PortalServiceException("Mock Exception")));
            }
        });
        ModelAndView mav = petroleumTenementController.getPetroleumTenementCount(mockServiceUrl, name, holder, null, 0);
        Assert.assertFalse((Boolean) mav.getModel().get("success"));
    }
}
