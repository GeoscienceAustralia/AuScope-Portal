package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.CSWRecordSelector;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.beans.factory.annotation.Value;
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
        selector.setRecordId("dc0fc108b228b497f0e99397c57e39dfc3528d00");

        String name = "Protected Areas CAPAD 2020 - Marine";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "protected-areas-terrestrial")
    @Order(GROUP_ORDER + 1)
    KnownLayer protectedAreasTerrestrial() {
        String id = "protected-areas-terrestrial";
        KnownLayerSelector selector = new WMSSelector("Terrestrial CAPAD");
        String name = "Protected Areas CAPAD 2020 - Terrestrial";

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
}
