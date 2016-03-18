package org.auscope.portal.server.web.controllers;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.server.web.service.RockPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RockPropertiesController extends BasePortalController {
	
	private RockPropertiesService rockPropertiesService;

    // ----------------------------------------------------------- Constructors

	private static final String SERVICE_URL = "http://www.ga.gov.au/geophysics-rockpropertypub-gws/ga_rock_properties_wfs/wfs";
	private static final String BULK_DENSITY = "mass density"; 
	
	
    @Autowired
    public RockPropertiesController(RockPropertiesService rockPropertiesService) {
        this.rockPropertiesService = rockPropertiesService;
    }

    public ModelAndView getRockPropertiesFeature(String rockProperty, String bboxJson, int maxFeatures)
                    throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(SERVICE_URL);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

        try {
            WFSTransformedResponse response = this.rockPropertiesService.getRockPropertiesGML(SERVICE_URL, rockProperty, bbox,
                    maxFeatures);

            return generateJSONResponseMAV(response.getSuccess(), response.getGml(), response.getTransformed(), response.getMethod());
        } catch (Exception e) {
            log.warn(String.format("Error performing filter for '%1$s': %2$s", SERVICE_URL, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, SERVICE_URL);
        }
    }
	
	@RequestMapping("/getBulkDensityFeatures.do")
	public ModelAndView getBulkDensityFeatures( @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
                    throws Exception {
		return getRockPropertiesFeature(BULK_DENSITY, bboxJson, maxFeatures);
	}
}
