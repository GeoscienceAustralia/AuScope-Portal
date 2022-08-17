package au.gov.geoscience.portal.server.controllers;

import org.apache.jena.rdf.model.Model;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.VocabularyCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Controller for cached vocabulary
 */
@Controller
public class VocabularyCacheController extends BasePortalController {

    private final VocabularyCacheService vocabularyCacheService;

    @Autowired
    public VocabularyCacheController(VocabularyCacheService vocabularyCacheService) {
        this.vocabularyCacheService = vocabularyCacheService;

        vocabularyCacheService.updateCache();
    }

    /**
     * Get the full set of vocabulary with the repositories they belong to
     *
     * @return
     */
    @RequestMapping("/getVocabularies.do")
    public ModelAndView getVocabularies() {
        Map<String, Model> vocabularyCache = this.vocabularyCacheService.getVocabularyCache();
        return generateJSONResponseMAV(true, vocabularyCache, "success");

    }

    /**
     * Updates the vocabulary cache
     *
     * @return
     */
    @RequestMapping("updateVocabularyCache.do")
    public ModelAndView updateVocabularyCache() {
        try {
            this.vocabularyCacheService.updateCache();
            return generateJSONResponseMAV(true);
        } catch (Exception e) {
            log.warn(String.format("Error updating vocabulary cache: %1$s", e));
            log.debug("Exception:", e);
            return generateJSONResponseMAV(false);
        }
    }

}
