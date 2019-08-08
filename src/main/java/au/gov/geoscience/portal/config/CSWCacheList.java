package au.gov.geoscience.portal.config;

import org.auscope.portal.core.services.csw.CSWServiceItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class CSWCacheList {

    @Order(1)
    @Bean
    public CSWServiceItem cswGeoscienceAll() {
        String id = "cswGeoscienceAll";
        String title = "Catalog for the AusGIN Portal";
        String serviceUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/csw";
        String recordInformationUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setHideFromCatalogue(true);
        return serviceItem;

    }

    @Order(2)
    @Bean
    public CSWServiceItem cswGARegistry() {
        String id = "cswGARegistry";
        String title = "Geoscience Australia eCat Catalog";
        String serviceUrl = "https://ecat.ga.gov.au/geonetwork/srv/eng/csw";
        String recordInformationUrl = "http://ecat.ga.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }

    @Order(3)
    @Bean
    public CSWServiceItem cswGsq() {
        String id = "cswGsq";
        String title = "Geological Survey of Queensland";
        String serviceUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/csw-gsq";
        String recordInformationUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }

    @Order(4)
    @Bean
    public CSWServiceItem cswMineralResourcesTasmaniaDev() {
        String id = "cswMineralResourcesTasmaniaDev";
        String title = "Mineral Resources Tasmania";
        String serviceUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/csw-tas";
        String recordInformationUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }

    @Order(5)
    @Bean
    public CSWServiceItem cswGsv() {
        String id = "cswGsv";
        String title = "Geological Survey of Victoria";
        String serviceUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/csw-gsv";
        String recordInformationUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }

    @Order(6)
    @Bean
    public CSWServiceItem cswGswa() {
        String id = "cswGswa";
        String title = "Geological Survey of Western Australia";
        String serviceUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/csw-gswa";
        String recordInformationUrl = "http://catalog.geoscience.gov.au/geonetwork/srv/eng/main.home?uuid=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }

    @Order(7)
    @Bean
    public CSWServiceItem cswEnvironment() {
        String id = "cswEnvironment";
        String title = "Australian Department of Environment";
        String serviceUrl = "http://www.environment.gov.au/fed/csw";
        String recordInformationUrl = "http://www.environment.gov.au/fed/csw?request=GetRecordById&amp;service=CSW&amp;version=2.0.2&amp;outputSchema=http://www.isotc211.org/2005/gmd&amp;elementSetName=full&amp;id=%1$s";
        CSWServiceItem serviceItem = new CSWServiceItem(id, serviceUrl, recordInformationUrl, title);
        serviceItem.setNoCache(true);
        return serviceItem;
    }


}


