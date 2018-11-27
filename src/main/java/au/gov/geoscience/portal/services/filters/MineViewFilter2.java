package au.gov.geoscience.portal.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.Set;

public class MineViewFilter2 extends AbstractFilter {

    private static String NAME = "erl:name";
    private static String STATUSURI = "erl:status_uri";
    private static String SHAPE = "erl:shape";

    public MineViewFilter2(String name, Set<String> statusUris, FilterBoundingBox bbox) {

        if (name !=null && !name.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, name));
        }

        if (statusUris !=null && !statusUris.isEmpty()) {
            filterList.add(generateOrFilter(STATUSURI, statusUris));
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        this.filter = filterFactory.and(filterList);
    }

}
