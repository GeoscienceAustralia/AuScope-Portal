package au.gov.geoscience.portal.server.controllers;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.server.http.download.DownloadResponse;
import org.auscope.portal.core.server.http.download.FileDownloadService;
import org.auscope.portal.core.server.http.download.FileUploadBean;
import org.auscope.portal.core.util.DOMUtil;
import org.auscope.portal.core.util.MimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Controller
public class CustomKMLController extends BasePortalController {
    FileDownloadService fileDownloadService;

    @Autowired
    public CustomKMLController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    @RequestMapping("/addKMLLayer.do")
    public @ResponseBody String addKMLLayer(FileUploadBean uploadItem) {
        try {
            CommonsMultipartFile file = uploadItem.getFile();
            ModelMap model = new ModelMap();
            model.put("success", true);
            model.put("file", IOUtils.toString(file.getInputStream()));
            model.put("name", file.getOriginalFilename());
            return new Gson().toJson(model);
        } catch (Exception e) {
            ModelMap model = new ModelMap();
            model.put("success", false);
            return new Gson().toJson(model);
        }
    }

    @RequestMapping("/addKMLUrl.do")
    public ModelAndView addKMLUrl(
            @RequestParam("url") String url) throws IOException {

        try {

            DownloadResponse dlRes = this.fileDownloadService.singleFileDownloadFromURL(url);

            URI uri = new URI(url);
            String fileExtension = MimeUtil.mimeToFileExtension(dlRes.getContentType());
            if (fileExtension != null && !fileExtension.isEmpty()) {
                fileExtension = "." + fileExtension;
            }

            String kmlString = dlRes.getResponseAsString();

            Document kml = DOMUtil.buildDomFromString(kmlString, true);
            if (kml.getDocumentElement() != null &&  !kml.getDocumentElement().getLocalName().equals("kml")) {
                return generateJSONResponseMAV(false, null, "Sorry, the URL you have supplied is not KML data");
            }

            ModelMap model = new ModelMap();
            model.put("success", true);
            model.put("file", kmlString);
            model.put("name", uri.getHost() + fileExtension);

            return generateJSONResponseMAV(true, model, "success");

        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", url, e));
            log.debug("Exception: ", e);
            return generateJSONResponseMAV(false, null, e.getMessage());

        }

    }
}
