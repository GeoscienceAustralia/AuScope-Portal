package au.gov.geoscience.portal.server.services.filters;


import org.auscope.portal.core.test.ResourceUtil;
import org.junit.Assert;


import org.auscope.portal.core.test.PortalTestClass;
import au.gov.geoscience.portal.server.ogc.AbstractFilterTestUtilities;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * User: Mathew Wyatt Date: 25/03/2009 Time: 8:23:09 AM
 */
public class TestMineFilter extends PortalTestClass {

    /**
     * Test without mine name. If there is no name specified then all of the mines should be queried.
     */
    @Test
    public void testWithNoMineName() throws Exception {
        final String xmlFilter = ResourceUtil.loadResourceAsString("au/gov/geoscience/portal/server/services/filters/mineWithNoName.xml").replace(System.lineSeparator(),"");
        MineFilter mineFilter = new MineFilter("");
    
        String filter = mineFilter.getFilterStringAllRecords();
        Assert.assertTrue(xmlStringEquals(
                xmlFilter,
                filter,
                false));
    }

    /**
     * Test with a mine name. A filter query should be generated searching for mines with the given name.
     */
    @Test
    public void testWithAMineName() throws Exception {
        MineFilter mineFilter = new MineFilter("Dominion Copper Mine");

        String filter = mineFilter.getFilterStringAllRecords();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(filter);

        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:PropertyName",
                new String[] {"er:specification/er:Mine/er:mineName/er:MineName/er:mineName"}, 1);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:Literal",
                new String[] {"*Dominion Copper Mine*"}, 1);

    }

    /**
     * Tests to ensure match case is set to false
     */
    @Test
    public void testMatchCaseDefault() throws Exception {
        MineFilter mineFilter = new MineFilter("mineNameToTest");

        String filter = mineFilter.getFilterStringAllRecords();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(filter);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/@matchCase",
                new String[] {"false"}, 1);
    }

}
