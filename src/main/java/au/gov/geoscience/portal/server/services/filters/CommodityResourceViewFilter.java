package au.gov.geoscience.portal.server.services.filters;

import au.gov.geoscience.portal.server.services.vocabularies.VocabularyLookup;
import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CommodityResourceViewFilter extends AbstractFilter {

    List<String> fragments;

    /**
     * @param mineralOccurrenceName
     * @param commodityUri
     * @param jorcCategoryUri
     */
    public CommodityResourceViewFilter(String mineralOccurrenceName, String commodityUri, String jorcCategoryUri) {


        fragments = new ArrayList<String>();

        if (mineralOccurrenceName != null && !mineralOccurrenceName.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:mineralOccurrenceName", mineralOccurrenceName));
        }

        if (commodityUri != null && !commodityUri.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("erl:commodityClassifier_uri", commodityUri));
        }

        generateJorcFragments(jorcCategoryUri);

    }

    /**
     * @param mineralOccurrenceName
     * @param commodityUris
     * @param jorcCategoryUri
     */
    public CommodityResourceViewFilter(String mineralOccurrenceName, Set<String> commodityUris, String jorcCategoryUri) {
        fragments = new ArrayList<String>();

        if (mineralOccurrenceName != null && !mineralOccurrenceName.isEmpty()) {
            fragments.add(this.generatePropertyIsLikeFragment("erl:mineralOccurrenceName",
                     mineralOccurrenceName));
        }

        if (commodityUris != null && !commodityUris.isEmpty()) {
            List<String> localFragments = new ArrayList<String>();
            for (String commodityUri : commodityUris) {
                localFragments.add(this.generatePropertyIsEqualToFragment("erl:commodityClassifier_uri", commodityUri));
            }
            fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
        }

        generateJorcFragments(jorcCategoryUri);
    }

    /**
     * @param jorcCategoryUri
     */
    private void generateJorcFragments(String jorcCategoryUri) {
        String propertyIsNull;
        String notNull;

        if (jorcCategoryUri != null && !jorcCategoryUri.isEmpty()) {
            if (jorcCategoryUri.startsWith(VocabularyLookup.RESERVE_CATEGORY.uri())) {
                if (jorcCategoryUri.equals(VocabularyLookup.RESERVE_CATEGORY.uri())) {
                    propertyIsNull = this.generatePropertyIsNull("erl:reservesCategory_uri");
                    notNull = this.generateNotComparisonFragment(propertyIsNull);
                    fragments.add(notNull);
                } else {
                    fragments.add(this.generatePropertyIsEqualToFragment("erl:reservesCategory_uri", jorcCategoryUri));
                }
            } else if (jorcCategoryUri.startsWith(VocabularyLookup.RESOURCE_CATEGORY.uri())) {
                if (jorcCategoryUri.equals(VocabularyLookup.RESOURCE_CATEGORY.uri())) {
                    propertyIsNull = this.generatePropertyIsNull("erl:resourcesCategory_uri");
                    notNull = this.generateNotComparisonFragment(propertyIsNull);
                    fragments.add(notNull);
                } else {
                    fragments.add(this.generatePropertyIsEqualToFragment("erl:resourcesCategory_uri", jorcCategoryUri));
                }

            }
        }
    }

    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
    }

    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {
        List<String> localFragment = new ArrayList<String>(fragments);
        localFragment.add(this.generateBboxFragment(bbox, "erl:shape"));

        return this.generateFilter(
                this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
    }

}
