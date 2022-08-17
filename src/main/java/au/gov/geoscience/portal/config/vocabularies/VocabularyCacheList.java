package au.gov.geoscience.portal.config.vocabularies;

import au.gov.geoscience.portal.config.AppConfig;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.VocabularyService;
import org.auscope.portal.core.services.methodmakers.VocabularyMethodMaker;
import org.auscope.portal.core.services.vocabs.VocabularyServiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppConfig.class})
public class VocabularyCacheList {

    @Autowired
    HttpServiceCaller serviceCaller;

    @Bean
    VocabularyServiceItem vocabularyGeologicTimescales(){
        VocabularyService service = new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ands.org.au/repository/api/lda/csiro/international-chronostratigraphic-chart/2017");
        VocabularyServiceItem serviceItem =  new VocabularyServiceItem("vocabularyGeologicTimescales", "Geological Timescales Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyCommodities(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "https://vocabs.ardc.edu.au/repository/api/lda/ga/commodity-code/v0-2");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyCommodities", "Mineral Commodity Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyNVCLScalars(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ardc.edu.au/repository/api/lda/csiro/national-virtual-core-library-scalars/v0-3");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyNVCLScalars", "NVCL Scalars Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyMineStatuses(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/cgi/sissvoc/mine-status");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyMineStatuses", "Mine Status Vocabulary", service);
        return serviceItem;
    }


    @Bean
    VocabularyServiceItem vocabularyResourceAssessments(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/cgi/sissvoc/resource-assessment-category");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyResourceAssessments", "Resource Assessment Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyReserveAssessments(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/cgi/sissvoc/reserve-assessment-category");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyReserveAssessments", "Reserve Assessment Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyTenementStatuses(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/ggic/sissvoc/tenement-status");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyTenementStatuses", "Tenement Status Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyTenementTypes(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/ggic/sissvoc/tenement-type");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyTenementTypes", "Tenement Type Vocabulary", service);
        return serviceItem;
    }

    @Bean
    VocabularyServiceItem vocabularyPetroleumTenementStatuses(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://resource.geoscience.gov.au/classifierScheme/ggic/2021.01/petroleum-tenement-status");
        return new VocabularyServiceItem("vocabularyPetroleumTenementStatuses", "Petroleum Tenement Status Vocabulary", service);
    }

    @Bean
    VocabularyServiceItem vocabularyPetroleumTenementTypes(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://resource.geoscience.gov.au/classifierScheme/ggic/2021.01/petroleum-tenement-type");
        return new VocabularyServiceItem("vocabularyPetroleumTenementTypes", "Petroleum Tenement Type Vocabulary", service);
    }

    @Bean
    VocabularyServiceItem vocabularyMineralOccurrenceTypes(){
        VocabularyService service= new VocabularyService(serviceCaller, new VocabularyMethodMaker(), "http://vocabs.ga.gov.au/cgi/sissvoc/mineral-occurrence-type");
        VocabularyServiceItem serviceItem = new VocabularyServiceItem("vocabularyMineralOccurrenceTypes", "Mineral Occurrence Type   Vocabulary", service);
        return serviceItem;
    }
}
