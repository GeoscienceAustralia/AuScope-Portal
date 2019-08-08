package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;

public class MineFilter extends AbstractFilter {
    List<String> fragments;

    public MineFilter(String mineName) {
        this(mineName, null);
    }

    public MineFilter(String mineName, String status) {
        fragments = new ArrayList<String>();

        if (mineName != null && !mineName.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment(
                    "er:specification/er:Mine/er:mineName/er:MineName/er:mineName",  mineName ));

        } else {
            String nullProperty = this.generatePropertyIsNull("er:specification/er:Mine/er:mineName/er:MineName/er:mineName");
            fragments.add(this.generateNotComparisonFragment(nullProperty));
        }

        if (status != null && !status.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("er:specification/er:Mine/er:status", status));
        }
        
    }

    @Override
    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
    }

    @Override
    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {
        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateBboxFragment(bbox, "er:location"));

        return this.generateFilter(
                this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
    }

}
