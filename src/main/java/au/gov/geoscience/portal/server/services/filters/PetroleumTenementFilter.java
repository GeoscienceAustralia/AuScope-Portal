package au.gov.geoscience.portal.server.services.filters;

import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class that represents ogc:Filter markup for pt:petroleumTenement queries
 * Based on MineralTenement filter by Victor Tey
 */
public class PetroleumTenementFilter extends AbstractFilter {
    List<String> fragments;

    /**
     * Utility constructor that takes a given tenement name, and builds a filter to wild card
     * search for tenement names.
     *
     * @param name - the name of the tenement
     */
    public PetroleumTenementFilter(String name) {
        this(name, null, null, null);
    }

    /**
     * Utility constructor that takes a given tenement name and tenement holder and builds a filter to wild card
     * search for tenement names.
     *
     * @param name   - the name of the tenement
     * @param holder - the name of the tenement holder
     */
    public PetroleumTenementFilter(String name, String holder) {
        this(name, holder, null, null);
    }

    /**
     * @param name             - the name of the tenement
     * @param holder           - holder of tenement
     * @param statusUris       - status of tenement
     * @param tenementTypeUris - type of tenement
     */
    public PetroleumTenementFilter(String name, String holder, Set<String> statusUris, Set<String> tenementTypeUris) {
        PetroleumTenementServiceProviderType petroleumTenementServiceProviderType = PetroleumTenementServiceProviderType.GeoServer;
        fragments = new ArrayList<String>();
        if (name != null && !name.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment(petroleumTenementServiceProviderType.nameField(), name));
        }
        if (holder != null && !holder.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment(petroleumTenementServiceProviderType.holderField(), holder));
        }
        if (statusUris != null && !statusUris.isEmpty() && statusUris.size() > 1) {
            List<String> localFragments = new ArrayList<String>();
            for (String statusUri : statusUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("pt:status_uri", statusUri));
            }
            fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
        } else if (statusUris != null && statusUris.size() == 1) {
            for (String statusURI : statusUris) {
                fragments.add(this.generatePropertyIsEqualToFragment("pt:status_uri", statusURI));
            }
        }
        if (tenementTypeUris != null && !tenementTypeUris.isEmpty() && tenementTypeUris.size() > 1) {
            List<String> localFragments = new ArrayList<String>();
            for (String typeUri : tenementTypeUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("pt:tenementType_uri", typeUri));
            }
            fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
        } else if (tenementTypeUris != null && tenementTypeUris.size() == 1) {
            for (String tenementTypeURI : tenementTypeUris) {
                fragments.add(this.generatePropertyIsEqualToFragment("pt:tenementType_uri", tenementTypeURI));
            }
        }
    }

    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
    }

    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {
        List<String> localFragment = new ArrayList<String>(fragments);
        if (bbox != null) {
            localFragment.add(this.generateBboxFragment(bbox, "pt:shape"));
        }
        return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
    }

    public String getFilterWithAdditionalStyle() {
        List<String> localFragment = new ArrayList<String>(fragments);
        return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
    }
}
