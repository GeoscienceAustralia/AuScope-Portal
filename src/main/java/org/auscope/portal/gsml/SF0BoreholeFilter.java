package org.auscope.portal.gsml;

import java.util.ArrayList;
import java.util.List;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.springframework.stereotype.Service;

/**
 * A class for filter SF0 Borehole web service
 * 
 * @author Florence Tan
 *
 */
@Service
public class SF0BoreholeFilter extends BoreholeFilter {

    // ----------------------------------------------------------- Constructors

    public SF0BoreholeFilter() {
        // test
        super(null, null, null, null);
    }

    public SF0BoreholeFilter(String boreholeName, String custodian, String dateOfDrilling, List<String> ids) {
        super(boreholeName, custodian, dateOfDrilling, ids);
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public String getFilterStringAllRecords() {
        return this.generateFilter(this.generateFilterFragment());
    }

    @Override
    public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

        return this
                .generateFilter(this.generateAndComparisonFragment(
                        this.generateBboxFragment(bbox,
                                "gsmlp:shape"),
                        this.generateFilterFragment()));
    }

    @Override
    protected String generateFilterFragment() {
        List<String> parameterFragments = new ArrayList<String>();
        if (boreholeName != null && !boreholeName.isEmpty()) {
            parameterFragments.add(this.generatePropertyIsLikeFragment(
                    "gsmlp:name", "*" + this.boreholeName + "*"));
        }

        if (custodian != null && !custodian.isEmpty()) {
            parameterFragments
                    .add(this
                            .generatePropertyIsLikeFragment(
                                    "gsmlp:boreholeMaterialCustodian",
                                    this.custodian));
        }

        if (dateOfDrilling != null && !dateOfDrilling.isEmpty()) {
            parameterFragments.add(this.generatePropertyIsLikeFragment(
                    "gsmlp:drillStartDate",
                    "*" +  this.dateOfDrilling + "*"));
        }

        if (this.restrictToIDList != null && !this.restrictToIDList.isEmpty()) {
            List<String> idFragments = new ArrayList<String>();
            for (String id : restrictToIDList) {
                if (id != null && id.length() > 0) {
                    idFragments.add(this.generateFeatureIdFragment("gsml.borehole." + id));
                }
            }
            parameterFragments.add(this
                    .generateOrComparisonFragment(idFragments
                            .toArray(new String[idFragments.size()])));
        }

        return this.generateAndComparisonFragment(this
                .generateAndComparisonFragment(parameterFragments
                        .toArray(new String[parameterFragments.size()])));
    }
}
