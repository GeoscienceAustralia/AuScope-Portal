package au.gov.geoscience.portal.services.filters;


import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.Set;

public class MineralOccurrenceViewFilter2 extends AbstractFilter {

    private static String NAME = "erl:name";
    private static String COMMODITYURI = "erl:representativeCommodity_uri";
    private static String TIMESCALEURI = "erl:representativeAge_uri";

    private static String SHAPE = "erl:shape";

    public MineralOccurrenceViewFilter2(String name, Set<String> commodityUris, Set<String> timescaleUris, FilterBoundingBox bbox) {

        if (name !=null && !name.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, name));
        }

        if (commodityUris != null && !commodityUris.isEmpty()) {
            filterList.add(generateOrFilter(COMMODITYURI, commodityUris));
        }

        if (timescaleUris != null && !timescaleUris.isEmpty()) {
            filterList.add(generateOrFilter(TIMESCALEURI, timescaleUris));
        }

        if (bbox != null) {

            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        if (!filterList.isEmpty()) {
            this.filter = filterFactory.and(filterList);
        }
     }

}
