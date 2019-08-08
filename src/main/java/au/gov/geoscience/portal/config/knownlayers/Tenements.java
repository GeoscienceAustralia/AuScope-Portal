package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterText;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.uifilter.optional.UICheckBoxGroupProvider;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.auscope.portal.core.view.knownlayer.WMSWFSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration
public class Tenements {

    private static final int GROUP_ORDER = GroupOrder.TENEMENTS;
    private static final String GROUP = "Tenements";

    private static final String NAGIOS_HOST_GROUP = "GeolSurveySISSDeployments";

    @Bean(name = "mineral-tenements")
    @Order(GROUP_ORDER)
    KnownLayer mineralTenements() {
        String id = "mineral-tenements";
        String name = "Mineral Tenements";
        String description = "A collection of services that implement the GGIC application schema mt:Mineral Tenement.";

        KnownLayerSelector selector = new WMSWFSSelector("mt:MineralTenement", "MineralTenement");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setProxyCountUrl("mineralTenementFilterCount.do");
        knownLayer.setProxyStyleUrl("mineralTenementFilterStyle.do");
        knownLayer.setProxyDownloadUrl("mineralTenementFilterDownload.do");

        knownLayer.setStaticLegendUrl("${portal.build.environment.host}/img/legends/mt_mineraltenement.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All tenement names", "name");
        nameFilter.setToolTip("Type all or part of a mineral tenement name. Use wildcards to broaden your search.  " +
                "# = a single character, * = any number of characters (eg, EL#234*). Search terms are case " +
                "insensitive.");

        AbstractBaseFilter ownerFilter = new UIFilterText("Owner","All tenement owners",
                "owner");
        ownerFilter.setToolTip("Type all or part of a mineral tenement owner. Use wildcards to broaden your search.  " +
                "# = a single character, * = any number of characters (eg, *BIL#ITON*). Search terms are case " +
                "insensitive.");

        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider", "All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, ownerFilter, providerFilter));

        knownLayer.setFilterCollection(filterCollection);


        return knownLayer;
    }

    @Bean(name = "offshore-petroleum-titles")
    @Order(GROUP_ORDER + 1)
    KnownLayer offshorePetroleumTitles() {
        String id = "offshore-petroleum-titles";
        String name = "Offshore Petroleum Titles";
        String description = "the boundary of the National Offshore Petroleum Titles in accordance with the Offshore " +
                "Petroleum and Greenhouse Gas Storage Act (OPGGSA) 2006.";

        KnownLayerSelector selector = new WMSSelector("Petroleum Titles Company");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }}
