package au.gov.geoscience.portal.config.knownlayers;

import au.gov.geoscience.portal.uifilter.UIFilterText;
import org.auscope.portal.core.uifilter.AbstractBaseFilter;
import org.auscope.portal.core.uifilter.FilterCollection;
import org.auscope.portal.core.uifilter.mandatory.AbstractMandatoryParamBinding;
import org.auscope.portal.core.uifilter.mandatory.UIHiddenParam;
import org.auscope.portal.core.uifilter.optional.UICheckBoxGroupProvider;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WFSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class Boreholes {

    private final static String GROUP = "Boreholes";
    private final static int GROUP_ORDER = GroupOrder.BOREHOLES;

    private final static String NAGIOS_HOST_GROUP = "GeolSurveySISSDeployments";

    @Order(GROUP_ORDER)
    @Bean(name = "national-virtual-core-library")
    KnownLayer nationalVirtualCoreLibrary() {
        String id = "national-virtual-core-library";
        String name = "National Virtual Core Library";

        KnownLayerSelector selector = new WFSSelector("gsmlp:BoreholeView");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("A collection of services implementing the National Virtual Core Library Profile " +
                "v1 for gsml:Borehole and a collection of observations.");

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setProxyCountUrl("nationalVirtualCoreLibraryFilterCount.do");
        knownLayer.setProxyDownloadUrl("doNVCLBoreholeViewCSVDownload.do");
        knownLayer.setProxyStyleUrl("nationalVirtualCoreLibraryFilterStyle.do");



        FilterCollection filterCollection = generateBoreholeFilters();

        AbstractMandatoryParamBinding nvclCollection = new UIHiddenParam("nvclCollection", "true");

        filterCollection.setHiddenParams(Collections.singletonList(nvclCollection));

        knownLayer.setFilterCollection(filterCollection);

        return knownLayer;
    }


    @Order(GROUP_ORDER + 1)
    @Bean(name = "all-boreholes")
    KnownLayer allBoreholes() {
        String id = "all-boreholes";
        String name = "All Boreholes";

        KnownLayerSelector selector = new WFSSelector("gsmlp:BoreholeView");

        KnownLayer knownLayer = new KnownLayer(id, selector);

        knownLayer.setName(name);
        knownLayer.setDescription("A collection of services implementing the GeoSciML Portrayal Borehole View " +
                "(gsmlp:BoreholeView).");

        knownLayer.setGroup(GROUP);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        knownLayer.setProxyCountUrl("doBoreholeViewCount.do");
        knownLayer.setProxyDownloadUrl("doBoreholeViewFilter.do");
        knownLayer.setProxyStyleUrl("doBoreholeViewFilterStyle.do");

        FilterCollection filterCollection = generateBoreholeFilters();

        knownLayer.setFilterCollection(filterCollection);

        knownLayer.setStaticLegendUrl("${portal.build.environment.host}/img/legends/gsmlp_boreholeview.jpg");

        return knownLayer;
    }

    FilterCollection generateBoreholeFilters() {
        FilterCollection filterCollection = new FilterCollection();


        AbstractBaseFilter nameFilter = new UIFilterText("Name","All names", "boreholeName");
        nameFilter.setToolTip("Type all or part of a borehole name. Use wildcards to broaden your search.  # = a " +
                "single character, * = any number of characters. (eg, *DD#1*). Search terms are case insensitive.");

        AbstractBaseFilter drillingDateFilter = new UIFilterText("Drilling Year","All years", "dateOfDrilling");
        drillingDateFilter.setToolTip("Type a year (eg, 1998). Use a wildcard to select a decade (eg, 199*).");


        AbstractBaseFilter providerFilter = new UICheckBoxGroupProvider("Provider","All data providers");
        providerFilter.setToolTip("Choose a data provider from the drop-down list.");

        filterCollection.setOptionalFilters(Arrays.asList(nameFilter, drillingDateFilter, providerFilter));
        return filterCollection;
    }
}
