package au.gov.geoscience.portal.xslt;

import org.auscope.portal.core.test.PortalTestClass;
import org.auscope.portal.core.test.ResourceUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestWfsToCsvTransformer extends PortalTestClass {

    private WfsToCsvTransformer wfsToCsvTransformer;

    @Before
    public void setup() {
        this.wfsToCsvTransformer = new WfsToCsvTransformer();
    }

    /**
     * Ensures the transformation occurs with no errors
     */
    @Test
    public void testNoErrors() throws Exception {
        final String wfs = ResourceUtil.loadResourceAsString("org/auscope/portal/erml/mine/mineGetFeatureResponse.xml");
        final String serviceUrl = "http://example.org/wfs";

        final String response = wfsToCsvTransformer.convert(wfs, serviceUrl);
        Assert.assertNotNull(response);
        Assert.assertFalse(response.isEmpty());
    }
}