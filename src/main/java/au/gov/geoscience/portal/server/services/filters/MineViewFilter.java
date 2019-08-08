package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A filter for erl:MineView features, allows filtering on name and status_uri,
 * with status_uri filtering available for a single value or a set.
 */
public class MineViewFilter extends AbstractFilter {
    List<String> fragments;

    /**
     * @param name
     * @param statusUri
     */
    public MineViewFilter(String name, String statusUri) {
        fragments = new ArrayList<String>();
        
        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:name", name ));
        }
        
        
        if (statusUri != null && !statusUri.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("erl:status_uri", statusUri));
        }
        
    }

    /**
     * @param name
     * @param statusUris
     */
    public MineViewFilter(String name, Set<String> statusUris) {
        fragments = new ArrayList<String>();

        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:name", name ));
        }

        if (statusUris != null && !statusUris.isEmpty()) {
            List<String> localFragments = new ArrayList<String>();
            for (String statusUri : statusUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("erl:status_uri", statusUri));
            }
            fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
        }
    }

    @Override
    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
    }

    @Override
    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {
        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateBboxFragment(bbox, "erl:shape"));

        return this.generateFilter(
                this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
    }

}
