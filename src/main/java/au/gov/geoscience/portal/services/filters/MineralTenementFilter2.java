package au.gov.geoscience.portal.services.filters;

import au.gov.geoscience.portal.services.filters.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.server.MineralTenementServiceProviderType;

public class MineralTenementFilter2 extends AbstractFilter {

    private static final String NAME = "mt:name";
    private static final String OWNER = "mt:owner";
    private static final String SHAPE = "mt:shape";

    public MineralTenementFilter2(String name, String owner, MineralTenementServiceProviderType mineralTenementServiceProviderType) {

        if (name != null && !name.isEmpty()) {
            filterList.add(generateLikeFilter(mineralTenementServiceProviderType.nameField(), name));
        }

        if (owner != null && !owner.isEmpty()) {
            filterList.add(generateLikeFilter(mineralTenementServiceProviderType.ownerField(), owner));
        }

        if (filterList.size() > 1) {
            filter = filterFactory.and(filterList);
        } else if (!filterList.isEmpty()) {
            filter = filterList.get(0);
        }


    }

    public MineralTenementFilter2(String name, String owner, FilterBoundingBox bbox) {

        if (name != null && !name.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, name));
        }

        if (owner != null && !owner.isEmpty()) {
            filterList.add(generateLikeFilter(OWNER, owner));
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        filter = filterFactory.and(filterList);

    }

}
