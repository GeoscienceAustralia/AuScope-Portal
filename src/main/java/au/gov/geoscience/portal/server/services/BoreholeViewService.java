package au.gov.geoscience.portal.server.services;

import au.gov.geoscience.portal.server.services.filters.BoreholeViewFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

/**
 * A service class for making requests to the SF0 Borehole web service
 *
 * @author Florence Tan
 *
 */
@Service
public class BoreholeViewService extends BoreholeService {

    // -------------------------------------------------------------- Constants

    // ----------------------------------------------------------- Constructors

    @Autowired
    public BoreholeViewService(HttpServiceCaller serviceCaller, WFSGetFeatureMethodMaker methodMaker) {
        super(serviceCaller, methodMaker);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get all SF0 Boreholes from a given service url and return the response
     *
     * @param serviceURL
     * @param bbox
     *            Set to the bounding box in which to fetch results, otherwise set it to null
     * @return
     * @throws Exception
     */
    public WFSResponse getAllBoreholes(String serviceURL, String boreholeName, String custodian,
            String dateOfDrilling, int maxFeatures, FilterBoundingBox bbox) throws Exception {
        return getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling, maxFeatures, bbox, null);
    }

    /**
     * Get all SF0 Boreholes from a given service url and return the response
     *
     * @param serviceURL
     * @param bbox
     *            Set to the bounding box in which to fetch results, otherwise set it to null
     * @return
     * @throws Exception
     */
    public WFSResponse getAllBoreholes(String serviceURL, String boreholeName, String custodian,
            String dateOfDrilling, int maxFeatures, FilterBoundingBox bbox, String outputFormat) throws Exception {
        return getAllBoreholes(serviceURL, boreholeName, custodian, dateOfDrilling, null, maxFeatures, bbox, outputFormat);
    }

    /**
     * Get all SF0 Boreholes from a given service url and return the response
     *
     * @param serviceURL
     * @param bbox
     *            Set to the bounding box in which to fetch results, otherwise set it to null
     * @return
     * @throws Exception
     */
    public WFSResponse getAllBoreholes(String serviceURL, String boreholeName, String custodian,
                                       String dateOfDrilling, Boolean justNVCL, int maxFeatures, FilterBoundingBox bbox, String outputFormat) throws Exception {
        String filterString;

        BoreholeViewFilter sf0BoreholeFilter = new BoreholeViewFilter(boreholeName, custodian, dateOfDrilling, null, null, justNVCL);
        if (bbox == null) {
            filterString = sf0BoreholeFilter.getFilterStringAllRecords();
        } else {
            filterString = sf0BoreholeFilter.getFilterStringBoundingBox(bbox);
        }

        HttpRequestBase method = null;
        try {
            // Create a GetFeature request with an empty filter - get all
            method = this.generateWFSRequest(serviceURL, getTypeName(), null, filterString, maxFeatures, null,
                    ResultType.Results, outputFormat);
            String responseGml = this.httpServiceCaller.getMethodResponseAsString(method);

            return new WFSResponse(responseGml, method);
        } catch (Exception ex) {
            throw new PortalServiceException(method, ex);
        }
    }

    public WFSCountResponse getBoreholesCount(String serviceUrl, String boreholeName, String custodian, String dateOfDrilling,
                                              Boolean justNVCL, int maxFeatures, FilterBoundingBox bbox) throws Exception {
        String filterString;

        BoreholeViewFilter sf0BoreholeFilter = new BoreholeViewFilter(boreholeName, custodian, dateOfDrilling, null, null, justNVCL);
        if (bbox == null) {
            filterString = sf0BoreholeFilter.getFilterStringAllRecords();
        } else {
            filterString = sf0BoreholeFilter.getFilterStringBoundingBox(bbox);
        }

        HttpRequestBase method = generateWFSRequest(serviceUrl, "gsmlp:BoreholeView", null,
                filterString, maxFeatures, null, ResultType.Hits);
        return getWfsFeatureCount(method);
    }


    public String getFilter(String boreholeName, String custodian, String dateOfDrilling,
            int maxFeatures, FilterBoundingBox bbox, List<String> ids, Boolean justNVCL) throws Exception {
        BoreholeViewFilter filter = new BoreholeViewFilter(boreholeName, custodian, dateOfDrilling, ids, null, justNVCL);
        return generateFilterString(filter, bbox);
    }

    public String getFilter(String boreholeName, String custodian, String dateOfDrilling,
            int maxFeatures, FilterBoundingBox bbox, List<String> ids, List<String> identifiers, Boolean justNVCL) throws Exception {
        BoreholeViewFilter filter = new BoreholeViewFilter(boreholeName, custodian, dateOfDrilling, ids, identifiers, justNVCL);
        return generateFilterString(filter, bbox);
    }

    @Override
    public String getTypeName() {
        return "gsmlp:BoreholeView";
    }

    @Override
    public String getGeometryName() {
        return "gsmlp:shape";
    }

    public WFSCountResponse getNVCLCount(String serviceUrl, String boreholeName, String dateOfDrilling, Boolean nvclCollection, FilterBoundingBox bbox, int maxFeatures) throws URISyntaxException, PortalServiceException {
        BoreholeViewFilter filter = new BoreholeViewFilter(boreholeName, "", dateOfDrilling, null, null, nvclCollection);


        String filterString = generateFilterString(filter, bbox);
        HttpRequestBase method = generateWFSRequest(serviceUrl, "gsmlp:BoreholeView", null,
                filterString, maxFeatures, null, ResultType.Hits);
        return getWfsFeatureCount(method);
    }
}
