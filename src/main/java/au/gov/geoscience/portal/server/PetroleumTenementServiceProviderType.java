package au.gov.geoscience.portal.server;

/**
 * Petroleum Tenement services' Geoserver settings
 */
public enum PetroleumTenementServiceProviderType {
    GeoServer("pt:PetroleumTenement", "pt:name", "pt:holder", "pt:status_uri", "pt:tenementType_uri", "pt:shape"),
    NSWGeoServer("PetroleumTenement", "pt:name", "pt:holder", "pt:status_uri", "pt:tenementType_uri", "pt:shape");

    private final String featureType;
    private final String nameField;
    private final String holderField;
    private final String statusURIField;
    private final String tenementTypeURIField;
    private final String shapeField;

    PetroleumTenementServiceProviderType(String featureType, String nameField, String holderField, String statusURIField, String tenementTypeURIField, String shapeField) {
        this.featureType = featureType;
        this.nameField = nameField;
        this.holderField = holderField;
        this.statusURIField = statusURIField;
        this.tenementTypeURIField = tenementTypeURIField;
        this.shapeField = shapeField;
    }

    public String featureType() {
        return featureType;
    }

    public String nameField() {
        return nameField;
    }

    public String holderField() {
        return holderField;
    }

    public String statusURIField() {
        return statusURIField;
    }

    public String tenementTypeURIField() {
        return tenementTypeURIField;
    }

    public String shapeField() {
        return shapeField;
    }
}
