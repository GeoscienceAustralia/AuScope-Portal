package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.test.PortalTestClass;
import au.gov.geoscience.portal.server.ogc.AbstractFilterTestUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

public class TestMinOccurViewFilter extends PortalTestClass {

    /**
     * Test with commodity
     * 
     * @throws Exception
     */
    @Test
    public void testMinOccurFilter() throws Exception {
        MinOccViewFilter filter = new MinOccViewFilter("abc");

        String result = filter.getFilterStringAllRecords();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(result);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:Literal",
                new String[] {"*abc*"}, 1);

    }

    /**
     * Test without commodity. Should return a empty string.
     * 
     * @throws Exception
     */
    @Test
    public void testEmptyComoodityFilter() throws Exception {
        MinOccViewFilter filter = new MinOccViewFilter("");

        String result = filter.getFilterStringAllRecords();
        Assert.assertTrue(result.isEmpty());

    }
}
