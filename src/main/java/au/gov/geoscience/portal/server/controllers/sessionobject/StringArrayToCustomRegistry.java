package au.gov.geoscience.portal.server.controllers.sessionobject;

import org.auscope.portal.core.services.csw.custom.CustomRegistry;
import org.auscope.portal.core.services.csw.custom.CustomRegistryInt;
import org.springframework.core.convert.converter.Converter;

public class StringArrayToCustomRegistry implements Converter<String[], CustomRegistryInt> {

    @Override
    public CustomRegistryInt convert(String[] source) {
        return new CustomRegistry(source);
    }

}
