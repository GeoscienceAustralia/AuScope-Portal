package org.auscope.portal.server.web.controllers;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.server.web.service.ErmlVocabService;
import org.auscope.portal.server.web.service.NvclVocabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;

/**
 * Controller that enables access to vocabulary services.
 */
@Controller
public class VocabController extends BasePortalController {
    private final Log log = LogFactory.getLog(getClass());

    private NvclVocabService nvclVocabService;
    private ErmlVocabService ermlVocabService;

    @Inject
    private ResourceLoader resourceLoader;
    
    /**
     * Construct
     * 
     * @param
     */
    @Autowired
    public VocabController(NvclVocabService nvclVocabService, ErmlVocabService ermlVocabService) {
        super();
        this.nvclVocabService = nvclVocabService;
        this.ermlVocabService = ermlVocabService;
    }

    /**
     * Performs a query to the vocabulary service on behalf of the client and returns a JSON Map success: Set to either true or false data: The raw XML response
     * scopeNote: The scope note element from the response label: The label element from the response
     * 
     * @param repository
     * @param label
     * @return
     */
    @RequestMapping("/getScalar.do")
    public ModelAndView getScalarQuery(@RequestParam("repository") final String repository,
            @RequestParam("label") final String label) throws Exception {

        //Attempt to request and parse our response
        try {
            //Do the request
            List<String> definitions = nvclVocabService.getScalarDefinitionsByLabel(label);

            String labelString = null;
            String scopeNoteString = null;
            String definitionString = null;
            if (definitions != null && definitions.size() > 0) {
                labelString = label;
                scopeNoteString = definitions.get(0); //this is for legacy support
                definitionString = definitions.get(0);
            }

            return generateJSONResponseMAV(true,
                    createScalarQueryModel(scopeNoteString, labelString, definitionString), "");
        } catch (Exception ex) {
            //On error, just return failure JSON (and the response string if any)
            log.error("getVocabQuery ERROR: " + ex.getMessage());

            return generateJSONResponseMAV(false, null, "");
        }
    }

    private ModelMap createScalarQueryModel(final String scopeNote, final String label, final String definition) {
        ModelMap map = new ModelMap();
        map.put("scopeNote", scopeNote);
        map.put("definition", definition);
        map.put("label", label);

        return map;
    }

    /**
     * Get all GA commodity URNs with prefLabels
     *
     * @param
     */
    @RequestMapping("getAllCommodities.do")
    public ModelAndView getAllCommodities() throws Exception {
        JSONArray dataItems = new JSONArray();
        Map<String, String> urnLabelMappings = null;

        //Attempt to request and parse our response
        try {
            urnLabelMappings = ermlVocabService.getGaCommodityConcepts("en");
        } catch (Exception ex) {
            //On error, just return failure JSON (and the response string if any)
            log.error("Error accessing commodity mappings: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return generateJSONResponseMAV(false);
        }

        //Turn our map of urns -> labels into an array of arrays for the view
        for (String urn : urnLabelMappings.keySet()) {
            String label = urnLabelMappings.get(urn);

            JSONArray tableRow = new JSONArray();
            tableRow.add(urn);
            tableRow.add(label);
            dataItems.add(tableRow);
        }

        return generateJSONResponseMAV(true, dataItems, "");
    }
    
    /**
     * Get all Area Maps.
     * Ideally this would call a service to get the data from a vocabulary service 
     * @return Spring ModelAndView containing the JSON
     */
    @RequestMapping("getAreaMaps.do")
    public @ResponseBody String getAreaMaps() throws Exception {

        String jsonData = null;
        
        //Attempt to locate the resource containing the area maps and parse it into a String
        try {
            Resource resource = resourceLoader.getResource("classpath:/localdatastore/AreaMaps.json");
            StringWriter writer = new StringWriter();
            IOUtils.copy(resource.getInputStream(), writer, "UTF-8");
            jsonData = writer.toString();            
        } catch (Exception ex) {
            //On error, just return failure JSON (and the response string if any)
            log.error("Error accessing 1:250k area maps: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return null;
        }

        // got the data, generate a response
        return jsonData;
    }
}
