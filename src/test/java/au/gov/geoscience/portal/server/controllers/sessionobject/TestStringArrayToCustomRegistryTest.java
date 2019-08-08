package au.gov.geoscience.portal.server.controllers.sessionobject;

import org.auscope.portal.core.services.csw.custom.CustomRegistryInt;
import org.auscope.portal.core.test.PortalTestClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStringArrayToCustomRegistryTest extends PortalTestClass {

    private StringArrayToCustomRegistry customRegistryConverter;

    @Before
    public void setUp(){
        this.customRegistryConverter = new StringArrayToCustomRegistry();
    }

    @Test
    public void testConvert() {
        String[] source = new String[] {"1234", "CSW", "http://portal.ga/csw", "http://portal.ga/csw/recordInfo" };
        CustomRegistryInt registry = customRegistryConverter.convert(source);

        Assert.assertEquals(registry.getId(), source[0]);
        Assert.assertEquals(registry.getTitle(), source[1]);
        Assert.assertEquals(registry.getServiceUrl(), source[2]);
        Assert.assertEquals(registry.getRecordInformationUrl(), source[3]);
    }

}