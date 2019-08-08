package au.gov.geoscience.portal.server.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auscope.portal.core.server.controllers.BaseCSWController;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.view.ViewCSWRecordFactory;
import org.auscope.portal.core.view.ViewKnownLayerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import au.gov.geoscience.portal.server.services.SeismicSurveyService;

@Controller
@Scope("session")
// this can't be a singleton as each request by a user may be targeting a
// specific wms version
public class SeismicSurveyController extends BaseCSWController {

    // ----------------------------------------------------- Instance variables
    private static final String ECATURL = "https://ecat.ga.gov.au/geonetwork/srv/eng/csw";
	
    private static final String METADATASUBSTRING = "http://www.ga.gov.au/metadata-gateway/metadata/record/gcat_";
    
    private SeismicSurveyService seismicSurveyService;
    private final Log log = LogFactory.getLog(getClass());
    private int BUFFERSIZE = 1024 * 1024;
    HttpServiceCaller serviceCaller;

    // ----------------------------------------------------------- Constructors

    @Autowired
    public SeismicSurveyController(SeismicSurveyService seismicSurveyService,
            ViewCSWRecordFactory viewCSWRecordFactory,
            ViewKnownLayerFactory knownLayerFact,
            HttpServiceCaller serviceCaller) {
        super(viewCSWRecordFactory, knownLayerFact);
        this.seismicSurveyService = seismicSurveyService;
        this.serviceCaller = serviceCaller;
    }

    @RequestMapping(value = "/getSeismicCSWRecord.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getSeismicCSWRecord(@RequestParam("serviceUrl") String serviceUrl)
            throws Exception {
        
        String recordNumber = serviceUrl.replace(METADATASUBSTRING,"");
    
        CSWRecord[] record = new CSWRecord[1];
        record[0] = this.seismicSurveyService.getCSWRecord(recordNumber, ECATURL);
        record[0].setRecordInfoUrl(serviceUrl);
        ModelAndView mav = generateJSONResponseMAV(record, record.length);

        return mav;

    }

}
