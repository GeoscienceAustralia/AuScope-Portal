package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents ogc:Filter markup for MinOccView queries
 *
 * @author Victor Tey
 * @version
 */
public class MinOccViewFilter extends AbstractFilter {
    List<String> fragments;

    /**
     * Given a mine name, this object will build a filter to a wild card search for mine names
     *
     * @param name
     *            the name
     */
    public MinOccViewFilter(String name) {

        fragments = new ArrayList<>();
        
        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("mo:name",  name));
        }
        


    }

    public String getFilterStringAllRecords() {

        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));

    }

    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateBboxFragment(bbox, "mo:shape"));

        return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment
                .size()])));
    }

}
