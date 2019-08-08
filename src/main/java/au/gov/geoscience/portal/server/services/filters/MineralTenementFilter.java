package au.gov.geoscience.portal.server.services.filters;

import au.gov.geoscience.portal.server.MineralTenementServiceProviderType;
import org.auscope.portal.core.services.methodmakers.filter.AbstractFilter;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class that represents ogc:Filter markup for mt:mineralTenement queries
 *
 * @author Victor Tey
 * @version
 */
public class MineralTenementFilter extends AbstractFilter {
	List<String> fragments;

	/**
	 * 
	 * Utility constructor that takes a given tenement name, and builds a filter to wild card
	 * search for tenement names.
	 * 
	 * @param tenementName
	 *            the name of the tenement
	 */
	public MineralTenementFilter(String tenementName) {
		this(tenementName, null, null, null, null, null);
	}

	
	/**
	 * 
	 * Utility constructor that takes a given tenement name and tenement owner and builds a filter to wild card
	 * search for tenement names.
	 * 
	 * @param tenementName
	 *            the name of the tenement
	 *  @param owner
	 *  		  the name of the tenemnet holder           
	 *            
	 */
	public MineralTenementFilter(String tenementName, String owner) {
		this(tenementName, null, owner, null, null, null);
	}
	
	/**
	 * Given required parameters, this object will build a filter to wild card
	 * for these parameters
	 *
	 * @param tenementName
	 *            the name of the tenement
	 * 
	 * @param tenementTypeUri
	 *            Type of tenement
	 * 
	 * @param owner
	 *            owner of tenement
	 * @param statusUri
	 *            status  of tenement
	 * @param endDate
	 *            Expiry date of tenement
	 */
	public MineralTenementFilter(String tenementName, String tenementTypeUri, String owner, String statusUri, String endDate, MineralTenementServiceProviderType mineralTenementServiceProviderType) {
		if (mineralTenementServiceProviderType == null) {
			mineralTenementServiceProviderType = MineralTenementServiceProviderType.GeoServer;
		}
		fragments = new ArrayList<String>();
		if (tenementName != null && !tenementName.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.nameField(), tenementName ));
		}
		if (tenementTypeUri != null && !tenementTypeUri.isEmpty()) {
			fragments.add(this.generatePropertyIsEqualToFragment("mt:tenementType_uri", tenementTypeUri));
		}
        if (statusUri != null && !statusUri.isEmpty()) {
            fragments.add(this.generatePropertyIsEqualToFragment("mt:status_uri", statusUri));
        }

		if (owner != null && !owner.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.ownerField(), owner));
		}

		if (endDate != null && !endDate.isEmpty()) {
			fragments.add(this.generatePropertyIsLessThanOrEqualTo("mt:expireDate", endDate));
		}
		

	}

	/**
	 * @param name
	 * @param owner
	 * @param statusUris
	 * @param typeUris
	 * @param mineralTenementServiceProviderType
	 */
    public MineralTenementFilter(String name, String owner, Set<String> statusUris, Set<String> typeUris, MineralTenementServiceProviderType mineralTenementServiceProviderType) {
		fragments = new ArrayList<String>();
    	if (name != null && !name.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.nameField(), name ));
		}

		if (owner != null && !owner.isEmpty()) {
			fragments.add(this.generatePropertyIsLikeFragment(mineralTenementServiceProviderType.ownerField(), owner));
		}

		if (statusUris != null && !statusUris.isEmpty()) {
			List<String> localFragments = new ArrayList<String>();
			for (String statusUri : statusUris) {
				localFragments.add(this.generatePropertyIsEqualToFragment("mt:status_uri", statusUri));
			}
			fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
		}

		if (typeUris != null && !typeUris.isEmpty()) {
			List<String> localFragments = new ArrayList<String>();
			for (String typeUri : typeUris) {
				localFragments.add(this.generatePropertyIsEqualToFragment("mt:tenementType_uri", typeUri));
			}
			fragments.add(this.generateOrComparisonFragment(localFragments.toArray(new String[localFragments.size()])));
		}

    }

    public String getFilterStringAllRecords() {
		return this.generateFilter(this.generateAndComparisonFragment(fragments.toArray(new String[fragments.size()])));
	}

	public String getFilterStringBoundingBox(FilterBoundingBox bbox) {

		List<String> localFragment = new ArrayList<String>(fragments);
		if (bbox != null) {
			localFragment.add(this.generateBboxFragment(bbox, "mt:shape"));
		}
		return this.generateFilter(
				this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
	}

	public String getFilterWithAdditionalStyle() {

		List<String> localFragment = new ArrayList<String>(fragments);
//		localFragment.add(this.generateOrComparisonFragment(this.generatePropertyIsLikeFragment("mt:status", "Active"),
//				this.generatePropertyIsLikeFragment("mt:status", "GRANTED")));

		return this.generateFilter(
				this.generateAndComparisonFragment(localFragment.toArray(new String[localFragment.size()])));
	}

}
