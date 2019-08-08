package au.gov.geoscience.portal.server.services.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tannu Gupta
 *
 * @version $Id$
 */

public class BoreholeFilter extends AbstractFilter {

	protected String boreholeName;
	protected String custodian;
	protected String dateOfDrilling;
	protected List<String> restrictToIDList;

	// -------------------------------------------------------------- Constants

	/** Log object for this class. */
	protected final Log logger = LogFactory.getLog(getClass());

	// ----------------------------------------------------------- Constructors

	public BoreholeFilter(String boreholeName, String custodian, String dateOfDrilling, List<String> restrictToIDList) {
		this.boreholeName = boreholeName;
		this.custodian = custodian;
		this.dateOfDrilling = dateOfDrilling;
		this.restrictToIDList = restrictToIDList;
	}

	// --------------------------------------------------------- Public Methods

	@Override
	public String getFilterStringAllRecords() {
		return this.generateFilter(this.generateFilterFragment());
	}

	@Override
	public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

		return this.generateFilter(this.generateAndComparisonFragment(
				this.generateBboxFragment(bbox, "gsml:collarLocation/gsml:BoreholeCollar/gsml:location"),
				this.generateFilterFragment()));
	}

	// -------------------------------------------------------- Private Methods
	protected String generateFilterFragment() {
		List<String> parameterFragments = new ArrayList<String>();
		if (boreholeName != null && !boreholeName.isEmpty()) {
			parameterFragments.add(this.generatePropertyIsLikeFragment("gml:name", "*" + this.boreholeName + "*"));
		}

		if (custodian != null && !custodian.isEmpty()) {
			parameterFragments.add(this.generatePropertyIsLikeFragment(
					"gsml:indexData/gsml:BoreholeDetails/gsml:coreCustodian/@xlink:title", this.custodian));
		}

		if (dateOfDrilling != null && !dateOfDrilling.isEmpty()) {
			try {
				int parsedDate = Integer.parseInt(dateOfDrilling);
				parameterFragments.add(this.generateAndComparisonFragment(
						this.generateDatePropertyIsGreaterThanOrEqualTo(
								"gsml:indexData/gsml:BoreholeDetails/gsml:dateOfDrilling", false,
								this.generateFunctionYearParse(parsedDate)),
						this.generateDatePropertyIsLessThan("gsml:indexData/gsml:BoreholeDetails/gsml:dateOfDrilling",
								false, this.generateFunctionYearParse(parsedDate + 1))));
			} catch (Exception ex) {
			}
		}

		if (this.restrictToIDList != null && !this.restrictToIDList.isEmpty()) {
			List<String> idFragments = new ArrayList<String>();
			for (String id : restrictToIDList) {
				if (id != null && id.length() > 0) {
					idFragments.add(generateFeatureIdFragment("gsml.borehole." + id));
				}
			}
			parameterFragments
					.add(this.generateOrComparisonFragment(idFragments.toArray(new String[idFragments.size()])));
		}

		return this.generateAndComparisonFragment(
				this.generateAndComparisonFragment(parameterFragments.toArray(new String[parameterFragments.size()])));
	}
}
