package au.gov.geoscience.portal.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterTransformer;
import org.geotools.filter.v1_1.OGC;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractFilter{

    protected Filter filter;

    protected FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();

    protected List<Filter> filterList = new ArrayList<>();

    protected Filter generateLikeFilter(String field, String value) {
        PropertyName property = filterFactory.property(field);
        return filterFactory.like(property, value, "*", "#", "!");
    }

    protected Filter generateEqualFilter(String field, String value) {
        PropertyName property = filterFactory.property(field);
        Literal literal = filterFactory.literal(value);
        return filterFactory.equals(property, literal);
    }

    protected Filter generateOrFilter(String field, Collection<String> values) {
        List<Filter> filters = new ArrayList<>();

        for (String value : values) {
            Filter propertyIsEqualTo = generateEqualFilter(field, value);
            filters.add(propertyIsEqualTo);
        }
        return filterFactory.or(filters);
    }

    protected Filter generateNotNullFilter(String field) {
        return filterFactory.not(generateNullFilter(field));
    }

    protected Filter generateBBOXFilter(String field, FilterBoundingBox value) {

        try {
            PropertyName propertyName = filterFactory.property(field);

            double[] lowerCornerPoints = value.getLowerCornerPoints();
            double[] upperCornerPoints = value.getUpperCornerPoints();


            if (value.getBboxSrs() != null && value.getBboxSrs().contains("EPSG")) {

//                CRSAuthorityFactory crsAuthorityFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
//                CoordinateReferenceSystem crs = crsAuthorityFactory.createCoordinateReferenceSystem(value.getBboxSrs());
//                BoundingBox bbox = new ReferencedEnvelope(lowerCornerPoints[0], upperCornerPoints[0], lowerCornerPoints[1], upperCornerPoints[1], crs);

                return filterFactory.bbox(propertyName, lowerCornerPoints[1], lowerCornerPoints[0], upperCornerPoints[1], upperCornerPoints[0], value.getBboxSrs());
            } else {
                return filterFactory.bbox(propertyName, lowerCornerPoints[0], lowerCornerPoints[1], upperCornerPoints[0], upperCornerPoints[1], value.getBboxSrs());
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Filter generateNullFilter(String field) {
        PropertyName property = filterFactory.property(field);
        return filterFactory.isNull(property);
    }

    public Filter getFilter() {
        return filter;
    }

    public String generateFilterStringAllRecords()  {
        if (filter == null) {
            return new String();
        }
        try {

            Configuration configuration = new OGCConfiguration();
            Encoder encoder = new Encoder( configuration );
            encoder.setOmitXMLDeclaration(true);
            return  encoder.encodeAsString( filter, OGC.Filter);

            //FilterTransformer transformer = new FilterTransformer();
            //return transformer.transform(filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }


}
