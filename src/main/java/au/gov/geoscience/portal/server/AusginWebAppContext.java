package au.gov.geoscience.portal.server;

import java.util.Arrays;

import org.auscope.portal.core.server.PortalProfileXmlWebApplicationContext;

public class AusginWebAppContext extends PortalProfileXmlWebApplicationContext {
    @Override
    protected String[] getDefaultConfigLocations() {

        // Necessary to get GeoTools to work with our Saxon parser

        System.setProperty("javax.xml.transform.TransformerFactory","org.apache.xalan.processor.TransformerFactoryImpl");
        String[] locations = super.getDefaultConfigLocations();

        //VT --perform some initalizing sequence. Could not find a more suitable place for this
        Initializer init = new Initializer();
        init.run();
        //--  end of initialization

        String[] ausginLocations = Arrays.copyOf(locations, locations.length + 4);
        ausginLocations[ausginLocations.length - 1] = DEFAULT_CONFIG_LOCATION_PREFIX + "ausgin-known-layers.xml";
        ausginLocations[ausginLocations.length - 2] = DEFAULT_CONFIG_LOCATION_PREFIX + "ausgin-registries.xml";
        ausginLocations[ausginLocations.length - 3] = DEFAULT_CONFIG_LOCATION_PREFIX + "ausgin-vocabularies.xml";
        ausginLocations[ausginLocations.length - 4] = DEFAULT_CONFIG_LOCATION_PREFIX + "service-level-config.xml";

        return ausginLocations;
    }
}
