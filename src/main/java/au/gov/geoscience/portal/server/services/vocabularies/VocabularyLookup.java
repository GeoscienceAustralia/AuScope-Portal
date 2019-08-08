package au.gov.geoscience.portal.server.services.vocabularies;


/**
 * A set of vocabularies with the base URIs. 
 *
 */
public enum VocabularyLookup {
	RESERVE_CATEGORY("http://resource.geosciml.org/classifier/cgi/reserve-assessment-category", "http://resource.geosciml.org/classifierscheme/cgi/201403/reserve-assessment-category"),
	RESOURCE_CATEGORY("http://resource.geosciml.org/classifier/cgi/resource-assessment-category", "http://resource.geosciml.org/classifierscheme/cgi/201403/resource-assessment-category"),
	COMMODITY_CODE("http://resource.geosciml.org/classifier/cgi/commodity-code/",null),
	GEOLOGIC_TIMESCALE("http://resource.geosciml.org/classifier/ics/ischart/2014",null),
	MINE_STATUS("http://resource.geosciml.org/classifier/cgi/mine-status","http://resource.geosciml.org/classifierscheme/cgi/201403/mine-status");
	
	
	private final String uri;
	
	private final String scheme;
	
	
	VocabularyLookup(String uri, String scheme) {
		this.uri = uri;
		this.scheme = scheme;
	}

	public String uri(){
		return uri;
	}
}
