package au.gov.geoscience.portal.server.controllers;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.auscope.portal.core.server.http.download.DownloadResponse;
import org.auscope.portal.core.server.http.download.FileDownloadService;
import org.auscope.portal.core.server.http.download.FileUploadBean;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class TestCustomKMLController extends PortalTestClass {

    private FileDownloadService mockFileDownloadService;

    private CustomKMLController customKMLController;

    @Before
    public void setUp() {
        this.mockFileDownloadService = context.mock(FileDownloadService.class);
        this.customKMLController = new CustomKMLController(mockFileDownloadService);
    }

    @Test
    public void testAddKMLLayer() throws IOException, ParserConfigurationException, SAXException {

        final String fileResource = "au/gov/geoscience/portal/server/controllers/file.kml";
        final String filename = "file.kml";

        DiskFileItemFactory factory = new DiskFileItemFactory();
        FileItem fileItem = factory.createItem("kml_file", "application/vnd.google-earth.kml+xml", true, filename);
        InputStream kmlStream = ResourceUtil.loadResourceAsStream(fileResource);
        IOUtils.copy(kmlStream, fileItem.getOutputStream());
        CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
        FileUploadBean fileUploadBean = new FileUploadBean();
        fileUploadBean.setFile(file);

        String kmlOutput = customKMLController.addKMLLayer(fileUploadBean);
        Gson gson = new Gson();
        ModelMap valueMap = gson.fromJson(kmlOutput, ModelMap.class);
        String kmlString = ResourceUtil.loadResourceAsString(fileResource);

        Assert.assertTrue((Boolean) valueMap.get("success"));
        Assert.assertEquals(filename, valueMap.get("name"));
        Assert.assertTrue(xmlStringEquals(
                kmlString,
                (String) valueMap.get("file"),
                false));
    }

    @Test
    public void testFailedAddKMLLayer() {
        String kmlOutput = customKMLController.addKMLLayer(null);

        Gson gson = new Gson();
        ModelMap valueMap = gson.fromJson(kmlOutput, ModelMap.class);

        Assert.assertFalse((Boolean) valueMap.get("success"));
    }

    @Test
    public void testAddKMLUrl() throws IOException, ParserConfigurationException, SAXException {

        final String mockUrl = "http://kml.com/file.kml";
        final String mockName = "kml.com.kml";
        final String fileResource = "au/gov/geoscience/portal/server/controllers/file.kml";

        final DownloadResponse response = new DownloadResponse(mockUrl);
        String kmlString = ResourceUtil.loadResourceAsString(fileResource);
        response.setResponseStream(new ByteArrayInputStream(kmlString.getBytes()));
        response.setContentType("kml");

        context.checking(new Expectations() {
            {

                oneOf(mockFileDownloadService).singleFileDownloadFromURL(mockUrl);
                will(returnValue(response));
            }
        });

        ModelAndView kmlResponse = customKMLController.addKMLUrl(mockUrl);

        Assert.assertTrue((Boolean) kmlResponse.getModel().get("success"));

        ModelMap data = (ModelMap) kmlResponse.getModel().get("data");
        Assert.assertEquals(mockName, data.get("name"));
        Assert.assertTrue(xmlStringEquals(
                ResourceUtil.loadResourceAsString(fileResource),
                (String) data.get("file"),
                false));
    }

    @Test
    public void testAddInvalidKML() throws IOException {
        final String mockUrl = "http://kml.com/invalid.kml";
        final String fileResource = "au/gov/geoscience/portal/server/controllers/invalid.kml";

        final DownloadResponse response = new DownloadResponse(mockUrl);
        String kmlString = ResourceUtil.loadResourceAsString(fileResource);
        response.setResponseStream(new ByteArrayInputStream(kmlString.getBytes()));


        context.checking(new Expectations() {
            {

                oneOf(mockFileDownloadService).singleFileDownloadFromURL(mockUrl);
                will(returnValue(response));
            }
        });

        ModelAndView kmlResponse = customKMLController.addKMLUrl(mockUrl);

        Assert.assertFalse((Boolean) kmlResponse.getModel().get("success"));
    }

    @Test
    public void testFailedAddKML() throws IOException {
        final String mockUrl = "http://kml.com/invalid.kml";
        final DownloadResponse response = new DownloadResponse(mockUrl);

        context.checking(new Expectations() {
            {

                oneOf(mockFileDownloadService).singleFileDownloadFromURL(mockUrl);
                will(returnValue(response));
            }
        });

        ModelAndView kmlResponse = customKMLController.addKMLUrl(mockUrl);

        Assert.assertFalse((Boolean) kmlResponse.getModel().get("success"));
    }
}
