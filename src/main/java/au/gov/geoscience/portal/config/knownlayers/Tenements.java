package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterText;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.uifilter.optional.UICheckBoxGroupProvider;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WFSSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.auscope.portal.core.view.knownlayer.WMSWFSSelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration
public class Tenements {

    private static final int GROUP_ORDER = GroupOrder.TENEMENTS;
    private static final String GROUP = "Tenements";

    private static final String NAGIOS_HOST_GROUP = "GeolSurveySISSDeployments";

    @Value("${portal.build.environment.host}")
    private String HOSTNAME;

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

        knownLayer.setStaticLegendUrl(HOSTNAME + "/img/legends/mt_mineraltenement.jpg");

        knownLayer.setGroup(GROUP);
        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All tenement names", "name");
        nameFilter.setToolTip("Type all or part of a mineral tenement name. Use wildcards to broaden your search.  " +
                "# = a single character, * = any number of characters (eg, EL#234*). Search terms are case " +
                "insensitive.");

        AbstractBaseFilter ownerFilter = new UIFilterText("Owner", "All tenement owners",
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

    @Bean(name = "offshore-petroleum-tenements")
    @Order(GROUP_ORDER + 1)
    KnownLayer offshorePetroleumTenements() {
        String id = "offshore-petroleum-tenements";
        String name = "Offshore Petroleum Tenements";
        String description = "the boundary of the National Offshore Petroleum Tenements in accordance with the Offshore " +
                "Petroleum and Greenhouse Gas Storage Act (OPGGSA) 2006.";


        String[] serviceEndPoints = new String[]{
                "https://arcgis.nopta.gov.au:443/arcgis/services/Public/TitlesCompany_NOPTA/MapServer/WmsServer?"
        };

        KnownLayerSelector selector = new WMSSelector("0", serviceEndPoints, true);

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name="onshore-petroleum-tenements")
    @Order(GROUP_ORDER + 2)
    KnownLayer onshorePetroleumTenements() {
        String id = "onshore-petroleum-tenements";
        String name = "Onshore Petroleum Tenements";
        String description = "";
        String[] serviceEndPoints = new String[]{
                "https://gs.geoscience.nsw.gov.au/geoserver/pt/wfs",
                "https://geology.data.nt.gov.au/geoserver/wfs"
        };
        KnownLayerSelector selector = new WFSSelector("pt:PetroleumTenement", serviceEndPoints, true);
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setSingleTile(true);
        FilterCollection filterCollection = new FilterCollection();

        AbstractBaseFilter nameFilter = new UIFilterText("Name", "All tenement names", "name");
        nameFilter.setToolTip("Type all or part of a mineral tenement name. Use wildcards to broaden your search.  " +
                "# = a single character, * = any number of characters (eg, EL#234*). Search terms are case " +
                "insensitive.");

        AbstractBaseFilter ownerFilter = new UIFilterText("Owner", "All tenement owners",
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

    @Bean(name = "OPGGSA_2006_Blocks")
    @Order(GROUP_ORDER + 3)
    KnownLayer OPGGSA2006Blocks() {
        String id = "OPGGSA_2006_Blocks";
        String name = "OPGGSA 2006 Blocks";
        String description = "This dataset is a digital realisation of the Blocks as defined under Section 33 (3) of the " +
                "Offshore Petroleum and Greenhouse Gas Storage Act 2006. The blocks are created using a 5 minute by 5 minute " +
                "graticular section that has been trimmed to the extent of the Offshore Area (defined in the same Act). " +
                "The dataset is comprised of both points and polygons, and is densified to ensure the geometry will remain correct " +
                "regardless of projection. The dataset's coverage includes areas of coastal waters and land within the constitutional " +
                "limits of the States and territories, however in these areas the data is indicative only. For titles within the limits of " +
                "the coastal waters, the relevant State agency should be consulted.";

        KnownLayerSelector selector = new WMSSelector("OPGGSA_2006_Blocks");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "OPGGSA_2006_Blocks_Locations")
    @Order(GROUP_ORDER + 4)
    KnownLayer OPGGSA2006BlocksLocations() {
        String id = "OPGGSA_2006_Blocks_Locations";
        String name = "OPGGSA 2006 Blocks Locations";
        String description = "This dataset is a digital realisation of the Blocks as defined under Section 33 (3) of the Offshore " +
                "Petroleum and Greenhouse Gas Storage Act 2006. The blocks are created using a 5 minute by 5 minute graticular section " +
                "that has been trimmed to the extent of the Offshore Area (defined in the same Act). The dataset is comprised of both " +
                "points and polygons, and is densified to ensure the geometry will remain correct regardless of projection. " +
                "The dataset's coverage includes areas of coastal waters and land within the constitutional limits of the States " +
                "and territories, however in these areas the data is indicative only. For titles within the limits of the coastal waters, " +
                "the relevant State agency should be consulted.";

        KnownLayerSelector selector = new WMSSelector("OPGGSA_2006_Blocks_Locations");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

}
