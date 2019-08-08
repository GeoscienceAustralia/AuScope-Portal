package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.CSWRecordSelector;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RestrictedAreas {

    private static final String GROUP = "Restricted Areas";
    private static final int GROUP_ORDER = GroupOrder.RESTRICTED_AREAS;

    @Bean(name = "protected-areas-marine")
    @Order(GROUP_ORDER)
    KnownLayer protectedAreasMarine() {
        String id = "protected-areas-marine";
        CSWRecordSelector selector = new CSWRecordSelector();
        selector.setRecordId("6cd862d180adb50c7ec7fde53bc1422a10a8f64c");

        String name = "Protected Areas CAPAD 2014 - Marine";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "protected-areas-terrestrial")
    @Order(GROUP_ORDER + 1)
    KnownLayer protectedAreasTerrestrial() {
        String id = "protected-areas-terrestrial";
        KnownLayerSelector selector = new WMSSelector("Protected_Areas");
        String name = "Protected Areas CAPAD 2014 - Terrestrial";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "world-heritage-areas")
    @Order(GROUP_ORDER + 2)
    KnownLayer worldHeritageAreas() {
        String id = "world-heritage-areas";
        KnownLayerSelector selector = new WMSSelector("World_Heritage_Areas_Australia");
        String name = "World Heritage Areas";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "defence-prohibited-areas")
    @Order(GROUP_ORDER + 3)
    KnownLayer defenceProhibitedAreas() {
        String id = "defence-prohibited-areas";
        KnownLayerSelector selector = new WMSSelector("Defence_Prohibited_Areas");
        String name = "Defence Prohibited Areas";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "defence-practice-areas")
    @Order(GROUP_ORDER + 4)
    KnownLayer defencePracticeAreas() {
        String id = "defence-practice-areas";
        KnownLayerSelector selector = new WMSSelector("Defence_Practice_Areas");
        String name = "Defence Practice Areas";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "defence-training-areas")
    @Order(GROUP_ORDER + 5)
    KnownLayer defenceTrainingAreas() {
        String id = "defence-training-areas";
        KnownLayerSelector selector = new WMSSelector("Defence_Training_Areas");
        String name = "Defence Training Areas";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }
}
