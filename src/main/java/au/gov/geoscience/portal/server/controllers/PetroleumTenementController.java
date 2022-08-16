package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.PetroleumTenementService;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.core.util.SLDLoader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;

@Controller
public class PetroleumTenementController extends BasePortalController {
    private final PetroleumTenementService petroleumTenementService;

    @Autowired
    public PetroleumTenementController(PetroleumTenementService petroleumTenementService) {
        this.petroleumTenementService = petroleumTenementService;
    }

    @RequestMapping("/petroleumTenementFilterStyle.do")
    public void petroleumTenementFilterStyle(
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "holder") String holder,
            @RequestParam(required = false, value = "statusUri") String statusUri,
            @RequestParam(required = false, value = "tenementTypeUri") String tenementTypeUri, HttpServletResponse response) throws Exception {
        FilterBoundingBox bbox = null;
        // Add an escape for any modulus operators
        holder = this.escapeModulusOperators(holder);
        String filter = this.petroleumTenementService.getPetroleumTenementFilter(name, holder, bbox, statusUri, tenementTypeUri);
        String style = SLDLoader.loadSLDWithFilter("/au/gov/geoscience/portal/sld/petroleumtenement.sld", filter);
        response.setContentType("text/xml");
        ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
        OutputStream outputStream = response.getOutputStream();
        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);
        styleStream.close();
        outputStream.close();
    }

    @RequestMapping("/petroleumTenementFilterCount.do")
    public ModelAndView getPetroleumTenementCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "holder") String holder,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {
        // The presence of a bounding box causes us to assume we will be using this GML for visualizing on a map
        // This will in turn limit the number of points returned to 200
        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        WFSCountResponse response;
        try {
            response = this.petroleumTenementService.getTenementCount(serviceUrl, name, holder, maxFeatures, bbox);
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }
        return generateJSONResponseMAV(true, response.getNumberOfFeatures(), "");
    }

    @RequestMapping("/petroleumTenementFilterDownload.do")
    public void petroleumTenementFilterDownload(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "holder") String holder,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "forceOutputFormat", defaultValue = "false") Boolean forceOutputFormat,
            HttpServletResponse response) throws Exception {
        PetroleumTenementServiceProviderType petroleumTenementServiceProviderType = PetroleumTenementServiceProviderType.parseUrl(serviceUrl);
        // This is required to work with FilterBoundingBox. Needs a better fix than this
        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        if (!forceOutputFormat) {
            outputFormat = "CSV";
        }
        // Add an escape for any modulus operators
        holder = this.escapeModulusOperators(holder);
        String filter = this.petroleumTenementService.getPetroleumTenementFilter(name, holder, bbox, null, null);
        InputStream inputStream = this.petroleumTenementService.getAllTenements(serviceUrl, petroleumTenementServiceProviderType.featureType(), filter, maxFeatures, outputFormat);
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("text/csv");
        FileIOUtil.writeInputToOutputStream(inputStream, outputStream, 1024, false);
    }

    public String escapeModulusOperators(String value) {
        return value.replaceAll("%", Matcher.quoteReplacement("%%"));
    }
}
