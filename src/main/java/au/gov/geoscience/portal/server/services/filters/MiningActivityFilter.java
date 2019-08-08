package au.gov.geoscience.portal.server.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathew Wyatt
 *
 * @version $Id$
 */
public class MiningActivityFilter extends AbstractFilter {
    private String mineName;
    private String startDate;
    private String endDate;
    private String oreProcessed;
    private String producedMaterial;
    private String cutOffGrade;
    private String production;

    // -------------------------------------------------------------- Constants

    // ----------------------------------------------------------- Constructors

    public MiningActivityFilter(String mineName,
                                String startDate,
                                String endDate,
                                String oreProcessed,
                                String producedMaterial,
                                String cutOffGrade,
                                String production) {
        this.mineName = mineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.oreProcessed = oreProcessed;
        this.producedMaterial = producedMaterial;
        this.cutOffGrade = cutOffGrade;
        this.production = production;
    }

    public MiningActivityFilter(String mineName) {
        this.mineName = mineName;
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateFilterFragment());
    }

    @Override
    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

        return this.generateFilter(
                this.generateAndComparisonFragment(
                        this.generateBboxFragment(bbox, "er:location"),
                        this.generateFilterFragment()));
    }

    // -------------------------------------------------------- Private Methods
    private String generateFilterFragment() {
        List<String> mineFragments = new ArrayList<String>();
        List<String> parameterFragments = new ArrayList<String>();

        String filterFragment = this.generatePropertyIsLikeFragment(
                "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/gml:name", "*");

        if (this.mineName != null && this.mineName.length() > 0)
            mineFragments.add(this.generatePropertyIsLikeFragment(
                    "er:specification/er:Mine/er:mineName/er:MineName/er:mineName", this.mineName));

        if (this.startDate != null && this.startDate.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsGreaterThanOrEqualTo(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:activityDuration/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition",
                                    this.startDate));

        if (this.endDate != null && this.endDate.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsLessThanOrEqualTo(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:activityDuration/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition",
                                    this.endDate));

        if (this.oreProcessed != null && this.oreProcessed.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsGreaterThan(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:oreProcessed/gsml:CGI_NumericValue/gsml:principalValue",
                                    this.oreProcessed));

        if (this.producedMaterial != null && this.producedMaterial.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsEqualToFragment(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:producedMaterial/er:Product/er:productName/gsml:CGI_TermValue/gsml:value",
                                    this.producedMaterial));

        if (this.cutOffGrade != null && this.cutOffGrade.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsGreaterThan(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:producedMaterial/er:Product/er:grade/gsml:CGI_NumericValue/gsml:principalValue",
                                    this.cutOffGrade));

        if (this.production != null && this.production.length() > 0)
            parameterFragments
                    .add(this
                            .generatePropertyIsGreaterThan(
                                    "er:specification/er:Mine/er:relatedActivity/er:MiningActivity/er:producedMaterial/er:Product/er:production/gsml:CGI_NumericValue/gsml:principalValue",
                                    this.production));

        return this.generateAndComparisonFragment(
                filterFragment,
                this.generateOrComparisonFragment(mineFragments.toArray(new String[mineFragments.size()])),
                this.generateAndComparisonFragment(parameterFragments.toArray(new String[parameterFragments.size()])));
    }
}
