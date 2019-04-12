package au.gov.geoscience.portal.services.filters;


import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

public class MineFilter2 extends AbstractFilter {

    private static final String NAME = "er:specification/er:Mine/er:mineName/er:MineName/er:mineName";
    private static final String STATUS = "er:specification/er:Mine/er:status";
    private static final String SHAPE = "er:location";

    public MineFilter2(String mineName, String status, FilterBoundingBox bbox) {

        if (mineName != null && !mineName.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, mineName));
        } else {
            filterList.add(generateNotNullFilter(NAME));
        }

        if (status != null && !status.isEmpty()) {
            filterList.add(generateEqualFilter(STATUS, status));
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }


        this.filter = filterFactory.and(filterList);
    }
}
