package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.MineralTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.MineralTenementService;
import au.gov.geoscience.portal.server.services.filters.MineralTenementFilter;
import au.gov.geoscience.portal.xslt.ArcGISToMineralTenement;
import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import org.apache.commons.io.IOUtils;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.test.ByteBufferedServletOutputStream;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.test.jmock.ReadableServletOutputStream;
import org.auscope.portal.core.util.FileIOUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TestMineralTenementController extends PortalTestClass {


    private MineralTenementController mineralTenementController;

    private WMSService mockWmsService;
    private ArcGISToMineralTenement mockArcGISToMineralTenementTransformer;
    private WfsToCsvTransformer mockCsvTransformer;
    private MineralTenementService mockMineralTenementService;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.mockWmsService = context.mock(WMSService.class);
        this.mockMineralTenementService = context.mock(MineralTenementService.class);
        this.mockArcGISToMineralTenementTransformer = context.mock(ArcGISToMineralTenement.class);
        this.mockCsvTransformer = context.mock(WfsToCsvTransformer.class);
        this.mineralTenementController = new MineralTenementController(mockWmsService, mockMineralTenementService, mockArcGISToMineralTenementTransformer, mockCsvTransformer);

        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testMineralTenementFilterStyle() throws Exception {
        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";
        final String typeUri = null;
        final String statusUri = null;
        final String endDate = null;


        final String filterString = new MineralTenementFilter(name, owner, null, null, MineralTenementServiceProviderType.GeoServer).getFilterStringAllRecords();

        final ReadableServletOutputStream os = new ReadableServletOutputStream();

        String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/mineralTenementTest.sld");

        context.checking(new Expectations() {
            {
                oneOf(mockMineralTenementService).getMineralTenementFilter(name, typeUri, owner, statusUri, endDate, null, MineralTenementServiceProviderType.GeoServer);
                will(returnValue(filterString));
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        mineralTenementController.mineralTenementFilterStyle(mockServiceUrl, name, typeUri, owner, statusUri, endDate, response);

        Assert.assertTrue(xmlStringEquals(mockSld, new String(os.getDataWritten()), true, true));
    }

    @Test
    public void testMineralTenementFilterCount() throws Exception {

        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";

        context.checking(new Expectations() {
            {
                oneOf(mockMineralTenementService).getTenementCount(mockServiceUrl,  name, owner, 0, null);
                will(returnValue(new WFSCountResponse(10)));
            }
        });

        ModelAndView mav = mineralTenementController.getMineralTenementCount(mockServiceUrl, name, owner, null, 0);


        Assert.assertTrue((Boolean) mav.getModel().get("success"));
        Assert.assertEquals(10, mav.getModel().get("data"));

    }

    @Test
    public void testFailedMineralTenementFilterCount() throws Exception {

        final String mockServiceUrl = "http://portal.ga/wms";
        final String name = "Tenement";
        final String owner = "BHPBilliton Limited";

        context.checking(new Expectations() {
            {
                oneOf(mockMineralTenementService).getTenementCount(mockServiceUrl,  name, owner, 0, null);
                will(throwException(new PortalServiceException("Mock Exception")));
            }
        });

        ModelAndView mav = mineralTenementController.getMineralTenementCount(mockServiceUrl, name, owner, null, 0);


        Assert.assertFalse((Boolean) mav.getModel().get("success"));

    }
}