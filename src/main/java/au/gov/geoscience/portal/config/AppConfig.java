package au.gov.geoscience.portal.config;

import au.gov.geoscience.portal.xslt.WfsToCsvTransformer;
import org.auscope.portal.core.configuration.ServiceConfiguration;
import org.auscope.portal.core.configuration.ServiceConfigurationItem;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.server.http.download.FileDownloadService;
import org.auscope.portal.core.services.CSWCacheService;
import org.auscope.portal.core.services.CSWFilterService;
import org.auscope.portal.core.services.KnownLayerService;
import org.auscope.portal.core.services.OpendapService;
import org.auscope.portal.core.services.WCSService;
import org.auscope.portal.core.services.WMSService;
import org.auscope.portal.core.services.csw.CSWServiceItem;
import org.auscope.portal.core.services.methodmakers.OPeNDAPGetDataMethodMaker;
import org.auscope.portal.core.services.methodmakers.WCSMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WMSMethodMaker;
import org.auscope.portal.core.services.methodmakers.WMSMethodMakerInterface;
import org.auscope.portal.core.services.methodmakers.WMS_1_3_0_MethodMaker;
import org.auscope.portal.core.services.namespaces.WFSNamespaceContext;
import org.auscope.portal.core.view.ViewCSWRecordFactory;
import org.auscope.portal.core.view.ViewKnownLayerFactory;
import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.xslt.WfsToKmlTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"org.auscope.portal", "au.gov.geoscience.portal"})
@PropertySource("classpath:application-${env:nonprod}.properties")
public class AppConfig {

    private static int TIMEOUT = 900000;

    @Autowired
    List<CSWServiceItem> cswServiceItems;

    @Autowired
    List<KnownLayer> knownLayers;

    @Bean
    public ThreadPoolTaskExecutor threadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        return executor;
    }

    @Bean
    public HttpServiceCaller serviceCaller() {
        return new HttpServiceCaller(TIMEOUT);
    }


    @Bean(name = "cswCacheService")
    CSWCacheService cswCacheService() {
        ArrayList<CSWServiceItem> items = new ArrayList<>(cswServiceItems);
        CSWCacheService cswCacheService = new CSWCacheService(this.threadExecutor(), serviceCaller(), items);
        return cswCacheService;
    }

    @Bean(name = "cswFilterService")
    CSWFilterService cswFilterService() {
        ArrayList<CSWServiceItem> items = new ArrayList<>(cswServiceItems);
        return new CSWFilterService(this.threadExecutor(), serviceCaller(), items);
    }

    @Bean
    ViewCSWRecordFactory viewCSWRecordFactory() {
        return new ViewCSWRecordFactory();
    }

    @Bean
    ViewKnownLayerFactory viewKnownLayerFactory() {
        return new ViewKnownLayerFactory();
    }

    @Bean
    ServiceConfiguration serviceConfiguration() {
        ServiceConfigurationItem item = new ServiceConfigurationItem("test", "test", true);
        return new ServiceConfiguration(new ArrayList<>(Arrays.asList(item)));
    }

    @Bean
    KnownLayerService knownLayerService() {
        return new KnownLayerService(knownLayers, cswCacheService());
    }

    @Bean
    WFSGetFeatureMethodMaker wfsGetFeatureMethodMaker() {
        WFSGetFeatureMethodMaker methodMaker = new WFSGetFeatureMethodMaker();
        methodMaker.setNamespaces(new WFSNamespaceContext());
        return methodMaker;
    }

    @Bean
    WCSService wcsService() {

        WCSMethodMaker methodMaker = new WCSMethodMaker();

        return new WCSService(serviceCaller(), methodMaker);
    }

    @Bean
    OpendapService opendapService() {
        return new OpendapService(serviceCaller(), new OPeNDAPGetDataMethodMaker());
    }

    @Bean
    WMS_1_3_0_MethodMaker wms_1_3_0_methodMaker() {
        return new WMS_1_3_0_MethodMaker(serviceCaller());
    }

    @Bean
    WMSMethodMaker wmsMethodMaker() {
        return new WMSMethodMaker(serviceCaller());
    }

    @Bean
    WMSService wmsService() {
        List<WMSMethodMakerInterface> methodMakers = new ArrayList<>();
        methodMakers.add(wms_1_3_0_methodMaker());
        methodMakers.add(wmsMethodMaker());

        return new WMSService(serviceCaller(), methodMakers);
    }

    @Bean
    WfsToCsvTransformer wfsToCsvTransformer() {
        return new WfsToCsvTransformer();
    }

    @Bean
    WfsToKmlTransformer wfsToKmlTransformer() {
        return new WfsToKmlTransformer();
    }

    @Bean
    FileDownloadService fileDownloadService() {
        return new FileDownloadService(serviceCaller());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
