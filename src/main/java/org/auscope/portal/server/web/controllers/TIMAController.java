package org.auscope.portal.server.web.controllers;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.gsml.TIMAGeosampleFilter;
import org.auscope.portal.server.web.service.WFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TIMAController extends BasePortalController {

    /** Used for making general WFS requests */
    private WFSService wfsService;

    @Autowired
    public TIMAController(WFSService wfsService) {
        this.wfsService = wfsService;
    }

    /**
     * Handles the borehole filter queries.
     *
     * @param serviceUrl
     *            the url of the service to query
     * @param sampleName
     *            The name of the sample
     * @param igsn
     * @return a WFS response converted into KML
     * @throws Exception
     */
    @RequestMapping("/doTIMAGeoSample.do")
    public ModelAndView doTIMAGeoSample(@RequestParam(required = false, value = "serviceUrl") String serviceUrl,
            @RequestParam(required = false, value = "sampleName") String sampleName,
            @RequestParam(required = false, value = "igsn") String igsn,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "200") int maxFeatures)
            throws Exception {

        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson,ogcServiceProviderType);

        //Build our filter details
        String filterString = generateGeoSampleFilter(sampleName, igsn, bbox);

        //Make our request and get it transformed
        WFSTransformedResponse response = null;
        try {
            response = wfsService.getWfsResponseAsKml(serviceUrl, "tima:geosample_and_mineralogy", filterString,
                    maxFeatures, null);
        } catch (Exception ex) {
            log.warn(String.format("Unable to request/transform WFS response for '%1$s' from '%2$s': %3$s", sampleName,
                    serviceUrl, ex));
            log.debug("Exception: ", ex);
            return generateExceptionResponse(ex, serviceUrl);
        }

        return generateJSONResponseMAV(true, response.getGml(), response.getTransformed(), response.getMethod());
    }

    /**
     * Utility function for generating an OGC filter for a TIMA simple feature
     * 
     * @return
     */
    private String generateGeoSampleFilter(String name, String igsn, FilterBoundingBox bbox) {
//        OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
//      FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxString,OgcServiceProviderType);
        TIMAGeosampleFilter timaGeosampleFilter = new TIMAGeosampleFilter(name, igsn);
        if (bbox == null) {
            return timaGeosampleFilter.getFilterStringAllRecords();
        } else {
            return timaGeosampleFilter.getFilterStringBoundingBox(bbox);
        }
    }

}
