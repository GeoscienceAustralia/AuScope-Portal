package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.test.ResourceUtil;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.core.util.SLDLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

/**
 * Controller to produce StyledLayerDescriptors for ArcGis Server layers.
 */
@Controller
public class SLDController extends BasePortalController {

    @RequestMapping("/scannedGeologicalMapsStyle.do")
    public void getScannedGeologicalMapsStyle(@RequestParam(value = "mapName", required = false) String mapName,
                                              HttpServletResponse response) throws Exception {

        String scannedGeologicalMapsStyle = "";
        if (mapName != null && !mapName.isEmpty()) {
            String filter = "<ogc:Filter><ogc:PropertyIsLike escape=\"!\" wildCard=\"*\" singleChar=\"#\">"
                    + "<ogc:PropertyName>name</ogc:PropertyName>" + "<ogc:Literal>*" + mapName + "*</ogc:Literal>"
                    + "</ogc:PropertyIsLike>" + "</ogc:Filter>";
            scannedGeologicalMapsStyle = SLDLoader.loadSLDWithFilter("/au/gov/geoscience/portal/sld/scannedgeologicalmaps.sld", filter);
        }

        writeStyleStream(scannedGeologicalMapsStyle, response);
    }

    @RequestMapping("/getOilStyle.do")
    public void getOilStyle(HttpServletResponse response) throws Exception {

        String oilStyle = SLDLoader.loadSLD("/au/gov/geoscience/portal/sld/oilpipelines.sld", null, true);

        writeStyleStream(oilStyle, response);

    }

    @RequestMapping("/getGasStyle.do")
    public void getGasStyle(HttpServletResponse response) throws Exception {

        String gasStyle = SLDLoader.loadSLD("/au/gov/geoscience/portal/sld/gaspipelines.sld", null, true);

         writeStyleStream(gasStyle, response);

    }

    private void writeStyleStream(String style, HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.TEXT_XML_VALUE);
        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }
}
