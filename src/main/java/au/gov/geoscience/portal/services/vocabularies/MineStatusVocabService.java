package au.gov.geoscience.portal.services.vocabularies;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
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

public class MineStatusVocabService extends SISSVoc3Service {
    
    public static final String REPOSITORY_NAME = "ga/mine-status/v0-1";
    
    public MineStatusVocabService(HttpServiceCaller httpServiceCaller, SISSVoc3MethodMaker sissVocMethodMaker,
            String baseUrl) {
        super(httpServiceCaller, sissVocMethodMaker, baseUrl, REPOSITORY_NAME);
   
    }

    
    public Map<String, String> getAllMineStatusConcepts() throws Exception {
        Map<String, String> result = new HashMap<String, String>();

        Model model = ModelFactory.createDefaultModel();

        int pageNumber = 0;
        int pageSize = this.getPageSize();

        do {
            HttpRequestBase method = sissVocMethodMaker.getAllConcepts(getBaseUrl(), getRepository(),
                    Format.Rdf, View.description, pageSize, pageNumber);
            if (requestPageOfConcepts(method, model)) {
                pageNumber++;
            } else {
                break;
            }
        } while (true);

        // Iterate over all the resources with a preferred label
        Property prefLabelProperty = model.createProperty(VocabNamespaceContext.SKOS_NAMESPACE, "prefLabel");

        ResIterator iterator = model.listResourcesWithProperty(prefLabelProperty);
        while (iterator.hasNext()) {
            // Ensure we only include the preferred labels matching 'language'
            Resource res = iterator.next();
            StmtIterator prefLabelIt = res.listProperties(prefLabelProperty);
            while (prefLabelIt.hasNext()) {
                Statement prefLabelStatement = prefLabelIt.next();
                String prefLabel = prefLabelStatement.getString();

                String urn = res.getURI();
                if (urn != null) {
                    result.put(urn, prefLabel);
                }

            }
        }

        return result;
    }
}
