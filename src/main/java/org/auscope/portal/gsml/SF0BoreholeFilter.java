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

	protected Boolean justNVCL;
	protected List<String> identifiers;
    // ----------------------------------------------------------- Constructors

    public SF0BoreholeFilter() {
        // test
        super(null, null, null, null);
    }


    public SF0BoreholeFilter(String boreholeName, String custodian, String dateOfDrilling, List<String> ids, List<String> identifiers,  Boolean justNVCL) {
        super(boreholeName, custodian, dateOfDrilling, ids);
        this.justNVCL = justNVCL;
        this.identifiers = identifiers;
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
            parameterFragments.add(generateOrComparisonFragment(
            		this.generatePropertyIsLikeFragment("gsmlp:drillStartDate", "*" +  this.dateOfDrilling + "*"),
            		this.generatePropertyIsLikeFragment("gsmlp:drillEndDate", "*" +  this.dateOfDrilling + "*")));
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


        if (this.identifiers != null && !this.identifiers.isEmpty()) {
            List<String> compareFragments = new ArrayList<String>();
            for (String identifier : identifiers) {
                if (identifier != null && identifier.length() > 0) {
                    compareFragments.add(this.generatePropertyIsEqualToFragment("gsmlp:identifier", identifier));
                }
            }

            parameterFragments.add(this
                    .generateOrComparisonFragment(compareFragments
                            .toArray(new String[compareFragments.size()])));
        }

        if (this.justNVCL != null && this.justNVCL==true) {
        	parameterFragments.add(this.generatePropertyIsEqualToFragment("gsmlp:nvclCollection", "true"));
        }

        return this.generateAndComparisonFragment(this
                .generateAndComparisonFragment(parameterFragments
                        .toArray(new String[parameterFragments.size()])));
    }
}
