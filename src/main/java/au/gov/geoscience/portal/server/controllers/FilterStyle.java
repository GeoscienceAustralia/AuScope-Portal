package au.gov.geoscience.portal.server.controllers;

public enum FilterStyle {
    MINE("square", 8, "#1fffff", "#000000", 0.15), MINE_VIEW("square", 8, "#aa00aa", "#000000", 0.15), MINERAL_OCCURRENCE_VIEW("circle", 8, "#00aa00", "#000000",
            0.15), COMMODITY_RESOURCE_VIEW("circle", 8, "#ff00aa", "#000000", 0.15);

    public final String shape;
    public final int size;
    public final String fillColour;
    public final String borderColour;
    public final double borderWidth;

    FilterStyle(final String shape, final int size, final String fillColour, final String borderColour,
            final double borderWidth) {
        this.shape = shape;
        this.size = size;
        this.fillColour = fillColour;
        this.borderColour = borderColour;
        this.borderWidth = borderWidth;
    }
    
    public final String getStyle(String filter, String name, String title, String namespace) {

        String style = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<StyledLayerDescriptor version=\"1.0.0\" xmlns:mo=\"http://xmlns.geoscience.gov.au/minoccml/1.0\" "
                + "xmlns:er=\"urn:cgi:xmlns:GGIC:EarthResource:1.1\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:erl=\""
                + namespace + "\" "
                + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\" "
                + "xmlns:sld=\"http://www.opengis.net/sld\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<NamedLayer>" + "<Name>" + name + "</Name>" + "<UserStyle>" + "<Name>portal-style</Name>" + "<Title>"
                + name + "</Title>" + "<Abstract>EarthResource</Abstract>" + "<IsDefault>1</IsDefault>"
                + "<FeatureTypeStyle>" + "<Rule>" + "<Name>" + name + "</Name>" + "<Title>" + title + "</Title>"
                + "<Abstract>" + name + "</Abstract>" + filter + "<PointSymbolizer>" + "<Graphic>" + "<Mark>"
                + "<WellKnownName>" + this.shape + "</WellKnownName>" + "<Fill>" + "<CssParameter name=\"fill\">"
                + this.fillColour + "</CssParameter>" + "</Fill>" + "<Stroke>" + "<CssParameter name=\"stroke\">"
                + this.borderColour + "</CssParameter>" + "<CssParameter name=\"stroke-width\">" + this.borderWidth
                + "</CssParameter>" + "</Stroke>" + "</Mark>" + "<Size>" + this.size + "</Size>" + "</Graphic>"
                + "</PointSymbolizer>" + "</Rule>" + "</FeatureTypeStyle>" + "</UserStyle>" + "</NamedLayer>"
                + "</StyledLayerDescriptor>";
        return style;
    }
}
