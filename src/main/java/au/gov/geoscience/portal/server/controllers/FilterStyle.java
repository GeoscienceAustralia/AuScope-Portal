package au.gov.geoscience.portal.server.controllers;

import org.geotools.sld.SLDConfiguration;
import org.geotools.styling.*;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.opengis.filter.Filter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterStyle {

    public static final String getStyle(InputStream sldStream, Filter filter, String namespace) throws TransformerException, URISyntaxException, IOException {

        Configuration config = new SLDConfiguration();
        Parser parser = new Parser(config);

        StyledLayerDescriptor sld = null;
        try {
            sld = (StyledLayerDescriptor) parser.parse(sldStream);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Map<URI, String> nsMap = new HashMap<>();

        if (namespace != null) {
            nsMap.put(new URI(namespace), "erl");
        }
        nsMap.put(new URI("http://xmlns.geoscience.gov.au/minoccml/1.0") , "mo");
        nsMap.put(new URI("urn:cgi:xmlns:GGIC:EarthResource:1.1"), "er");
        nsMap.put(new URI("http://xmlns.geoscience.gov.au/mineraltenementml/1.0") , "mt");
        nsMap.put(new URI("http://xmlns.geosciml.org/geosciml-portrayal/4.0"), "gsmlp");
        StyledLayer[] styledLayers = sld.getStyledLayers();

        for (int i = 0; i < styledLayers.length; i++) {
            if (styledLayers[i] instanceof NamedLayer) {
                NamedLayer layer = (NamedLayer) styledLayers[i];
                Style[] styles = layer.getStyles();
                for (int j = 0; j< styles.length; j++) {
                    List<FeatureTypeStyle> ftStyles = styles[j].featureTypeStyles();
                    for (FeatureTypeStyle featureTypeStyle : ftStyles) {
                        for (Rule rule : featureTypeStyle.rules()) {
                            rule.setFilter(filter);
                        }
                    }
                }
            }
        }

        SLDTransformer transformer = new SLDTransformer(nsMap);
        return transformer.transform(sld);
    }
}
