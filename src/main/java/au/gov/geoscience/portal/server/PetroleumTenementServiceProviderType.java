package au.gov.geoscience.portal.server;

/**
 * Differentiates WMS service providers for Petroleum Tenement services.
 * <p>
 * The behaviour of ArcGIS Server is different to the default (GeoServer) and this
 * enum provides settings for each such that they behave in the same way to the
 * end user.
 */
public enum PetroleumTenementServiceProviderType {
    GeoServer("pt:PetroleumTenement", "#d3faff", "#0813a8", "pt:name", "pt:holder", "pt:shape");
    private final String featureType;
    private final String fillColour;
    private final String borderColour;
    private final String nameField;
    private final String holderField;
    private final String shapeField;

    PetroleumTenementServiceProviderType(String featureType, String fillColour, String borderColour, String nameField, String holderField, String shapeField) {
        this.featureType = featureType;
        this.fillColour = fillColour;
        this.borderColour = borderColour;
        this.nameField = nameField;
        this.holderField = holderField;
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

    public String holderField() {
        return holderField;
    }

    public String shapeField() {
        return shapeField;
    }

    public static PetroleumTenementServiceProviderType parseUrl(String serviceUrl) {
        return PetroleumTenementServiceProviderType.GeoServer;
    }
}
