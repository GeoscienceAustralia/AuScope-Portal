package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.test.jmock.ReadableServletOutputStream;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

public class TestSLDController extends PortalTestClass {

    private HttpServletResponse response;

    private SLDController sldController;

    @Before
    public void setUp() {
        this.response = context.mock(HttpServletResponse.class);

        this.sldController = new SLDController();
    }

    @Test
    public void testScannedGeologicalMapsStyle() throws Exception {
        final String mapName = "ADELAIDE";
        final String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/controllers/scannedMapsAdelaideTest.sld");
        final ReadableServletOutputStream os = new ReadableServletOutputStream();

        context.checking(new Expectations(){
            {
                allowing(response).setContentType(with(any(String.class)));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        sldController.getScannedGeologicalMapsStyle(mapName, this.response);

        Assert.assertTrue(xmlStringEquals(mockSld, new String(os.getDataWritten()), true, true));
    }

    @Test
    public void testEmptyScannedGeologicalMapsStyle() throws Exception {
        final ReadableServletOutputStream os = new ReadableServletOutputStream();
        context.checking(new Expectations(){
            {
                allowing(response).setContentType(with(any(String.class)));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        sldController.getScannedGeologicalMapsStyle(null, this.response);
        Assert.assertEquals(new String(), new String(os.getDataWritten()));
    }


    @Test
    public void testOilStyle() throws Exception {
        final String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/sld/oilpipelines.sld");
        final ReadableServletOutputStream os = new ReadableServletOutputStream();

        context.checking(new Expectations(){
            {
                allowing(response).setContentType(with(any(String.class)));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        sldController.getOilStyle(this.response);

        Assert.assertTrue(xmlStringEquals(mockSld, new String(os.getDataWritten()), true));
    }

    @Test
    public void testGasStyle() throws Exception {
        final String mockSld = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/sld/gaspipelines.sld");
        final ReadableServletOutputStream os = new ReadableServletOutputStream();

        context.checking(new Expectations(){
            {
                allowing(response).setContentType(with(any(String.class)));
                oneOf(response).getOutputStream();
                will(returnValue(os));
            }
        });

        sldController.getGasStyle(this.response);

        Assert.assertTrue(xmlStringEquals(mockSld, new String(os.getDataWritten()), true));
    }
}