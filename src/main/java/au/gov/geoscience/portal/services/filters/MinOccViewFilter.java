package au.gov.geoscience.portal.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

public class MinOccViewFilter extends AbstractFilter {

    private static final String NAME = "mo:name";
    private static final String SHAPE = "mo:shape";

    public MinOccViewFilter (String name, FilterBoundingBox bbox) {
        if (name !=null && !name.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, name));
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        if (!filterList.isEmpty()) {
            this.filter = filterFactory.and(filterList);
        }
    }

}
