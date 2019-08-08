package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for all the topography and infrastructure layers
 */
@Configuration
public class TopographyAndInfrastructure {

    private static final String GROUP = "Topography and Infrastructure";
    private static final int GROUP_ORDER = GroupOrder.TOPOGRAPHY_AND_INFRASTRUCTURE;

    private static final String NAGIOS_HOST_GROUP = "GeoscienceAustralia";

    private static final String TOPOGRAPHY_LEGEND = "http://www.ga.gov.au/interactive-maps/content/legends/AustralianTopogaphyLegend.png";

    @Bean(name = "populated-places")
    @Order(GROUP_ORDER)
    KnownLayer populatedPlaces() {
        String id = "populated-places";
        List<String> layerNames = Arrays.asList("Populated_Places_1",
                "Populated_Places_2",
                "Populated_Places_3",
                "Populated_Places_4",
                "Populated_Places_5",
                "Populated_Places_6",
                "Populated_Places_7",
                "Populated_Places_8");
        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);
        String name = "Populated Places";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setStaticLegendUrl(TOPOGRAPHY_LEGEND);

        return knownLayer;
    }

    @Bean(name = "coastlines")
    @Order(GROUP_ORDER + 1)
    KnownLayer coastlines() {
        String id = "coastlines";
        KnownLayerSelector selector = new WMSSelector("Coastline1M");
        String name = "Coastlines";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setStaticLegendUrl(TOPOGRAPHY_LEGEND);

        return knownLayer;
    }

    @Bean(name = "rivers-and-lakes")
    @Order(GROUP_ORDER + 2)
    KnownLayer riversAndLakes() {
        String id = "rivers-and-lakes";
        List<String> layerNames = Arrays.asList("Watercourses_All_Rivers_Watercourse_Lines",
                "Watercourse_Areas",
                "Watercourses_Major_Rivers_National_Scale_to_5Million",
                "Watercourses_Major_Rivers_Scale_5Million_to_300000",
                "Waterbody_Extra_Lakes_and_Reservoirs",
                "Waterbody_Lakes_National_Scale_to_5Million",
                "Waterbody_Lakes_Scale_5Million_to_300000",
                "Waterbody_All_Lakes",
                "Waterbody_Reservoirs_National_Scale_to_300000",
                "Waterbody_All_Reservoirs");
        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);
        String name = "Rivers and Lakes";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setStaticLegendUrl(TOPOGRAPHY_LEGEND);

        return knownLayer;
    }

    @Bean(name = "roads")
    @Order(GROUP_ORDER + 3)
    KnownLayer roads() {
        String id = "roads";
        List<String> layerNames = Arrays.asList(
                "TOPO250K_Roads",
                "Roads_1",
                "Roads_2",
                "Roads_3",
                "Roads_4"
        );
        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);
        String name = "Roads";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setStaticLegendUrl(TOPOGRAPHY_LEGEND);

        return knownLayer;
    }

    @Bean(name = "railways")
    @Order(GROUP_ORDER + 4)
    KnownLayer railways() {
        String id = "railways";
        List<String> layerNames = Arrays.asList(
                "Railways_1",
                "Railways_2",
                "Railways_3"
        );
        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);
        String name = "Railways";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setStaticLegendUrl(TOPOGRAPHY_LEGEND);

        return knownLayer;
    }

    @Bean(name = "oil-pipelines")
    @Order(GROUP_ORDER + 5)
    KnownLayer oilPipelines() {
        String id = "oil-pipelines";
        KnownLayerSelector selector = new WMSSelector("National_Onshore_Oil_Pipelines");
        String name = "Oil Pipelines";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setProxyStyleUrl("getOilStyle.do");

        return knownLayer;
    }

    @Bean(name = "gas-pipelines")
    @Order(GROUP_ORDER + 6)
    KnownLayer gasPipelines() {
        String id = "gas-pipelines";
        KnownLayerSelector selector = new WMSSelector("National_Onshore_Gas_Pipelines");
        String name = "Gas Pipelines";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setProxyStyleUrl("getGasStyle.do");

        return knownLayer;
    }

    @Bean(name = "national-major-ports")
    @Order(GROUP_ORDER + 7)
    KnownLayer nationalMajorPorts() {
        String id = "national-major-ports";
        KnownLayerSelector selector = new WMSSelector("National_Major_Ports");
        String name = "National Major Ports";

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;
    }

}
