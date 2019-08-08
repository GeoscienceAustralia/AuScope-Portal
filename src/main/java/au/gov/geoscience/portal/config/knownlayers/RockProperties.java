package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RockProperties {


    private final static String GROUP = "Rock Properties";
    private final static int GROUP_ORDER = GroupOrder.ROCK_PROPERTIES;

    private final static String ROCK_PROPERTIES_DOWNLOAD = "getRockPropertiesDownload.do";

    @Value("${portal.build.environment.host}")
    private String HOSTNAME;

    @Bean
    @Order(GROUP_ORDER)
    KnownLayer bulkDensity() {
        String id = "bulk-density";
        KnownLayerSelector selector = new WMSSelector("rockprops:BulkDensity");
        String name = "Bulk Density";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setMapStyles("rockprops:bulk_density_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/bulk_density_cluster_legend.jpg");

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

        knownLayer.setMapStyles("rockprops:chargeability_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/chargeability_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:electrical_conductivity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/electrical_conductivity_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:electrical_resistivity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/electrical_resistivity_cluster_legend.jpg");

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

        knownLayer.setMapStyles("rockprops:gamma_density_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);
        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/gamma_bulk_density_cluster_legend.jpg");

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

        knownLayer.setMapStyles("rockprops:koenigsberger_ratio_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/koenigsberger_ratio_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:magnetic_susceptibility_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/magnetic_susceptibility_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:natural_gamma_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/natural_gamma_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:porosity_cluster");

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

        knownLayer.setMapStyles("rockprops:remanent_magnetisation_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/remanent_magnetisation_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:sonic_velocity_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/sonic_velocity_cluster_legend.jpg");


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

        knownLayer.setMapStyles("rockprops:temperature_cluster");

        knownLayer.setProxyDownloadUrl(ROCK_PROPERTIES_DOWNLOAD);

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/rock_properties/temperature_cluster_legend.jpg");


        return knownLayer;
    }
}
