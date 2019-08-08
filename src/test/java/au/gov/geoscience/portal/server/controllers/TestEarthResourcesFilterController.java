package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.services.EarthResourceService;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;

/**
 * @author san218
 * @version $Id$
 */
public class TestEarthResourcesFilterController extends PortalTestClass {
    private EarthResourceFilterController earthResourcesFilterController;
    private EarthResourceService earthResourceService;
    private NamespaceService namespaceService;

    @Before
    public void setUp() {
        this.earthResourceService = context.mock(EarthResourceService.class);
        this.earthResourcesFilterController = new EarthResourceFilterController(this.earthResourceService, this.namespaceService);
    }

    private void testMAVResponse(ModelAndView mav, Boolean success, String gml, String kml) {

        ModelMap model = mav.getModelMap();
        Assert.assertEquals(success.booleanValue(), model.get("success"));

        if (success) {
            Object dataObj = model.get("data");
            Assert.assertNotNull(dataObj);
            if (dataObj instanceof ModelMap) {
                if (gml != null) {
                    ModelMap data = ((ModelMap) dataObj);
                    Assert.assertNotNull(data);
                    Assert.assertEquals(gml, data.get("gml"));
                }
                if (gml != null) {
                    ModelMap data = ((ModelMap) dataObj);
                    Assert.assertNotNull(data);
                    Assert.assertEquals(gml, data.get("kml"));
                }
            }
        }

    }

    private void testMAVResponseCount(ModelAndView mav, Boolean success, Integer count) {
        ModelMap model = mav.getModelMap();

        if (success != null) {
            Assert.assertEquals(success.booleanValue(), model.get("success"));
        }

        if (count != null) {
            Integer data = (Integer) model.get("data");
            Assert.assertNotNull(data);
            Assert.assertEquals(count, data);
        }
    }





    /**
     * Tests using the mine count service
     * 
     * @throws Exception
     */
    @Test
    public void testMineCount() throws Exception {
        final String serviceUrl = "http://localhost?";
        final String mineName = "mineName"; //to get all mines
        final int maxFeatures = 21341;
        final int responseCount = 21;

        context.checking(new Expectations() {
            {
                oneOf(earthResourceService).getMineCount(serviceUrl, mineName, null, null, maxFeatures);
                will(returnValue(new WFSCountResponse(responseCount)));
            }
        });

        ModelAndView modelAndView = this.earthResourcesFilterController.mineFilterCount(serviceUrl, mineName, null, null,
                maxFeatures);
        testMAVResponseCount(modelAndView, new Boolean(true), responseCount);
    }

    /**
     * Tests using the mineral occurrence count service
     * 
     * @throws Exception
     */
    @Test
    public void testMineralOccurrenceCount() throws Exception {
        final String serviceUrl = "http://localhost?";
        final int maxFeatures = 21341;
        final int responseCount = 21;
        final String occurrenceName = "mineName";


        context.checking(new Expectations() {
            {
                oneOf(earthResourceService).getMinOccViewCount(serviceUrl, occurrenceName, null, maxFeatures);
                will(returnValue(new WFSCountResponse(responseCount)));
            }
        });

        ModelAndView modelAndView = this.earthResourcesFilterController.minOccViewFilterCount(serviceUrl, occurrenceName, null, maxFeatures);
        testMAVResponseCount(modelAndView, new Boolean(true), responseCount);
    }

    /**
     * Tests using the mine activity count service
     * 
     * @throws Exception
     */
    @Test
    public void testMineActivityCount() throws Exception {
        final String serviceUrl = "http://localhost?";
        final int maxFeatures = 21341;
        final int responseCount = 21;
        final String mineName = "mineName"; //to get all mines
        final String startDate = "2010-01-01";
        final String endDate = "2011-01-01";
        final String oreProcessed = "3";
        final String producedMaterial = "pm";
        final String cutOffGrade = "55";
        final String production = "prod";
        context.checking(new Expectations() {
            {
                oneOf(earthResourceService).getMiningActivityCount(serviceUrl, mineName, null, maxFeatures);
                will(returnValue(new WFSCountResponse(responseCount)));
            }
        });

        ModelAndView modelAndView = this.earthResourcesFilterController.miningActivityFilterCount(serviceUrl,
                mineName, null, maxFeatures);
        testMAVResponseCount(modelAndView, new Boolean(true), responseCount);
    }
}
