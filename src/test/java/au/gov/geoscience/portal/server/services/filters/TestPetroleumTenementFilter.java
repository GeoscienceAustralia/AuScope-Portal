package au.gov.geoscience.portal.server.services.filters;

import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import au.gov.geoscience.portal.server.ogc.AbstractFilterTestUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashSet;
import java.util.Set;

public class TestPetroleumTenementFilter {

    /**
     * Should return an empty string.
     *
     * @throws Exception
     */
    @Test
    public void testEmptyPetroleumTenementFilter() throws Exception {
        PetroleumTenementServiceProviderType serviceProviderType = PetroleumTenementServiceProviderType.GeoServer;
        PetroleumTenementFilter filter = new PetroleumTenementFilter("http://portal.ga/wfs", "", "", new HashSet<String>(), new HashSet<String>(), serviceProviderType);
        String result = filter.getFilterStringAllRecords();
        Assert.assertTrue(result.isEmpty());
    }

    /**
     * Should return an empty string.
     *
     * @throws Exception
     */
    @Test
    public void testAdditionalStyle() throws Exception {
        PetroleumTenementServiceProviderType serviceProviderType = PetroleumTenementServiceProviderType.GeoServer;
        Set<String> statusUris = new HashSet<>();
        statusUris.add("ghi");
        Set<String> tenementTypeUris = new HashSet<>();
        tenementTypeUris.add("jkl");
        PetroleumTenementFilter filter = new PetroleumTenementFilter("http://portal.ga/wfs", "abc", "def", statusUris, tenementTypeUris, serviceProviderType);
        String result = filter.getFilterWithAdditionalStyle();
        Document doc = AbstractFilterTestUtilities.parsefilterStringXML(result);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsLike/ogc:Literal", new String[]{"abc", "def"}, 2);
        AbstractFilterTestUtilities.runNodeSetValueCheck(doc, "/descendant::ogc:PropertyIsEqualTo/ogc:Literal", new String[]{"ghi", "jkl"}, 2);
    }
}
