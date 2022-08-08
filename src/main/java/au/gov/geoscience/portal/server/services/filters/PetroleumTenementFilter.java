package au.gov.geoscience.portal.server.services.filters;
import au.gov.geoscience.portal.server.PetroleumTenementServiceProviderType;
import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents ogc:Filter markup for pt:petroleumTenement queries
 *
 * based on MineralTenement filter by Victor Tey
 */
public class PetroleumTenementFilter extends AbstractFilter {
    List<String> fragments;

	/**
	 * 
	 * Utility constructor that takes a given tenement name, and builds a filter to wild card
	 * search for tenement names.
	 * 
	 * @param tenementName
	 *            the name of the tenement
	 */
	public PetroleumTenementFilter(String tenementName) {
		this(tenementName, null, null);
	}

	
	/**
	 * 
	 * Utility constructor that takes a given tenement name and tenement holder and builds a filter to wild card
	 * search for tenement names.
	 * 
	 * @param tenementName
	 *            the name of the tenement
	 *  @param holder
	 *  		  the name of the tenement holder           
	 *            
	 */
	public PetroleumTenementFilter(String tenementName, String holder) {
		this(tenementName, holder, null);
	}
	
	/**
	 * Given required parameters, this object will build a filter to wild card
	 * for these parameters
	 *
	 * @param tenementName
	 *            the name of the tenement
	 * @param holder
	 *            holder of tenement
	 * @param petroleumTenementServiceProviderType
	 *            enum for differentiating service providers for Petroleum Tenement services
	 */
	public PetroleumTenementFilter(String tenementName, String holder, PetroleumTenementServiceProviderType petroleumTenementServiceProviderType) {
		if (petroleumTenementServiceProviderType == null) {
			petroleumTenementServiceProviderType = PetroleumTenementServiceProviderType.GeoServer;
		}
		fragments = new ArrayList<String>();
		if (tenementName != null && !tenementName.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(petroleumTenementServiceProviderType.nameField(), tenementName ));
		}

		if (holder != null && !holder.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(petroleumTenementServiceProviderType.holderField(), holder));
		}
		
	}

    public String getFilterStringAllRecords() {
		return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
	}

	public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

		List<String> localFragment = new ArrayList<String>(fragments);
		if (bbox != null) {
			localFragment.add(this.generateBboxFragment(bbox, "pt:shape"));
		}
		return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
	}

	public String getFilterWithAdditionalStyle() {

		List<String> localFragment = new ArrayList<String>(fragments);

		return this.generateFilter(this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
	}

}
