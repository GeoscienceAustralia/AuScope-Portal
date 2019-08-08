package au.gov.geoscience.portal.server.controllers;

import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.WFSService;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.SimpleBBoxFilter;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProvincesDownloadController extends BasePortalController {

    private WFSService wfsService;
    private WfsToCsvTransformer wfsToCsvTransformer;

    @Autowired
    public ProvincesDownloadController(WFSService wfsService, WfsToCsvTransformer wfsToCsvTransformer) {
        this.wfsService = wfsService;
        this.wfsToCsvTransformer = wfsToCsvTransformer;
    }

    @RequestMapping("/geologicalProvincesDownload.do")
    public ModelAndView geologicalProvincesDownload(@RequestParam("serviceUrl") String serviceUrl,
                                                    @RequestParam("typeName") String typeName,
                                                    @RequestParam(required = false, value = "bbox") String bboxJson,
                                                    @RequestParam(required = false, value = "maxFeatures") Integer maxFeatures,
                                                    @RequestParam(required = false, value = "outputFormat") String outputFormat) throws Exception {


        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson);

        SimpleBBoxFilter filterAdapter = new SimpleBBoxFilter();

        String filterString = filterAdapter.getFilterStringBoundingBox(bbox);

        WFSResponse wfsResponse = this.wfsService.getWfsResponse(serviceUrl, typeName, filterString, maxFeatures, null);

        String response = wfsToCsvTransformer.convert(wfsResponse.getData(), serviceUrl);

        return generateNamedJSONResponseMAV(true, "csv", response, wfsResponse.getMethod());

    }
}
