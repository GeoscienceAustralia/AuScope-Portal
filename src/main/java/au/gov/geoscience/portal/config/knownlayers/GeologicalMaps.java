package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterVocabulary;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.view.knownlayer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GeologicalMaps {

    private final static String GROUP = "Geological Maps";
    private final static int GROUP_ORDER = GroupOrder.GEOLOGICAL_MAPS;

    private final static String NAGIOS_HOST_GROUP = "GeoscienceAustralia";

    @Bean(name = "250k-scanned-maps")
    @Order(GROUP_ORDER)
    KnownLayer scannedGeologicalMaps() {
        String id = "250k-scanned-maps";
        String name = "Scanned 250K Geological Map Index";

        KnownLayerSelector selector = new WMSSelector("Scanned_250K_Geological_Map_Index");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("This Web Map Service displays the spatial extents of scanned images of all " +
                "1:250,000 scale geological maps of Australia. The service contains information on the edition, " +
                "publication date, and map publisher, and has links to map images available as 75 DPI, 125 DPI or " +
                "250 DPI resolution JPG files.");

        knownLayer.setGroup(GROUP);

        knownLayer.setProxyStyleUrl("scannedGeologicalMapsStyle.do");
        knownLayer.setSingleTile(true);
        //knownLayer.setStaticLegendUrl("http://services.ga.gov.au/Scanned_250K_Geological_Map_Index/capabilities/Scanned_250K_Geological_Map_Index_Legend.png");

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter mapSheetFilter = new UIFilterVocabulary("Map Name", "All map sheets", "mapName", "getAreaMaps.do");
        mapSheetFilter.setToolTip("The name of the map sheet you wish to view, eg CANBERRA.");

        List<AbstractBaseFilter> filters = Arrays.asList(mapSheetFilter);

        filterCollection.setOptionalFilters(filters);
        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }

    @Bean(name = "contacts-faults-dykes-marker-beds")
    @Order(GROUP_ORDER + 1)
    KnownLayer contactsFaultsDykesMarkerBeds() {
        String id = "contacts-faults-dykes-marker-beds";
        String name = "Contacts, faults, dykes, marker beds";

        List<String> layerNames = Arrays.asList("AUS_GA_2500k_Faults",
                "AUS_GA_2500k_Contacts",
                "AUS_GA_1M_Faults",
                "AUS_GA_1M_Contacts",
                "AUS_GA_1M_GULine");

        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("This layer contains lines that complement the geology data, but are not classified " +
                "as geological features (eg, coastline and other water boundaries, limits of mapping or available " +
                "data), compiled at 1:1M scale. This layer is limited to display only at scales greater than " +
                "1:1,500,000), AUS GA 1:1M Faults and Shears (Fault and shear structures, represented as lines, " +
                "compiled for use around 1:1M scale. This layer is limited to display only at scales greater than " +
                "1:1,500,000), AUS GA 1:1M Contacts (Lines that represent contacts between geological units (eg, " +
                "depositional, intrusive and faulted contacts), compiled for use around 1:1M scale. This layer is " +
                "limited to display only at scales greater than 1:1,500,000), AUS GA 1:1M Geological Unit Lines " +
                "(Surface geological units (outcrop or near-outcrop) that are too narrow to be represented by polygons, " +
                "compiled for use around 1:1M scale. This layer is limited to display only at scales greater than " +
                "1:1,500,000.");

        knownLayer.setGroup(GROUP);

        knownLayer.setSingleTile(true);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;
    }

    @Bean(name = "geological-units-lithostratigraphy")
    @Order(GROUP_ORDER + 2)
    KnownLayer geologicalUnitsLithostratigraphy() {
        String id = "geological-units-lithostratigraphy";
        String name = "Geological units (lithostratigraphy)";

        List<String> layerNames = Arrays.asList(
                "AUS_GA_2500k_GUPoly_Lithostratigraphy",
                "AUS_GA_1M_GUPoly_Lithostratigraphy"
        );

        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);

        KnownLayer knownLayer = new KnownLayer(id, selector);


        knownLayer.setName(name);
        knownLayer.setDescription("Surface geological units (outcrop or near-outcrop bedrock and surficial regolith " +
                "units) symbolised by lithostratigraphic classification.");

        knownLayer.setGroup(GROUP);

        knownLayer.setSingleTile(true);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);


        return knownLayer;
    }

    @Bean(name = "geological-units-lithology")
    @Order(GROUP_ORDER + 3)
    KnownLayer geologicalUnitsLithology() {
        String id = "geological-units-lithology";
        String name = "Geological units (lithology)";

        List<String> layerNames = Arrays.asList(
                "AUS_GA_2500k_GUPoly_Lithology",
                "AUS_GA_1M_GUPoly_Lithology"
        );

        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("Surface geological units (outcrop or near-outcrop bedrock and surficial regolith " +
                "units) symbolised by lithological classification.");

        knownLayer.setGroup(GROUP);

        knownLayer.setSingleTile(true);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;

    }

    @Bean(name = "geological-units-age")
    @Order(GROUP_ORDER + 4)
    KnownLayer geologicalUnitsAge() {
        String id = "geological-units-age";
        String name = "Geological units (age)";

        List<String> layerNames = Arrays.asList(
                "AUS_GA_2500k_GUPoly_Age",
                "AUS_GA_1M_GUPoly_Age"
        );

        KnownLayerSelector selector = new WMSSelectors(SelectorsMode.AND, layerNames);

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("Surface geological units (outcrop or near-outcrop bedrock and surficial regolith " +
                "units) symbolised by age.");

        knownLayer.setGroup(GROUP);

        knownLayer.setSingleTile(true);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;
    }
}
