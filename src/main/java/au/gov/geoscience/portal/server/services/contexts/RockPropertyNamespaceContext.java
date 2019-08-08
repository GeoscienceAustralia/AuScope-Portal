package au.gov.geoscience.portal.server.services.contexts;

import org.auscope.portal.core.services.namespaces.WFSNamespaceContext;

public class RockPropertyNamespaceContext extends WFSNamespaceContext {

    public RockPropertyNamespaceContext() {
        super();
        map.put("rockprops", "http://www.ga.gov.au");
    }
}
