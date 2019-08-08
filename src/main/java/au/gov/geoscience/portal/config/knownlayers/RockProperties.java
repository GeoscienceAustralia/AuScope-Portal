package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RockProperties {


    private final static String GROUP = "Rock Properties";
    private final static int GROUP_ORDER = GroupOrder.ROCK_PROPERTIES;

    private final static String ROCK_PROPERTIES_DOWNLOAD = "getRockPropertiesDownload.do";

    @Bean
    @Order(GROUP_ORDER)
    KnownLayer bulkDensity() {
        String id = "bulk-density";
        KnownLayerSelector selector = new WMSSelector("rockprops:BulkDensity");
        String name = "Bulk Density";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("bulk_density_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 1)
    KnownLayer chargeability() {
        String id = "chargeability";
        KnownLayerSelector selector = new WMSSelector("rockprops:Chargeability");
        String name = "Chargeability";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("chargeability_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 2)
    KnownLayer electricalConductivity() {
        String id = "electrical-conductivity";
        KnownLayerSelector selector = new WMSSelector("rockprops:ElectricalConductivity");
        String name = "Electrical Conductivity";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("electrical_conductivity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 2)
    KnownLayer electricalResistivity() {
        String id = "electrical-resistivity";
        KnownLayerSelector selector = new WMSSelector("rockprops:ElectricalResistivity");
        String name = "Electrical Resistivity";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("electrical_resistivity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 3)
    KnownLayer gammaBulkDensity() {
        String id = "gamma-bulk-density";
        KnownLayerSelector selector = new WMSSelector("rockprops:GammaBulkDensity");
        String name = "Gamma Bulk Density";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("gamma_density_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 4)
    KnownLayer koenigsbergerRatio() {
        String id = "koenigsberger-ratio";
        KnownLayerSelector selector = new WMSSelector("rockprops:KoenigsbergerRatio");
        String name = "Koenigsberger Ratio";

            KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("koenigsberger_ratio_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 5)
    KnownLayer magneticSusceptibility() {
        String id = "magnetic-susceptibility";
        KnownLayerSelector selector = new WMSSelector("rockprops:MagneticSusceptibility");
        String name = "Magnetic Susceptibility";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("magnetic_susceptibility_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 6)
    KnownLayer naturalGamma() {
        String id = "natural-gamma";
        KnownLayerSelector selector = new WMSSelector("rockprops:NaturalGamma");
        String name = "Natural Gamma";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("natural_gamma_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }


    @Bean
    @Order(GROUP_ORDER + 7)
    KnownLayer porosity() {
        String id = "porosity";
        KnownLayerSelector selector = new WMSSelector("rockprops:Porosity");
        String name = "Porosity";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("porosity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 8)
    KnownLayer remanentMagnetisation() {
        String id = "remanent-magnetisation";
        KnownLayerSelector selector = new WMSSelector("rockprops:RemanentMagnetisation");
        String name = "Remanent Magnetisation";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("remanent_magnetisation_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 9)
    KnownLayer sonicVelocity() {
        String id = "sonic-velocity";
        KnownLayerSelector selector = new WMSSelector("rockprops:SonicVelocity");
        String name = "Sonic Velocity";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("sonic_velocity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }

    @Bean
    @Order(GROUP_ORDER + 10)
    KnownLayer temperature() {
        String id = "temperature";
        KnownLayerSelector selector = new WMSSelector("rockprops:Temperature");
        String name = "Temperature";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("temperature_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        return knownLayer;
    }
}
