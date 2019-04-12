package au.gov.geoscience.portal.services.filters;


import au.gov.geoscience.portal.services.vocabularies.VocabularyLookup;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.Set;

public class CommodityResourceViewFilter2 extends AbstractFilter {

    private static final String MINERALOCCURRENCENAME = "erl:mineralOccurrenceName";
    private static final String COMMODITYURI = "erl:commodityClassifier_uri";
    private static final String SHAPE = "erl:shape";

    private static final String RESERVESCATEGORYURI = "erl:reservesCategory_uri";
    private static final String RESOURCESCATEGORYURI = "erl:resourcesCategory_uri";

    public CommodityResourceViewFilter2(String mineralOccurrenceName, Set<String> commodityUris, String jorcCategoryUri, FilterBoundingBox bbox) {

        if (mineralOccurrenceName != null && !mineralOccurrenceName.isEmpty()) {
            this.filterList.add(generateLikeFilter(MINERALOCCURRENCENAME,mineralOccurrenceName));
        }

        if (commodityUris != null && !commodityUris.isEmpty()) {
            this.filterList.add(generateOrFilter(COMMODITYURI, commodityUris)) ;
        }

        if (bbox != null) {
            filterList.add(generateBBOXFilter(SHAPE, bbox));
        }

        generateJorcFilters(jorcCategoryUri);

        filter = filterFactory.and(filterList);
    }

    /**
     * @param jorcCategoryUri
     */
    private void generateJorcFilters(String jorcCategoryUri) {
        if (jorcCategoryUri != null && !jorcCategoryUri.isEmpty()) {
            if (jorcCategoryUri.startsWith(VocabularyLookup.RESERVE_CATEGORY.uri())) {
                if (jorcCategoryUri.equals(VocabularyLookup.RESERVE_CATEGORY.uri())) {
                    filterList.add(generateNotNullFilter(RESERVESCATEGORYURI));
                } else {
                    filterList.add(generateEqualFilter(RESERVESCATEGORYURI, jorcCategoryUri));
                }
            } else if (jorcCategoryUri.startsWith(VocabularyLookup.RESOURCE_CATEGORY.uri())) {
                if (jorcCategoryUri.equals(VocabularyLookup.RESOURCE_CATEGORY.uri())) {
                    filterList.add(generateNotNullFilter(RESOURCESCATEGORYURI));
                } else {
                    filterList.add(generateEqualFilter(RESOURCESCATEGORYURI, jorcCategoryUri));
                }
            }
        }
    }
}
