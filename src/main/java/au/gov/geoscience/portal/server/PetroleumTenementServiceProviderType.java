package au.gov.geoscience.portal.server;

/**
 * Petroleum Tenement services' Geoserver settings
 */
public class PetroleumTenementServiceProviderType {
    private final String featureType;
    private final String nameField;
    private final String holderField;

    public PetroleumTenementServiceProviderType() {
        this.featureType = "pt:PetroleumTenement";
        this.nameField = "pt:name";
        this.holderField = "pt:holder";
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
}
