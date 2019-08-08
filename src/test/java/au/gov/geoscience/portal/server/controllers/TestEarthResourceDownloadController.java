package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.services.EarthResourceDownloadService;
import au.gov.geoscience.portal.server.services.EarthResourceService;
import au.gov.geoscience.portal.server.services.filters.CommodityResourceViewFilter;
import au.gov.geoscience.portal.server.services.filters.MinOccViewFilter;
import au.gov.geoscience.portal.server.services.filters.MineViewFilter;
import au.gov.geoscience.portal.server.services.filters.MineralOccurrenceViewFilter;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.test.ByteBufferedServletOutputStream;
import org.auscope.portal.core.test.PortalTestClass;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TestEarthResourceDownloadController extends PortalTestClass {

    private EarthResourceDownloadController earthResourceDownloadController;
    private EarthResourceService mockEarthResourceService;
    private EarthResourceDownloadService mockEarthResourceDownloadService;

    private HttpServletResponse response;

    final class MyServletOutputStream extends ByteBufferedServletOutputStream {
        public MyServletOutputStream(int length) {
            super(length);
        }

        public String getInputString() {
            return this.getStream().toString();
        }
    }

    @Before
    public void setUp() {
        this.mockEarthResourceService = context.mock(EarthResourceService.class);
        this.mockEarthResourceDownloadService = context.mock(EarthResourceDownloadService.class);
        this.earthResourceDownloadController = new EarthResourceDownloadController(mockEarthResourceService, mockEarthResourceDownloadService);

        this.response = context.mock(HttpServletResponse.class);
    }

    @Test
    public void testMinOccViewFilterDownload() throws Exception {
        final String resultXML = "<result>this is a test</result>";

        final ByteArrayInputStream ins = new ByteArrayInputStream(resultXML.getBytes());
        final MyServletOutputStream out = new MyServletOutputStream(resultXML.length());
        final String mockServiceUrl = "http://mock.url/";
        final String mockName = "Mt Isa";
        final String mockFilterString = new MinOccViewFilter(mockName).getFilterStringAllRecords();


        context.checking(new Expectations(){
            {
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(out));
                oneOf(mockEarthResourceService).getMinOccViewFilter(mockName, null);
                will(returnValue(mockFilterString));
                oneOf(mockEarthResourceDownloadService).downloadMinOccView(mockServiceUrl, mockFilterString, 0, null);
                will(returnValue(ins));
            }
        });

        earthResourceDownloadController.minOccViewFilterDownload(mockServiceUrl, mockName, null, null, 0, response);
        Assert.assertTrue(out.getInputString().equals(resultXML));

    }

    @Test
    public void testDownloadMineralOccurrenceView() throws Exception {
        final String resultXML = "<result>this is a test</result>";

        final ByteArrayInputStream ins = new ByteArrayInputStream(resultXML.getBytes());
        final MyServletOutputStream out = new MyServletOutputStream(resultXML.length());
        final String mockServiceUrl = "http://mock.url/";
        final String mockName = "Mt Isa";
        final String mockCommodityUri = "http://vocabs.ga/commodity/gold";
        final String mockTimescaleUri = "http://vocabs.ga/timescale/archean";
        final String mockFilterString = new MineralOccurrenceViewFilter(mockName, mockCommodityUri, mockTimescaleUri).getFilterStringAllRecords();


        context.checking(new Expectations(){
            {
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(out));
                oneOf(mockEarthResourceService).getMineralOccurrenceViewFilter(mockName, mockCommodityUri, mockTimescaleUri, null);
                will(returnValue(mockFilterString));
                oneOf(mockEarthResourceDownloadService).downloadMineralOccurrenceView(mockServiceUrl, mockFilterString, 0, null);
                will(returnValue(ins));
            }
        });

        earthResourceDownloadController.mineralOccurrenceViewDownload(mockServiceUrl, mockName, mockCommodityUri, mockTimescaleUri, null, null,0, response);
        Assert.assertTrue(out.getInputString().equals(resultXML));
    }

    @Test
    public void testDownloadMineView() throws IOException, PortalServiceException, URISyntaxException {

        final String resultXML = "<result>this is a test</result>";

        final ByteArrayInputStream ins = new ByteArrayInputStream(resultXML.getBytes());
        final MyServletOutputStream out = new MyServletOutputStream(resultXML.length());
        final String mockServiceUrl = "http://mock.url/";
        final String mockName = "Mt Isa";
        final String mockStatusUri = "http://vocabs.ga/mine-status/gold";
        final String mockFilterString = new MineViewFilter(mockName, mockStatusUri).getFilterStringAllRecords();


        context.checking(new Expectations(){
            {
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(out));
                oneOf(mockEarthResourceService).getMineViewFilter(mockName, mockStatusUri, null);
                will(returnValue(mockFilterString));
                oneOf(mockEarthResourceDownloadService).downloadMineView(mockServiceUrl, mockFilterString, 0, null);
                will(returnValue(ins));
            }
        });

        earthResourceDownloadController.mineViewDownload(mockServiceUrl, mockName, mockStatusUri,null, null,0, response);
        Assert.assertTrue(out.getInputString().equals(resultXML));
    }

    @Test
    public void testDownloadCommodityResourceView() throws PortalServiceException, IOException, URISyntaxException {
        final String resultXML = "<result>this is a test</result>";

        final ByteArrayInputStream ins = new ByteArrayInputStream(resultXML.getBytes());
        final MyServletOutputStream out = new MyServletOutputStream(resultXML.length());
        final String mockServiceUrl = "http://mock.url/";
        final String mockName = "Mt Isa";
        final String mockCommodityUri = "http://vocabs.ga/commodity/gold";
        final String mockCategoryUri = "http://vocabs.ga/category/proved-reserves";
        final String mockFilterString = new CommodityResourceViewFilter(mockName, mockCommodityUri, mockCategoryUri).getFilterStringAllRecords();


        context.checking(new Expectations(){
            {
                allowing(response).setContentType((with(any(String.class))));
                oneOf(response).getOutputStream();
                will(returnValue(out));
                oneOf(mockEarthResourceService).getCommodityResourceViewFilter(mockName, mockCommodityUri, mockCategoryUri, null);
                will(returnValue(mockFilterString));
                oneOf(mockEarthResourceDownloadService).downloadCommodityResourceView(mockServiceUrl, mockFilterString, 0, null);
                will(returnValue(ins));
            }
        });

        earthResourceDownloadController.commodityResourceViewDownload(mockServiceUrl, mockName, mockCommodityUri, mockCategoryUri, null,null,0, response);
        Assert.assertTrue(out.getInputString().equals(resultXML));
    }
}