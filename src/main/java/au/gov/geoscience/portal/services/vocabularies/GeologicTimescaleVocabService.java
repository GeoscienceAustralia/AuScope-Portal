package au.gov.geoscience.portal.services.vocabularies;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.SISSVoc3Service;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.Format;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker.View;
import org.auscope.portal.core.services.namespaces.VocabNamespaceContext;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class GeologicTimescaleVocabService extends SISSVoc3Service {

    private static final Set<String> RANKS = new HashSet<String>(
            Arrays.asList("http://resource.geosciml.org/ontology/timescale/gts#Period",
                    "http://resource.geosciml.org/ontology/timescale/gts#Era", // TODO
                                                                               // Fix
                                                                               // this
                    "http://resource.geosciml.org/ontology/timescale/gts#Eon"));

    public static final String REPOSITORY_NAME = "csiro/international-chronostratigraphic-chart-2016/2016-12-v3";

    public GeologicTimescaleVocabService(HttpServiceCaller httpServiceCaller, SISSVoc3MethodMaker sissVocMethodMaker,
            String baseUrl) {
        super(httpServiceCaller, sissVocMethodMaker, baseUrl, REPOSITORY_NAME);

    }

    public Map<String, String> getAllTimescaleConcepts() throws PortalServiceException, URISyntaxException {
        Map<String, String> result = new HashMap<String, String>();

        Model model = ModelFactory.createDefaultModel();
        int pageNumber = 0;
        int pageSize = this.getPageSize();

        // Request each of the GA commodity names
        do {
            HttpRequestBase method = sissVocMethodMaker.getAllConcepts(getBaseUrl(), getRepository(), Format.Rdf,
                    View.description, pageSize, pageNumber);
            if (requestPageOfConcepts(method, model)) {
                pageNumber++;
            } else {
                break;
            }
        } while (true);

        // Iterate over all the resources with a preferred label
        Property prefLabelProperty = model.createProperty(VocabNamespaceContext.SKOS_NAMESPACE, "prefLabel");
        Property typeProperty = model.createProperty(VocabNamespaceContext.RDF_NAMESPACE, "type");

        ResIterator iterator = model.listResourcesWithProperty(prefLabelProperty);
        while (iterator.hasNext()) {
            // Ensure we only include the preferred labels matching 'language'
            Resource res = iterator.next();
            StmtIterator prefLabelIt = res.listProperties(prefLabelProperty);
            while (prefLabelIt.hasNext()) {
                Statement prefLabelStatement = prefLabelIt.next();
                // Statement typeStatement = res.getProperty(typeProperty);
                if (prefLabelStatement.getLanguage().equals("en")) {
                    String prefLabel = prefLabelStatement.getString();
                    StmtIterator typeIt = res.listProperties(typeProperty);
                    while (typeIt.hasNext()) {
                        Statement typeStatement = typeIt.next();

                        String urn = res.getURI();
                        if (urn != null) {

                            if (typeStatement != null && RANKS.contains(typeStatement.getResource().getURI())) {
                                result.put(urn, prefLabel);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

}
