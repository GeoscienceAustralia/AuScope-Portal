package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterText;
import au.gov.geoscience.portal.uifilter.UIFilterVocabulary;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.uifilter.optional.UICheckBoxGroupProvider;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WFSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration
public class MinesAndMiningActivity {

    private final static String GROUP = "Mines and Mining Activity";
    private final static int GROUP_ORDER = GroupOrder.MINES_AND_MINING_ACTIVITY;

    private final static String NAGIOS_HOST_GROUP = "GeolSurveySISSDeployments";

    @Bean(name = "mine-view")
    @Order(GROUP_ORDER)
    KnownLayer mineView() {
        String id = "mine-view";
        String name = "Mines (new version)";
        String description = "A collection of services that implement the EarthResourceML Lite v1.0 schema for " +
                "erl:MineView.";

        KnownLayerSelector selector = new WFSSelector("erl:MineView");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("mineViewFilterCount.do");
        knownLayer.setProxyStyleUrl("mineViewFilterStyle.do");
        knownLayer.setProxyDownloadUrl("mineViewDownload.do");

        knownLayer.setStaticLegendUrl("${portal.build.environment.host}/img/legends/erl_mineview.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All mine names", "name");
        nameFilter.setToolTip("Type all or part of a mine name. Use wildcards to broaden your search.  # = a single " +
                "character, * = any number of characters. (eg, M*T#ISA*). Search terms are case insensitive.");

        AbstractBaseFilter mineStatusFilter = new UIFilterVocabulary("Operating status",
                "All mine statuses", "statusUri", "getAllMineStatuses.do");
        mineStatusFilter.setToolTip("Choose from a list of operating mine statuses from our mine status lookup service.");

        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, mineStatusFilter, providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }

    @Bean(name = "mine")
    @Order(GROUP_ORDER + 1)
    KnownLayer mine() {
        String id = "mine";
        String name = "Mine";
        String description = "A collection of services that implement the AuScope EarthResourceML v1 Profile for er:Mine.";

        WFSSelector selector = new WFSSelector("er:MiningFeatureOccurrence");

        selector.setRelatedFeatureTypeNames(new String[]{"er:Mine", "gsml:MappedFeature"});

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("mineFilterCount.do");
        knownLayer.setProxyStyleUrl("mineFilterStyle.do");
        knownLayer.setProxyDownloadUrl("mineDownload.do");

        knownLayer.setStaticLegendUrl("${portal.build.environment.host}/img/legends/er_mine.jpg");


        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All mine names",
                "mineName");
        nameFilter.setToolTip("Type all or part of a mine name. Use wildcards to broaden your search.  # = a single " +
                "character, * = any number of characters. (eg, M*T#ISA*). Search terms are case insensitive.");

        AbstractBaseFilter mineStatusFilter = new UIFilterVocabulary("Operating status",
                "All mine statuses", "statusUri", "getAllMineStatuses.do");
        mineStatusFilter.setToolTip("Choose from a list of operating mine statuses from our mine status lookup " +
                "service.");

        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, mineStatusFilter, providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }


    @Bean(name = "mining-activity")
    @Order(GROUP_ORDER + 2)
    KnownLayer miningActivity() {
        String id = "mining-activity";
        String name = "Mining Activity";
        String description = "A collection of services that implement the AuScope EarthResourceML v1 Profile for " +
                "er:MiningActivity.";

        WFSSelector selector = new WFSSelector("er:MiningFeatureOccurrence");

        selector.setRelatedFeatureTypeNames(new String[]{"er:MiningActivity", "gsml:MappedFeature"});

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("miningActivityFilterCount.do");
        knownLayer.setProxyStyleUrl("miningActivityFilterStyle.do");
        knownLayer.setProxyDownloadUrl("miningActivityDownload.do");

        knownLayer.setStaticLegendUrl("${portal.build.environment.host}/img/legends/er_miningactivity.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All mine names", "mineName");
        nameFilter.setToolTip("Type all or part of a mine name. Use wildcards to broaden your search.  # = a single " +
                "character, * = any number of characters. (eg, M*T#ISA*). Search terms are case insensitive.");

        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, providerFilter));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }


}
