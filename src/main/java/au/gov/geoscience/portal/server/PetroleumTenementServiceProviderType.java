package au.gov.geoscience.portal.server;

/**
 * Petroleum Tenement services' Geoserver settings
 */
public class PetroleumTenementServiceProviderType {
    private final String featureType;
    private final String nameField;
    private final String holderField;
    private final String statusURIField;
    private final String tenementTypeURIField;
    private final String shapeField;

    public PetroleumTenementServiceProviderType() {
        this.featureType = "pt:PetroleumTenement";
        this.nameField = "pt:name";
        this.holderField = "pt:holder";
        this.statusURIField = "pt:status_uri";
        this.tenementTypeURIField = "pt:tenementType_uri";
        this.shapeField = "pt:shape";
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
