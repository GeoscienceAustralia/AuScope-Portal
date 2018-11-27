package au.gov.geoscience.portal.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

public class MiningActivityFilter extends AbstractFilter{

    private static final String MINING_ACTIVITY = "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/gml:name";

    private static final String NAME = "er:specification/er:Mine/er:mineName/er:MineName/er:mineName";
    private static final String SHAPE = "er:location";

    public MiningActivityFilter(String mineName, FilterBoundingBox bbox) {
        filterList.add(generateNotNullFilter(MINING_ACTIVITY));

        if (mineName != null && !mineName.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, mineName));
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        this.filter = filterFactory.and(filterList);
    }
}
