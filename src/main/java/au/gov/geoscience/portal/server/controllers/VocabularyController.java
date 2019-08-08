package au.gov.geoscience.portal.server.controllers;


import au.gov.geoscience.portal.server.services.NvclVocabService;
import au.gov.geoscience.portal.server.services.vocabularies.VocabularyLookup;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that enables access to vocabulary services.
 */
@Controller
public class VocabularyController extends BasePortalController {


    public static final String TIMESCALE_VOCABULARY_ID = "vocabularyGeologicTimescales";
    public static final String COMMODITY_VOCABULARY_ID = "vocabularyCommodities";
    public static final String MINE_STATUS_VOCABULARY_ID = "vocabularyMineStatuses";
    public static final String RESOURCE_VOCABULARY_ID = "vocabularyResourceCategories";
    public static final String RESERVE_VOCABULARY_ID = "vocabularyReserveCategories";
    public static final String TENEMENT_TYPE_VOCABULARY_ID = "vocabularyTenementType";
    public static final String TENEMENT_STATUS_VOCABULARY_ID = "vocabularyTenementStatus";
    public static final String MINERAL_OCCURRENCE_TYPE_VOCABULARY = "vocabularyMineralOccurrenceType";


    private final Log log = LogFactory.getLog(getClass());

    private NvclVocabService nvclVocabService;

    private VocabularyFilterService vocabularyFilterService;


    @Inject
    private ResourceLoader resourceLoader;

    /**
     * Construct
     *
     * @param
     */
    @Autowired
    public VocabularyController(NvclVocabService nvclVocabService,
                                VocabularyFilterService vocabularyFilterService) {
        super();
        this.nvclVocabService = nvclVocabService;

        this.vocabularyFilterService = vocabularyFilterService;
    }

    /**
     * Performs a query to the vocabulary service on behalf of the client and
     * returns a JSON Map success: Set to either true or false data: The raw XML
     * response scopeNote: The scope note element from the response label: The
     * label element from the response
     *
     * @param repository
     * @param label
     * @return
     */
    @RequestMapping("/getScalar.do")
    public ModelAndView getScalarQuery(@RequestParam("repository") final String repository,
                                       @RequestParam("label") final String label) throws Exception {

        // Attempt to request and parse our response
        try {
            // Do the request
            List<String> definitions = nvclVocabService.getScalarDefinitionsByLabel(label);

            String labelString = null;
            String scopeNoteString = null;
            String definitionString = null;
            if (definitions != null && definitions.size() > 0) {
                labelString = label;
                scopeNoteString = definitions.get(0); // this is for legacy
                // support
                definitionString = definitions.get(0);
            }

            return generateJSONResponseMAV(true, createScalarQueryModel(scopeNoteString, labelString, definitionString),
                    "");
        } catch (Exception ex) {
            // On error, just return failure JSON (and the response string if
            // any)
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


    @RequestMapping("getAllMineralOccurrenceTypes.do")
    public ModelAndView getAllMineralOccurrenceTypes() {
        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(MINERAL_OCCURRENCE_TYPE_VOCABULARY);

        return getVocabularyMappings(vocabularyMappings);
    }

    /**
     * Get all GA commodity URNs with prefLabels
     *
     * @param
     */
    @RequestMapping("getAllCommodities.do")
    public ModelAndView getAllCommodities() {
        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(COMMODITY_VOCABULARY_ID);

        return getVocabularyMappings(vocabularyMappings);
    }


    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("getAllMineStatuses.do")
    public ModelAndView getAllMineStatuses() {
        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(MINE_STATUS_VOCABULARY_ID);

        return getVocabularyMappings(vocabularyMappings);
    }


    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("getAllJorcCategories.do")
    public ModelAndView getAllJorcCategories() {

        Property sourceProperty = DCTerms.source;

        Selector selector = new SimpleSelector(null, sourceProperty, "CRIRSCO Code; JORC 2004", "en");


        Map<String, String> jorcCategoryMappings = new HashMap<String, String>();
        jorcCategoryMappings.put(VocabularyLookup.RESERVE_CATEGORY.uri(), "any reserves");
        jorcCategoryMappings.put(VocabularyLookup.RESOURCE_CATEGORY.uri(), "any resources");

        Map<String, String> resourceCategoryMappings = this.vocabularyFilterService.getVocabularyById(RESOURCE_VOCABULARY_ID, selector);
        Map<String, String> reserveCategoryMappings = this.vocabularyFilterService.getVocabularyById(RESERVE_VOCABULARY_ID, selector);
        jorcCategoryMappings.putAll(resourceCategoryMappings);
        jorcCategoryMappings.putAll(reserveCategoryMappings);

        return getVocabularyMappings(jorcCategoryMappings);


    }


    /**
     * @return
     */
    @RequestMapping("getAllTimescales.do")
    public ModelAndView getAllTimescales() {

        String[] ranks = {"http://resource.geosciml.org/ontology/timescale/gts#Period",
                "http://resource.geosciml.org/ontology/timescale/gts#Era",
                "http://resource.geosciml.org/ontology/timescale/gts#Eon"};

        Property typeProperty = RDF.type;

        Selector[] selectors = new Selector[ranks.length];
        for (int i = 0; i < ranks.length; i++) {
            selectors[i] = new SimpleSelector(null, typeProperty, ResourceFactory.createResource(ranks[i]));
        }
        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(TIMESCALE_VOCABULARY_ID, selectors);

        return getVocabularyMappings(vocabularyMappings);

    }

    /**
     * @return
     */
    @RequestMapping("getTenementTypes.do")
    public ModelAndView getTenementTypes() {
        String[] topConcepts = {
                "http://resource.geoscience.gov.au/classifier/ggic/tenementtype/production",
                "http://resource.geoscience.gov.au/classifier/ggic/tenementtype/exploration"
        };

        Selector[] selectors = new Selector[topConcepts.length];

        for (int i = 0; i < topConcepts.length; i++) {
            selectors[i] = new SimpleSelector(ResourceFactory.createResource(topConcepts[i]), null, (RDFNode) null);
        }

        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(TENEMENT_TYPE_VOCABULARY_ID, selectors);

        return getVocabularyMappings(vocabularyMappings);
    }

    /**
     * @return
     */
    @RequestMapping("getTenementStatuses.do")
    public ModelAndView getTenementStatuses() {
        String[] topConcepts = {
                "http://resource.geoscience.gov.au/classifier/ggic/tenement-status/granted",
                "http://resource.geoscience.gov.au/classifier/ggic/tenement-status/application"
        };

        Selector[] selectors = new Selector[topConcepts.length];

        for (int i = 0; i < topConcepts.length; i++) {
            selectors[i] = new SimpleSelector(ResourceFactory.createResource(topConcepts[i]), null, (RDFNode) null);
        }

        Map<String, String> vocabularyMappings = this.vocabularyFilterService.getVocabularyById(TENEMENT_STATUS_VOCABULARY_ID, selectors);

        return getVocabularyMappings(vocabularyMappings);
    }

    /**
     * @param vocabularyMappings
     * @return
     */
    private ModelAndView getVocabularyMappings(Map<String, String> vocabularyMappings) {
        JSONArray dataItems = new JSONArray();

        // Turn our map of urns -> labels into an array of arrays for the view
        for (String urn : vocabularyMappings.keySet()) {
            String label = vocabularyMappings.get(urn);

            JSONArray tableRow = new JSONArray();
            tableRow.add(urn);
            tableRow.add(label);
            dataItems.add(tableRow);
        }

        return generateJSONResponseMAV(true, dataItems, "");
    }

    /**
     * Get all Area Maps. Ideally this would call a service to get the data from
     * a vocabulary service
     *
     * @return Spring ModelAndView containing the JSON
     */
    @RequestMapping("getAreaMaps.do")
    public ModelAndView getAreaMaps() throws Exception {

        String jsonString = null;
        JSONArray json = null;
        // Attempt to locate the resource containing the area maps and parse it
        // into a String
        try {
            Resource resource = resourceLoader.getResource("classpath:/localdatastore/AreaMaps.json");
            StringWriter writer = new StringWriter();
            IOUtils.copy(resource.getInputStream(), writer, "UTF-8");
            jsonString = writer.toString();
            json = (JSONArray) JSONSerializer.toJSON(jsonString);
        } catch (Exception ex) {
            // On error, just return failure JSON (and the response string if
            // any)
            log.error("Error accessing 1:250k area maps: " + ex.getMessage());
            log.debug("Exception: ", ex);
            return null;
        }

        // got the data, generate a response
        return generateJSONResponseMAV(true, json, "");
    }
}
