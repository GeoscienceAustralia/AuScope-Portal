package org.auscope.portal.mineraloccurrence;

import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

public class RockPropertiesFilter extends AbstractFilter {

	private String filterFragment;

	public RockPropertiesFilter(String rockProperty) {
		if (rockProperty != null && rockProperty.length() > 0) {
			this.filterFragment = this.generatePropertyIsEqualToFragment(
					"PROPERTY", rockProperty);
		}
		// Ensure a MFO query always returns a type mine!
		else {
			this.filterFragment = this.generatePropertyIsEqualToFragment(
					"PROPERTY", "");
		}
	}

	@Override
	public String getFilterStringAllRecords() {
		return this.generateFilter(this.filterFragment);
	}

	@Override
	public String getFilterStringBoundingBox(FilterBoundingBox bbox) {
		return this.generateFilter(this.generateAndComparisonFragment(this.generateBboxFragment(bbox, "GEOM"),
				this.filterFragment));
	}

}
