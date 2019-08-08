package au.gov.geoscience.portal.server;

/**
 * Differentiates WMS service providers for Mineral Tenement services.
 * 
 * The behaviour of ArcGIS Server is different to the default (GeoServer) and this
 * enum provides settings for each such that they behave in the same way to the
 * end user.
 * 
 */
public enum MineralTenementServiceProviderType {
	GeoServer("mt:MineralTenement", "#66ff66", "#a3ffa3", "mt:name", "mt:owner", "mt:shape"),
	ArcGIS("MineralTenement", "#00ff00", "#66ff66", "TENNAME", "TENOWNER", "SHAPE");

	private final String featureType;
	private final String fillColour;
	private final String borderColour;
	private final String nameField;
	private final String ownerField;
	private final String shapeField;

	private MineralTenementServiceProviderType(String featureType, String fillColour, String borderColour,
                                               String nameField, String ownerField, String shapeField) {
		this.featureType = featureType;
		this.fillColour = fillColour;
		this.borderColour = borderColour;
		this.nameField = nameField;
		this.ownerField = ownerField;
		this.shapeField = shapeField;
	}

	public String featureType() {
		return featureType;
	}

	public String fillColour() {
		return fillColour;
	}

	public String borderColour() {
		return borderColour;
	}

	public String nameField() {
		return nameField;
	}

	public String ownerField() {
		return ownerField;
	}

	public String shapeField() {
		return shapeField;
	}

	public static MineralTenementServiceProviderType parseUrl(String serviceUrl) {
		if (serviceUrl != null && (serviceUrl.toUpperCase().contains("MAPSERVER/WFSSERVER")
				|| serviceUrl.toUpperCase().contains("MAPSERVER/WMSSERVER"))) {
			return MineralTenementServiceProviderType.ArcGIS;
		}
		return MineralTenementServiceProviderType.GeoServer;
	}
}
