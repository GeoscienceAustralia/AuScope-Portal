package org.auscope.portal.server.web.service;

import au.gov.geoscience.portal.server.controllers.VocabularyController;
import au.gov.geoscience.portal.services.methodmaker.filter.MineralTenementFilter;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.VocabularyFilterService;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.IFilter;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSResponse;
import org.auscope.portal.server.MineralTenementServiceProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages mineral tenement queries
 *
 * @author VictorTey
 * @version
 */
@Service
public class MineralTenementService extends BaseWFSService {


	private final VocabularyFilterService vocabularyFilterService;

	@Autowired
	public MineralTenementService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker methodMaker, VocabularyFilterService vocabularyFilterService) {
		super(httpServiceCaller, methodMaker);
		this.vocabularyFilterService = vocabularyFilterService;

	}

    /**
     * @param name
     * @param typeUri
     * @param owner
     * @param statusUri
     * @param endDate
     * @param bbox
     * @param mineralTenementServiceProviderType
     * @return
     * @throws Exception
     */
	public String getMineralTenementFilter(String name, String typeUri, String owner, String statusUri, String endDate,
			FilterBoundingBox bbox, MineralTenementServiceProviderType mineralTenementServiceProviderType) throws Exception {

	    Set<String> typeUris = new HashSet<>();

	    if (typeUri != null && !typeUri.isEmpty()) {
	        typeUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_TYPE_VOCABULARY_ID, typeUri);
        }

        Set<String> statusUris = new HashSet<>();
        if (statusUri != null && !statusUri.isEmpty()) {
            statusUris = this.vocabularyFilterService.getAllNarrower(VocabularyController.TENEMENT_STATUS_VOCABULARY_ID, statusUri);
        }
		MineralTenementFilter filter = new MineralTenementFilter(name, owner, statusUris, typeUris, mineralTenementServiceProviderType);
		return generateFilterString(filter, bbox);
	}

	public String getMineralTenementWithStyling(String name, String tenementTypeUri, String owner, String statusUri,
			String endDate) throws Exception {
		MineralTenementFilter filter = new MineralTenementFilter(name, tenementTypeUri, owner, statusUri, endDate, MineralTenementServiceProviderType.GeoServer);
		return generateAdditionalStyleFilter(filter);
	}

	/**
	 * Utility for turning a filter and add additional styling to the filter.
	 * 
	 * @param filter
	 *            The filter
	 * @return
	 * @throws OperationNotSupportedException
	 */
	public static String generateAdditionalStyleFilter(IFilter filter) throws OperationNotSupportedException {
		if (filter instanceof MineralTenementFilter) {
			MineralTenementFilter mtFilter = (MineralTenementFilter) filter;
			return mtFilter.getFilterWithAdditionalStyle();
		} else {
			throw new OperationNotSupportedException(
					"Only MineralTenementFilter supports the use of additional style filtering");
		}

	}

	public WFSResponse getAllTenements(String serviceURL, String tenementName, String owner, int maxFeatures,
			FilterBoundingBox bbox, String outputFormat) throws Exception {
		String filterString;
		MineralTenementFilter mineralTenementFilter = new MineralTenementFilter(tenementName);
		if (bbox == null) {
			filterString = mineralTenementFilter.getFilterStringAllRecords();
		} else {
			filterString = mineralTenementFilter.getFilterStringBoundingBox(bbox);
		}

		HttpRequestBase method = null;

		try {
			method = this.generateWFSRequest(serviceURL, "mt:MineralTenement", null, filterString, maxFeatures, null,
					ResultType.Results,outputFormat);
			String responseGML = this.httpServiceCaller.getMethodResponseAsString(method);
			return new WFSResponse(responseGML, method);
		} catch (Exception e) {
			throw new PortalServiceException(method, e);
		}

	}

	public WFSCountResponse getTenementCount(String serviceURL, String tenementName, String owner, int maxFeatures,
			FilterBoundingBox bbox) throws PortalServiceException, URISyntaxException {
		// TODO Auto-generated method stub
		String filterString;
		MineralTenementFilter mineralTenementFilter = new MineralTenementFilter(tenementName);
		if (bbox == null) {
			filterString = mineralTenementFilter.getFilterStringAllRecords();
		} else {
			filterString = mineralTenementFilter.getFilterStringBoundingBox(bbox);
		}

		HttpRequestBase method = null;

		method = generateWFSRequest(serviceURL, "mt:MineralTenement", null, filterString, maxFeatures, null,
				ResultType.Hits);
		return getWfsFeatureCount(method);

	}

    /**
     * Utility for turning a filter and optional bounding box into a OGC filter
     * string
     *
     * @param filter
     *            The filter
     * @param bbox
     *            [Optional] the spatial bounds to constrain the result set
     * @return
     */
    public static String generateFilterString(IFilter filter, FilterBoundingBox bbox) {
        String filterString = null;
        if (bbox == null) {
            filterString = filter.getFilterStringAllRecords();
        } else {
            filterString = filter.getFilterStringBoundingBox(bbox);
        }

        return filterString;
    }

}
