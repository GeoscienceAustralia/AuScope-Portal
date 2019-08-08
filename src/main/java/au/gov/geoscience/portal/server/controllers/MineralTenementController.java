package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.server.MineralTenementServiceProviderType;
import au.gov.geoscience.portal.server.services.MineralTenementService;
import au.gov.geoscience.portal.xslt.ArcGISToMineralTenement;
import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.core.util.SLDLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

@Controller
public class MineralTenementController extends BasePortalController {

    private WMSService wmsService;
    private ArcGISToMineralTenement arcGISToMineralTenementTransformer;
    private WfsToCsvTransformer csvTransformer;
    private MineralTenementService mineralTenementService;

    @Autowired
    public MineralTenementController(WMSService wmsService, MineralTenementService mineralTenementService, ArcGISToMineralTenement arcGISToMineralTenement, WfsToCsvTransformer wfsToCsvTransformer) {
        this.wmsService = wmsService;
        this.mineralTenementService = mineralTenementService;
        this.arcGISToMineralTenementTransformer = arcGISToMineralTenement;
        this.csvTransformer = wfsToCsvTransformer;
    }

    @RequestMapping("/mineralTenementFilterStyle.do")
    public void mineralTenementFilterStyle(@RequestParam(required = false, value = "serviceUrl") String serviceUrl,
                                           @RequestParam(required = false, value = "name") String name,
                                           @RequestParam(required = false, value = "tenementTypeUri") String tenementTypeUri,
                                           @RequestParam(required = false, value = "owner") String owner,
                                           @RequestParam(required = false, value = "tenementStatusUri") String tenementStatusUri,
                                           @RequestParam(required = false, value = "endDate") String endDate,
                                           HttpServletResponse response) throws Exception {

        // Vt: wms shouldn't need the bbox because it is tiled.
        FilterBoundingBox bbox = null;

        MineralTenementServiceProviderType mineralTenementServiceProviderType = MineralTenementServiceProviderType.parseUrl(serviceUrl);

        String filter = this.mineralTenementService.getMineralTenementFilter(name, tenementTypeUri, owner, tenementStatusUri, endDate,
                bbox, mineralTenementServiceProviderType);


        String style = SLDLoader.loadSLDWithFilter("/au/gov/geoscience/portal/sld/mineraltenement.sld", filter);
        response.setContentType("text/xml");

        ByteArrayInputStream styleStream = new ByteArrayInputStream(
                style.getBytes());
        OutputStream outputStream = response.getOutputStream();

        FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

        styleStream.close();
        outputStream.close();
    }

    @RequestMapping("/mineralTenementFilterCount.do")
    public ModelAndView getMineralTenementCount(
            @RequestParam("serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "tenementName") String tenementName,
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
            throws Exception {

        // The presence of a bounding box causes us to assume we will be using this GML for visualizing on a map
        // This will in turn limit the number of points returned to 200
        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);
        WFSCountResponse response = null;
        try {
            response = this.mineralTenementService.getTenementCount(serviceUrl, tenementName, owner,
                    maxFeatures, bbox);


        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
        }


        return generateJSONResponseMAV(true, response.getNumberOfFeatures(), "");
    }
}
