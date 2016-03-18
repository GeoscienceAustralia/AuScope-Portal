package org.auscope.portal.server.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.BaseWFSService;
import org.auscope.portal.core.services.PortalServiceException;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker;
import org.auscope.portal.core.services.methodmakers.WFSGetFeatureMethodMaker.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.methodmakers.filter.IFilter;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.xslt.WfsToKmlTransformer;
import org.auscope.portal.mineraloccurrence.RockPropertiesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RockPropertiesService extends BaseWFSService {

	private final Log log = LogFactory.getLog(getClass());

	private WfsToKmlTransformer gmlToKml;

	@Autowired
	public RockPropertiesService(HttpServiceCaller httpServiceCaller, WFSGetFeatureMethodMaker wfsMethodMaker,
			WfsToKmlTransformer gmlToKml) {
		super(httpServiceCaller, wfsMethodMaker);
		this.gmlToKml = gmlToKml;

	}

	
	
	public WFSTransformedResponse getRockPropertiesGML(String serviceUrl, String rockProperty, FilterBoundingBox bbox,
			int maxFeatures) throws PortalServiceException {
		RockPropertiesFilter rockPropertiesFilter = new RockPropertiesFilter(rockProperty);

		HttpRequestBase method = null;
		String filterString = generateFilterString(rockPropertiesFilter, bbox);

		log.warn("Filter String: " + filterString);

		try {
			method = generateWFSRequest(serviceUrl, "ga_rock_properties_wfs:scalar_results", null, filterString,
					maxFeatures, null, ResultType.Results);
			String responseGml = httpServiceCaller.getMethodResponseAsString(method);
			String responseKml = gmlToKml.convert(responseGml, serviceUrl);
			return new WFSTransformedResponse(responseGml, responseKml, method);
		} catch (Exception ex) {
			throw new PortalServiceException(method, "Error when attempting to download Rock Properties GML", ex);
		}

	}

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
