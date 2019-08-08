package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class MineralOccurrenceViewFilter extends AbstractFilter {
    List<String> fragments;

    /**
     * @param name
     * @param commodityUri
     * @param timescaleUri
     */
    public MineralOccurrenceViewFilter(String name, String commodityUri, String timescaleUri) {
        fragments = new ArrayList<String>();

        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:name", name ));
        }

        if (commodityUri != null && !commodityUri.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("erl:representativeCommodity_uri", commodityUri));
        }

        if (timescaleUri != null && !timescaleUri.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("erl:representativeAge_uri", timescaleUri));
        }
    }

    /**
     * @param name
     * @param commodityUris
     * @param timescaleUris
     */
    public MineralOccurrenceViewFilter(String name, Set<String> commodityUris, Set<String> timescaleUris) {
        fragments = new ArrayList<String>();

        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:name",  name ));
        }

        if (commodityUris != null && !commodityUris.isEmpty()) {
            List<String> localFragments = new ArrayList<String>();
            for (String commodityUri : commodityUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("erl:representativeCommodity_uri", commodityUri));
            }
            fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
        }

        if (timescaleUris != null && !timescaleUris.isEmpty()) {
            List<String> localFragments = new ArrayList<String>();
            for (String timescaleUri : timescaleUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("erl:representativeAge_uri", timescaleUri));
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
