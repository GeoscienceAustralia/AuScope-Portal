package au.gov.geoscience.portal.server.controllers;
import org.auscope.portal.core.test.PortalTestClass;
import org.junit.Before;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;

import au.gov.geoscience.portal.server.services.PetroleumTenementService;
import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.filters.PetroleumTenementFilter;
import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import javax.servlet.http.HttpServletResponse;
import org.auscope.portal.core.test.jmock.ReadableServletOutputStream;
import org.auscope.portal.core.test.ResourceUtil;

import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;


public class TestPetroleumTenementController extends PortalTestClass {
    private PetroleumTenementController petroleumTenementController;
    private WMSService mockWmsService;
    private WfsToCsvTransformer mockCsvTransformer;
    private HttpServletResponse response;
    private PetroleumTenementService mockPetroleumTenementService;

    @Before
    public void setUp() {
        this.mockWmsService = context.mock(WMSService.class);
        this.mockPetroleumTenementService = context.mock(PetroleumTenementService.class);
        this.mockCsvTransformer = context.mock(WfsToCsvTransformer.class);
        this.petroleumTenementController = new PetroleumTenementController(mockWmsService, mockPetroleumTenementService, mockCsvTransformer);
        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testPetroleumTenementFilterStyle() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";
        final String typeUri = null;
        final String statusUri = null;
        final String endDate = null;


        final String filterString = new PetroleumTenementFilter(name, owner, null, null, PetroleumTenementServiceProviderType.GeoServer).getFilterStringAllRecords();

        final ReadableServletOutputStream os = new ReadableServletOutputStream();

        String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/petroleumTenementTest.sld");

        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getPetroleumTenementFilter(name, typeUri, owner, statusUri, endDate, null, PetroleumTenementServiceProviderType.GeoServer);
                will(returnValue(filterString));
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        petroleumTenementController.petroleumTenementFilterStyle(mockServiceUrl, name, typeUri, owner, statusUri, endDate, response);

        Assert.assertTrue(xmlStringEquals(mockSld, new String(os.getDataWritten()), true, true));
    }

    @Test
    public void testPetroleumTenementFilterCount() throws Exception {

        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";

        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl,  name, owner, 0, null);
                will(returnValue(new WFSCountResponse(10)));
            }
        });

        ModelAndView mav = petroleumTenementController.getPetroleumTenementCount(mockServiceUrl, name, owner, null, 0);


        Assert.assertTrue((Boolean) mav.getModel().get("success"));
        Assert.assertEquals(10, mav.getModel().get("data"));

    }

    @Test
    public void testFailedPetroleumTenementFilterCount() throws Exception {

        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";

        context.checking(new Expectations() {
            {
                oneOf(mockPetroleumTenementService).getTenementCount(mockServiceUrl,  name, owner, 0, null);
                will(throwException(new PortalServiceException("Mock Exception")));
            }
        });

        ModelAndView mav = petroleumTenementController.getPetroleumTenementCount(mockServiceUrl, name, owner, null, 0);


        Assert.assertFalse((Boolean) mav.getModel().get("success"));

    }
}
