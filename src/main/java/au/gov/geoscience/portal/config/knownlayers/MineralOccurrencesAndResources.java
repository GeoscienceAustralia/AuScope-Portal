package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterText;
import au.gov.geoscience.portal.uifilter.UIFilterVocabulary;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.uifilter.optional.UICheckBoxGroupProvider;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WFSSelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration
public class MineralOccurrencesAndResources {


    private final static String GROUP = "Mineral Occurrences and Resources";
    private final static int GROUP_ORDER = GroupOrder.MINERAL_OCCURRENCES_AND_RESOURCES;


    private final static String NAGIOS_HOST_GROUP = "GeolSurveySISSDeployments";

    @Value("${portal.build.environment.host}")
    private String HOSTNAME;

    /**
     * This bean is configured for the now deprecated mo:MinOccView feature type.
     * However a number of state surveys still use this feature type, so until
     * they replace it with the preferred erl:MineralOccurrenceView feature type
     * this will need to be used
     *
     * @return mo:MinOccView known layer.
     * @deprecated
     */
    @Bean(name = "min-occ-view")
    @Order(GROUP_ORDER)
    KnownLayer minOccView() {
        String id = "min-occ-view";
        String name = "Mineral Occurrences (old version)";
        String description = "A collection of services that implement the AuScope EarthResourceML v1 Profile for " +
                "mo:MinOccView.";


        String[] serviceEndPoints = new String[]{"http://geology.data.vic.gov.au/nvcl/wfs",
                "http://geossdi.dmp.wa.gov.au/services/wfs", "http://geology.data.vic.gov.au:80/nvcl/wfs"};

        KnownLayerSelector selector = new WFSSelector("mo:MinOccView", serviceEndPoints, true);


        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("minOccViewFilterCount.do");
        knownLayer.setProxyStyleUrl("minOccViewFilterStyle.do");
        knownLayer.setProxyDownloadUrl("minOccViewFilterDownload.do");

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/mo_minoccview.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All names", "name");
        nameFilter.setToolTip("Type all or part of a mineral occurrence name. Use wildcards to broaden your search. " +
                "# = a single character, * = any number of characters (eg, M*T#ISA*). Search terms are case " +
                "insensitive.");


        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }


    @Bean(name = "mineral-occurrence-view")
    @Order(GROUP_ORDER + 1)
    KnownLayer mineralOccurrenceView() {
        String id = "mineral-occurrence-view";
        String name = "Mineral Occurrences (new version)";
        String description = "A collection of services that implement the EarthResourceML Lite schema for " +
                "erl:MineralOccurrenceView";

        String[] serviceEndPoints = new String[]{"https://geology.data.nt.gov.au/geoserver/wfs", "https://www.mrt.tas.gov.au/web-services/wfs",
                "https://gs.geoscience.nsw.gov.au/geoserver/wfs", "https://services.ga.gov.au/gis/earthresource/wfs", "https://sarigdata.pir.sa.gov.au/geoserver/wfs"};

        KnownLayerSelector selector = new WFSSelector("erl:MineralOccurrenceView", serviceEndPoints, true);

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("mineralOccurrenceViewFilterCount.do");
        knownLayer.setProxyStyleUrl("mineralOccurrenceViewFilterStyle.do");
        knownLayer.setProxyDownloadUrl("mineralOccurrenceViewDownload.do");

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/erl_mineraloccurrenceview.jpg");


        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);


        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All names", "name");
        nameFilter.setToolTip("Type all or part of a mineral occurrence name. Use wildcards to broaden your search. " +
                "# = a single character, * = any number of characters (eg, M*T#ISA*). Search terms are case " +
                "insensitive.");


        AbstractBaseFilter commodityFilter = new UIFilterVocabulary("Primary Commodity", "All commodities",
                "commodityUri", "getAllCommodities.do");

        commodityFilter.setToolTip("Choose from a list of commodities from our commodity lookup service. This will " +
                "query on the major commodity associated with a mineral occurrence");

        AbstractBaseFilter timescaleFilter = new UIFilterVocabulary("Geologic Timescale", "All timescales",
                "timescaleUri", "getAllTimescales.do");

        timescaleFilter.setToolTip("Choose from a list of eons, eras, and periods from our timescale lookup service. " +
                "This will query on the timescale you select, and any timescales contained within that selection");


        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");


        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, commodityFilter, timescaleFilter,
                providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }


    @Bean(name = "commodity-resource-view")
    @Order(GROUP_ORDER + 2)
    KnownLayer commodityResourceView() {
        String id = "commodity-resource-view";
        String name = "Commodities, Reserves and Resources";
        String description = "A collection of services that implement the EarthResourceML Lite schema for " +
                "erl:CommodityResourceView.";

        KnownLayerSelector selector = new WFSSelector("erl:CommodityResourceView");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);

        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("commodityResourceViewFilterCount.do");
        knownLayer.setProxyStyleUrl("commodityResourceViewFilterStyle.do");
        knownLayer.setProxyDownloadUrl("commodityResourceViewDownload.do");

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/erl_commodityresourceview.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All names", "name");
        nameFilter.setToolTip("Type all or part of a mineral occurrence name. Use wildcards to broaden your search. " +
                "# = a single character, * = any number of characters (eg, M*T#ISA*). Search terms are case " +
                "insensitive.");


        AbstractBaseFilter commodityFilter = new UIFilterVocabulary("Commodity", "All commodities",
                "commodityUri", "getAllCommodities.do");

        commodityFilter.setToolTip("Choose from a list of commodities from our commodity lookup service. ");

        AbstractBaseFilter jorcCategoryFilter = new UIFilterVocabulary("Resource Category", "All categories",
                "jorcCategoryUri", "getAllJorcCategories.do");

        jorcCategoryFilter.setToolTip("Choose from a list of JORC-related resource categories, from our reserve and " +
                "resource category lookup service");


        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");


        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, commodityFilter, jorcCategoryFilter,
                providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }
}


