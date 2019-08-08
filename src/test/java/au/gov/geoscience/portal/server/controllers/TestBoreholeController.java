package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.services.BoreholeViewService;
import au.gov.geoscience.portal.server.services.NVCL2_0_DataService;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.NamespaceService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.core.test.PortalTestClass;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * The Class TestNVCLController.
 * 
 * @version: $Id$
 */
@SuppressWarnings("rawtypes")
public class TestBoreholeController extends PortalTestClass {

    /** The mock portrayal borehole view service. */
    private BoreholeViewService mockBoreholeViewService;

    /** The portrayal borehole view controller. */
    private BoreholeController boreholeController;

    private NVCL2_0_DataService mockNvclDataService;

    /** The mock csw service. */
    private CSWCacheService mockCSWService;

    private NamespaceService mockNamespaceService;

    /**
     * Setup.
     */
    @Before
    public void setUp() {
        context.mock(HttpServletResponse.class);
        this.mockBoreholeViewService = context.mock(BoreholeViewService.class);
        this.mockCSWService = context.mock(CSWCacheService.class);
        this.mockNamespaceService = context.mock(NamespaceService.class);
        this.mockNvclDataService = context.mock(NVCL2_0_DataService.class);
        this.boreholeController = new BoreholeController(this.mockBoreholeViewService, this.mockCSWService, this.mockNvclDataService, this.mockNamespaceService);
    }

    @Test
    public void testNationalVirtualCoreLibraryFilterCount() throws PortalServiceException, URISyntaxException {
        String serviceUrl = "http://portal.ga/wfs";
        String boreholeName = "DDH1";
        String dateOfDrilling = "2014";
        Boolean nvclCollection = true;
        String bboxJson = null;
        int maxFeatures = 0;


        context.checking(new Expectations() {
            {
                oneOf(mockBoreholeViewService).getNVCLCount(serviceUrl, boreholeName, dateOfDrilling, nvclCollection, null, maxFeatures);
            }
        });

        boreholeController.nationalVirtualCoreLibraryFilterCount(serviceUrl, boreholeName, dateOfDrilling, nvclCollection, bboxJson, maxFeatures);

    }

    /**
     * Tests to ensure that a non hylogger request calls the correct functions.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testNonHyloggerFilter() throws Exception {
        final String serviceUrl = "http://fake.com/wfs";
        final String nameFilter = "filterBob";
        final String custodianFilter = "filterCustodian";
        final String filterDate = "1986-10-09";
        final int maxFeatures = 10;
        final String sf0BoreholeWfsResponse = "wfsResponse";
        final HttpRequestBase mockHttpMethodBase = context.mock(HttpRequestBase.class);
        final URI httpMethodURI = new URI("http://example.com");
        final String outputFormat = "text/csv";
        
        context.checking(new Expectations() {
            {
                oneOf(mockBoreholeViewService).getAllBoreholes(serviceUrl, nameFilter, custodianFilter, filterDate,
                        maxFeatures, null, outputFormat);
                will(returnValue(new WFSResponse(sf0BoreholeWfsResponse, mockHttpMethodBase)));

                allowing(mockHttpMethodBase).getURI();
                will(returnValue(httpMethodURI));

            }
        });

        ModelAndView response = this.boreholeController.doBoreholeFilter(serviceUrl, nameFilter, custodianFilter,
                filterDate, maxFeatures, null, outputFormat);
        Assert.assertTrue((Boolean) response.getModel().get("success"));

        Map data = (Map) response.getModel().get("data");
        Assert.assertNotNull(data);
        Assert.assertEquals(sf0BoreholeWfsResponse, data.get("gml"));
    }


}
