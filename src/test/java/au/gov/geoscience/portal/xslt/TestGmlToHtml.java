package au.gov.geoscience.portal.xslt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import au.gov.geoscience.portal.xslt.GmlToHtml;
import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGmlToHtml extends PortalTestClass {

    private GmlToHtml gmlToHtml;

    @Before
    public void setup() {
        this.gmlToHtml = new GmlToHtml();
    }

    /**
     * Ensures the transformation occurs with no errors
     */
    @Test
    public void testNoErrors() throws Exception {
        final String wfs = ResourceUtil.loadResourceAsString("org/auscope/portal/erml/mine/mineGetFeatureResponse.xml");
        final String serviceUrl = "http://example.org/wfs";

        final String response = gmlToHtml.convert(wfs, serviceUrl);
        Assert.assertNotNull(response);
        Assert.assertFalse(response.isEmpty());
    }
}
