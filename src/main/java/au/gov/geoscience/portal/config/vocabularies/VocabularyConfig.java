package au.gov.geoscience.portal.config.vocabularies;

import au.gov.geoscience.portal.config.AppConfig;
import au.gov.geoscience.portal.server.services.NvclVocabService;
import au.gov.geoscience.portal.server.services.methodmaker.NvclVocabMethodMaker;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.VocabularyCacheService;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.vocabs.VocabularyServiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({AppConfig.class, VocabularyCacheList.class})
public class VocabularyConfig {

    private final static String NVCL_URL = "http://auscope-services-test.arrc.csiro.au/sissvoc/";

    @Autowired
    HttpServiceCaller serviceCaller;

    @Autowired
    ThreadPoolTaskExecutor threadExecutor;

    @Autowired
    List<VocabularyServiceItem> serviceItemList;

    @Bean
    NvclVocabService nvclVocabService() {
        NvclVocabMethodMaker methodMaker = new NvclVocabMethodMaker();
        NvclVocabService service = new NvclVocabService(serviceCaller, methodMaker, NVCL_URL);
        return service;
    }

    @Bean
    VocabularyFilterService vocabularyFilterService() {
        VocabularyFilterService service = new VocabularyFilterService(vocabularyCacheService());
        return service;
    }

    @Bean
    VocabularyCacheService vocabularyCacheService() {
        ArrayList<VocabularyServiceItem> vocabularyServices = new ArrayList<>();
        vocabularyServices.addAll(serviceItemList);
        VocabularyCacheService service = new VocabularyCacheService(threadExecutor, vocabularyServices);
        return service;
    }


}
