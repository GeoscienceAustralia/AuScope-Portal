package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class GeologicalProvinces {

    private static final String GROUP = "Geological Provinces";
    private static final int GROUP_ORDER = GroupOrder.GEOLOGICAL_PROVINCES;
    private static final String PROXY_DOWNLOAD_URL = "geologicalProvincesDownload.do";
    private static final String NAGIOS_HOST_GROUP = "GeoscienceAustralia";

    private static final int FEATURE_COUNT = 100;

    @Bean(name = "knownTypeGeologicalProvinceContact")
    @Order(GROUP_ORDER)
    KnownLayer knownTypeGeologicalProvinceContact() {
        String id = "geologicalProvinceContacts";
        KnownLayerSelector selector = new WMSSelector("ProvinceContacts");

        String name = "Province Contacts";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Contacts (boundaries) of the full spatial extent of Australia's geological provinces. " +
                "Where possible, the contacts of provinces have been attributed with information about the source, " +
                "accuracy, and observation method of those lines.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeCenozoicPaleovalleys")
    @Order(GROUP_ORDER + 1)
    KnownLayer knownTypeCenozoicPaleovalleys() {
        String id = "cenozoicPaleovalleys";
        KnownLayerSelector selector = new WMSSelector("CenozoicPaleovalleys");

        String name = "Cenozoic Paleovalleys";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);

        String description = "Descriptions and spatial extents of Cenozoic paleovalleys of the western protion of the " +
                "Australian continent (Bell, J.G., et al, 2012. WASANT palaeovalley map - distribution of " +
                "palaeovalleys in arid and semi-arid WA-SA-NT). The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);
        ;
        ;
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeGeologicalProvinceMetallogenic")
    @Order(GROUP_ORDER + 2)
    KnownLayer knownTypeGeologicalProvinceMetallogenic() {
        String id = "geologicalProvinceMetallogenic";
        KnownLayerSelector selector = new WMSSelector("MetallogenicProvinces");

        String name = "Metallogenic Provinces";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);

        String description = "Descriptions and spatial extents of metallogenic provinces of the Australian continent.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsCenozoic")
    @Order(GROUP_ORDER + 3)
    KnownLayer knownTypeSedimentaryBasinsCenozoic() {
        String id = "sedimentaryBasinsCenozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-Cenozoic");

        String name = "Sedimentary Basins - Cenozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Cenozoic sedimentary basins of the Australian " +
                "continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including the extent " +
                "of a province under any overlying cover material) has been captured.";

        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsMesozoicCenozoic")
    @Order(GROUP_ORDER + 4)
    KnownLayer knownTypeSedimentaryBasinsMesozoicCenozoic() {
        String id = "sedimentaryBasinsMesozoicCenozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-MesozoicCenozoic");

        String name = "Sedimentary Basins - Mesozoic to Cenozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Mesozoic to Cenozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsMesozoic")
    @Order(GROUP_ORDER + 5)
    KnownLayer knownTypeSedimentaryBasinsMesozoic() {
        String id = "sedimentaryBasinsMesozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-Mesozoic");

        String name = "Sedimentary Basins - Mesozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Mesozoic sedimentary basins of the Australian " +
                "continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including the extent " +
                "of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsPaleozoicCenozoic")
    @Order(GROUP_ORDER + 6)
    KnownLayer knownTypeSedimentaryBasinsPaleozoicCenozoic() {
        String id = "sedimentaryBasinsPaleozoicCenozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-PaleozoicCenozoic");

        String name = "Sedimentary Basins - Paleozoic to Cenozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Paleozoic to Cenozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsPaleozoicMesozoic")
    @Order(GROUP_ORDER + 7)
    KnownLayer knownTypeSedimentaryBasinsPaleozoicMesozoic() {
        String id = "sedimentaryBasinsPaleozoicMesozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-PaleozoicMesozoic");

        String name = "Sedimentary Basins - Paleozoic to Mesozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Paleozoic to Mesozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsLatePaleozoic")
    @Order(GROUP_ORDER + 8)
    KnownLayer knownTypeSedimentaryBasinsLatePaleozoic() {
        String id = "sedimentaryBasinsLatePaleozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-LatePaleozoic");

        String name = "Sedimentary Basins - Late Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Late Paleozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";

        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsEarlyToLatePaleozoic")
    @Order(GROUP_ORDER + 9)
    KnownLayer knownTypeSedimentaryBasinsEarlyToLatePaleozoic() {
        String id = "sedimentaryBasinsEarlyToLatePaleozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-EarlyToLatePaleozoic");

        String name = "Sedimentary Basins - Early To Late Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Early to Late Paleozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsEarlyPaleozoic")
    @Order(GROUP_ORDER + 10)
    KnownLayer knownTypeSedimentaryBasinsEarlyPaleozoic() {
        String id = "sedimentaryBasinsEarlyPaleozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-EarlyPaleozoic");

        String name = "Sedimentary Basins - Early Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Early Paleozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);


        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeTectonicProvincesPaleozoicMesozoic")
    @Order(GROUP_ORDER + 11)
    KnownLayer knownTypeTectonicProvincesPaleozoicMesozoic() {
        String id = "tectonicProvincesPaleozoicMesozoic";
        KnownLayerSelector selector = new WMSSelector("TectonicProvinces-PaleozoicMesozoic");

        String name = "Tectonic Provinces - Paleozoic to Mesozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Paleozoic to Mesozoic tectonic provinces of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);


        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeTectonicProvincesNeoproterozoicPaleozoic")
    @Order(GROUP_ORDER + 12)
    KnownLayer knownTypeTectonicProvincesNeoproterozoicPaleozoic() {
        String id = "tectonicProvincesNeoproterozoicPaleozoic";
        KnownLayerSelector selector = new WMSSelector("TectonicProvinces-NeoproterozoicPaleozoic");

        String name = "Tectonic Provinces - Neoproterozoic to Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Neoproterozoic to Paleozoic tectonic provinces of " +
                "the Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";

        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsNeoproterozoicPaleozoic")
    @Order(GROUP_ORDER + 13)
    KnownLayer knownTypeSedimentaryBasinsNeoproterozoicPaleozoic() {
        String id = "sedimentaryBasinsNeoproterozoicPaleozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-NeoproterozoicPaleozoic");

        String name = "Sedimentary Basins - Neoproterozoic to Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Neoproterozoic to Paleozoic sedimentary basins of " +
                "the Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeIgneousProvincesProterozoicPaleozoic")
    @Order(GROUP_ORDER + 14)
    KnownLayer knownTypeIgneousProvincesProterozoicPaleozoic() {
        String id = "igneousProvincesProterozoicPaleozoic";
        KnownLayerSelector selector = new WMSSelector("IgneousProvinces-ProterozoicPaleozoic");

        String name = "Igneous Provinces - Proterozoic to Paleozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Proterozoic to Paleozoic igneous provinces of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsMesoproterozoic")
    @Order(GROUP_ORDER + 15)
    KnownLayer knownTypeSedimentaryBasinsMesoproterozoic() {
        String id = "sedimentaryBasinsMesoproterozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-Mesoproterozoic");

        String name = "Sedimentary Basins - Mesoproterozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Mesoproterozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including " +
                "the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeTectonicProvincesProterozoic")
    @Order(GROUP_ORDER + 16)
    KnownLayer knownTypeTectonicProvincesProterozoic() {
        String id = "tectonicProvincesProterozoic";
        KnownLayerSelector selector = new WMSSelector("TectonicProvinces-Proterozoic");

        String name = "Tectonic Provinces - Proterozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Proterozoic tectonic provinces of the Australian " +
                "continent and offshore surrounds. The full 2D spatial extent of provinces (ie, including the extent " +
                "of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsPaleoproterozoic")
    @Order(GROUP_ORDER + 17)
    KnownLayer knownTypeSedimentaryBasinsPaleoproterozoic() {
        String id = "sedimentaryBasinsPaleoproterozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-Paleoproterozoic");

        String name = "Sedimentary Basins - Paleoproterozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Paleoproterozoic sedimentary basins of the " +
                "Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeSedimentaryBasinsArcheanPaleoproterozoic")
    @Order(GROUP_ORDER + 18)
    KnownLayer knownTypeSedimentaryBasinsArcheanPaleoproterozoic() {
        String id = "sedimentaryBasinsArcheanPaleoproterozoic";
        KnownLayerSelector selector = new WMSSelector("SedimentaryBasins-ArcheanPaleoproterozoic");

        String name = "Sedimentary Basins - Archean to Paleoproterozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Archean to Paleoproterozoic sedimentary basins of " +
                "the Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeTectonicProvincesArcheanProterozoic")
    @Order(GROUP_ORDER + 19)
    KnownLayer knownTypeTectonicProvincesArcheanProterozoic() {
        String id = "tectonicProvincesArcheanProterozoic";
        KnownLayerSelector selector = new WMSSelector("TectonicProvinces-ArcheanProterozoic");

        String name = "Tectonic Provinces - Archean to Proterozoic";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of Archean to Proterozoic tectonic provinces basins " +
                "of the Australian continent and offshore surrounds. The full 2D spatial extent of provinces (ie, " +
                "including the extent of a province under any overlying cover material) has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeCrustalElements")
    @Order(GROUP_ORDER + 20)
    KnownLayer knownTypeCrustalElements() {
        String id = "crustalElements";
        KnownLayerSelector selector = new WMSSelector("CrustalElements");

        String name = "Crustal Elements";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "The Australian Crustal Elements data layer delineates major upper crustal elements, " +
                "primarily based on composite geophysical domains, each of which shows a distinctive pattern of " +
                "magnetic and gravity anomalies. These elements generally relate to older basement, rather than " +
                "the younger sedimentary basins.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }

    @Bean(name = "knownTypeAllGeologicalProvinces")
    @Order(GROUP_ORDER + 21)
    KnownLayer knownTypeAllGeologicalProvinces() {
        String id = "allGeologicalProvinces";
        KnownLayerSelector selector = new WMSSelector("AllProvinces");

        String name = "All Provinces";
        KnownLayer knownLayer = new KnownLayer(id, selector);
        knownLayer.setName(name);
        String description = "Descriptions and spatial extents of the fundamental geological elements of the " +
                "Australian continent and offshore surrounds. Province types include sedimentary basins, tectonic " +
                "provinces such as cratons and orogens, igneous provinces, and metallogenic provinces. The full 2D " +
                "spatial extent (ie, including the extent of a province under any overlying cover material) of " +
                "provinces has been captured.";
        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);
        knownLayer.setProxyDownloadUrl(PROXY_DOWNLOAD_URL);
        knownLayer.setSingleTile(true);

        knownLayer.setNagiosHostGroup(NAGIOS_HOST_GROUP);
        return knownLayer;
    }


}
