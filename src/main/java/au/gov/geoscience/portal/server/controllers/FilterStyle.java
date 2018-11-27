package au.gov.geoscience.portal.server.controllers;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.*;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import javax.xml.transform.TransformerException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum FilterStyle {
    MINE("square", 8, "#1fffff", "#000000", 0.15),
    MINING_ACTIVITY("square", 8, "#1fffff", "#000000", 0.15),
    MINE_VIEW("square", 8, "#aa00aa", "#000000", 0.15),
    MINOCC_VIEW("circle", 8, "#ffff00", "#000000", 0.15),
    MINERAL_OCCURRENCE_VIEW("circle", 8, "#00aa00", "#000000", 0.15),
    COMMODITY_RESOURCE_VIEW("circle", 8, "#ff00aa", "#000000", 0.15),
    BOREHOLE_VIEW(null, 0, null, null, 0),
    NVCL(null, 0, null, null, 0),
    TENEMENTS(null, 0, null, null, 0),
    ARCGIS_TENEMENTS(null, 0, null, null, 0);


    public final String shape;
    public final int size;
    public final String fillColour;
    public final String borderColour;
    public final double borderWidth;

    FilterStyle(final String shape, final int size, final String fillColour, final String borderColour,
            final double borderWidth) {

        StyledLayerDescriptor sld = null;
        this.shape = shape;
        this.size = size;
        this.fillColour = fillColour;
        this.borderColour = borderColour;
        this.borderWidth = borderWidth;
    }
    
    public final String getStyle(Filter filter, String name, String title, String namespace) throws TransformerException, URISyntaxException {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        Map<URI, String> nsMap = new HashMap<>();

        // TODO: Fix below

        if (namespace != null) {
            nsMap.put(new URI(namespace), "erl");
        }
        nsMap.put(new URI("http://xmlns.geoscience.gov.au/minoccml/1.0") , "mo");
        nsMap.put(new URI("urn:cgi:xmlns:GGIC:EarthResource:1.1"), "er");

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        sld.setAbstract("Portal Style");
        sld.setName("portal-style");
        sld.setTitle("Title");

        NamedLayer namedLayer = styleFactory.createNamedLayer();
        namedLayer.setName(name);

        Style style = styleFactory.createStyle();
        style.setName("UserStyle");

        FeatureTypeStyle ftStyle = styleFactory.createFeatureTypeStyle();

        Expression fillColour = filterFactory.literal(this.fillColour);
        Expression strokeColour = filterFactory.literal(this.borderColour);
        Expression strokeWidth = filterFactory.literal(this.borderWidth);
        Expression shape = filterFactory.literal(this.shape);
        Expression size = filterFactory.literal(this.size);

        Fill fill = styleFactory.createFill(fillColour);
        Stroke stroke = styleFactory.createStroke(strokeColour,strokeWidth);

        Mark mark = styleFactory.createMark(shape, stroke, fill, null, null);

        Graphic graphic = styleFactory.createGraphic(null, new Mark[]{mark},null,null, size,null);


        PointSymbolizer symbolizer = styleFactory.createPointSymbolizer();

        symbolizer.setGraphic(graphic);

        Rule rule = styleFactory.createRule();

        rule.setName(name);
        rule.symbolizers().add(symbolizer);


        if (filter != null) {
            rule.setFilter(filter);
        }
        ftStyle.rules().add(rule);

        style.featureTypeStyles().add(ftStyle);


        style.setDefaultSpecification(symbolizer);
        namedLayer.addStyle(style);
        sld.setStyledLayers(new StyledLayer[]{namedLayer});

       SLDTransformer transformer = new SLDTransformer(nsMap);
       return transformer.transform(sld);

    }
}
