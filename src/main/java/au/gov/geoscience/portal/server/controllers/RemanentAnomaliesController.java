package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.core.util.SLDLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

@Controller
public class RemanentAnomaliesController extends BasePortalController {

    public RemanentAnomaliesController() {
    }

    @RequestMapping("/remanentAnomaliesFilterStyle.do")
    public void remanentAnomaliesFilterStyle(@RequestParam(required = false, value = "serviceUrl") String serviceUrl,
                                           @RequestParam(required = false, value = "name") String name,
                                           @RequestParam(required = false, value = "tenementTypeUri") String tenementTypeUri,
                                           @RequestParam(required = false, value = "owner") String owner,
                                           @RequestParam(required = false, value = "tenementStatusUri") String tenementStatusUri,
                                           @RequestParam(required = false, value = "endDate") String endDate,
                                           HttpServletResponse response) throws Exception {
        String style;
        style = SLDLoader.loadSLDWithFilter("/au/gov/geoscience/portal/sld/remanentanomalies.sld", "");
        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }
}
