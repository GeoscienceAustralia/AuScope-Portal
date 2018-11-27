package org.auscope.portal.server.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.auscope.portal.core.server.OgcServiceProviderType;
import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.auscope.portal.core.services.responses.wfs.WFSCountResponse;
import org.auscope.portal.core.services.responses.wfs.WFSTransformedResponse;
import org.auscope.portal.core.util.FileIOUtil;
import org.auscope.portal.server.web.controllers.downloads.EarthResourcesDownloadController;
import org.auscope.portal.server.web.service.MineralOccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import au.gov.geoscience.portal.server.controllers.EarthResourceFilterController;

/**
 * Controller that handles all Earth Resource related requests
 * <p>
 * It handles the following WFS features:
 * <ul>
 * <li>Mine</li>
 * <li>Mineral Occurrence</li>
 * <li>Mininig Activity</li>
 * </ul>
 * </p>
 * @deprecated Replaced by {@link EarthResourceFilterController}
 *
 * @author Jarek Sanders
 * @author Josh Vote
 */
@Deprecated
@Controller
public class EarthResourcesFilterController extends BasePortalController {

	/**
	 * different styles that apply for the different layers managed by this
	 * controller
	 */
	public enum Styles {
		MINE("square", 8, "#1fffff", "#000000", 0.15), MINING_ACTIVITY("square", 6, "#FF9900", "#000000",
				0.15), MINERAL_OCCURRENCE("circle", 8, "#ffff00", "#000000", 0.15);

		public final String shape;
		public final int size;
		public final String fillColour;
		public final String borderColour;
		public final double borderWidth;

		private Styles(final String shape, final int size, final String fillColour, final String borderColour,
				final double borderWidth) {
			this.shape = shape;
			this.size = size;
			this.fillColour = fillColour;
			this.borderColour = borderColour;
			this.borderWidth = borderWidth;
		}
	}

	// ----------------------------------------------------- Instance variables

	private MineralOccurrenceService mineralOccurrenceService;

	// ----------------------------------------------------------- Constructors

	@Autowired
	public EarthResourcesFilterController(MineralOccurrenceService mineralOccurrenceService) {
		this.mineralOccurrenceService = mineralOccurrenceService;
	}

	// ------------------------------------------- Property Setters and Getters

	/**
	 * Handles the Earth Resource Mine filter queries. (If the bbox elements are
	 * specified, they will limit the output response to 200 records implicitly)
	 *
	 * @param serviceUrl
	 *            the url of the service to query
	 * @param mineName
	 *            the name of the mine to query for
	 * @param request
	 *            the HTTP client request
	 * @return a WFS response converted into KML
	 * @throws Exception
	 */
	@RequestMapping("/doMineFilter.do")
	public ModelAndView doMineFilter(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam("mineName") String mineName, @RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualizing on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			WFSTransformedResponse response = this.mineralOccurrenceService.getMinesGml(serviceUrl, mineName, bbox,
					maxFeatures);

			return generateJSONResponseMAV(response.getSuccess(), response.getGml(), response.getTransformed(),
					response.getMethod());
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);
		}
	}

	/**
	 * Handles getting the count of the Earth Resource Mine filter queries. (If
	 * the bbox elements are specified, they will limit the output response to
	 * 200 records implicitly)
	 *
	 * @param serviceUrl
	 *            the url of the service to query
	 * @param mineName
	 *            the name of the mine to query for
	 * @param request
	 *            the HTTP client request
	 * @return a WFS response converted into KML
	 * @throws Exception
	 */
	@RequestMapping("/doMineFilterCount.do")
	public ModelAndView doMineFilterCount(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "mineName") String mineName,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualizing on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			WFSCountResponse response = this.mineralOccurrenceService.getMinesCount(serviceUrl, mineName, bbox,
					maxFeatures);
			return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);
		}
	}

	/**
	 * Handles the Earth Resource MineralOccerrence filter queries.
	 *
	 * @param serviceUrl
	 * @param commodityName
	 * @param measureType
	 * @param minOreAmount
	 * @param minOreAmountUOM
	 * @param minCommodityAmount
	 * @param minCommodityAmountUOM
	 * @param request
	 *            the HTTP client request
	 *
	 * @return a WFS response converted into KML
	 * @throws Exception
	 */
	@RequestMapping("/doMineralOccurrenceFilter.do")
	public ModelAndView doMineralOccurrenceFilter(
			@RequestParam(value = "serviceUrl", required = false) String serviceUrl,
			@RequestParam(value = "commodityName", required = false) String commodityName,
			@RequestParam(value = "measureType", required = false) String measureType,
			@RequestParam(value = "minOreAmount", required = false) String minOreAmount,
			@RequestParam(value = "minOreAmountUOM", required = false) String minOreAmountUOM,
			@RequestParam(value = "minCommodityAmount", required = false) String minCommodityAmount,
			@RequestParam(value = "minCommodityAmountUOM", required = false) String minCommodityAmountUOM,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {
		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualising on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			// get the mineral occurrences
			WFSTransformedResponse response = this.mineralOccurrenceService.getMineralOccurrenceGml(serviceUrl,
					commodityName, measureType, minOreAmount, minOreAmountUOM, minCommodityAmount,
					minCommodityAmountUOM, maxFeatures, bbox);

			return generateJSONResponseMAV(true, response.getGml(), response.getTransformed(), response.getMethod());
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);

		}
	}

	/**
	 * Handles counting the results of a Earth Resource MineralOccerrence filter
	 * query.
	 *
	 * @param serviceUrl
	 * @param commodityName
	 * @param measureType
	 * @param minOreAmount
	 * @param minOreAmountUOM
	 * @param minCommodityAmount
	 * @param minCommodityAmountUOM
	 * @param request
	 *            the HTTP client request
	 *
	 * @return Returns Integer count
	 * @throws Exception
	 */
	@RequestMapping("/doMineralOccurrenceFilterCount.do")
	public ModelAndView doMineralOccurrenceFilterCount(
			@RequestParam(value = "serviceUrl", required = false) String serviceUrl,
			@RequestParam(value = "commodityName", required = false) String commodityName,
			@RequestParam(value = "measureType", required = false) String measureType,
			@RequestParam(value = "minOreAmount", required = false) String minOreAmount,
			@RequestParam(value = "minOreAmountUOM", required = false) String minOreAmountUOM,
			@RequestParam(value = "minCommodityAmount", required = false) String minCommodityAmount,
			@RequestParam(value = "minCommodityAmountUOM", required = false) String minCommodityAmountUOM,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {
		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualising on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			// get the mineral occurrences
			WFSCountResponse response = this.mineralOccurrenceService.getMineralOccurrenceCount(serviceUrl,
					commodityName, measureType, minOreAmount, minOreAmountUOM, minCommodityAmount,
					minCommodityAmountUOM, maxFeatures, bbox);

			return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);

		}
	}
	
	@RequestMapping("/minOccViewFilterCount.do")
	public ModelAndView minOccViewFilterCount(
	        @RequestParam(value = "serviceUrl", required = false) String serviceUrl,
	        @RequestParam(required = false, value = "name") String name,            
            @RequestParam(required = false, value = "commodityName") String commodityName,
            @RequestParam(required = false, value = "minOreAmount") String minOreAmount,
            @RequestParam(required = false, value = "minReserves") String minReserves,
            @RequestParam(required = false, value = "minReserves") String minResources,
            @RequestParam(required = false, value = "bbox") String bboxJson,
            @RequestParam(required = false, value = "outputFormat") String outputFormat,
            @RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures
	        ){
	    OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
        FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson,ogcServiceProviderType);
        
        
	    try {
	        WFSCountResponse response = this.mineralOccurrenceService.getMinOccViewCount(serviceUrl, commodityName, minOreAmount, minReserves, minResources,
	                maxFeatures, bbox);
	        return generateJSONResponseMAV(true,new Integer(response.getNumberOfFeatures()),"");
	    } catch (Exception e) {
	        log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
            log.debug("Exception: ", e);
            return this.generateExceptionResponse(e, serviceUrl);
	    }
	}
	

	/**
	 * Handles Mining Activity filter queries Returns WFS response converted
	 * into KML.
	 *
	 * @param serviceUrl
	 * @param mineName
	 * @param startDate
	 * @param endDate
	 * @param oreProcessed
	 * @param producedMaterial
	 * @param cutOffGrade
	 * @param production
	 * @param request
	 * @return the KML response
	 * @throws Exception
	 */
	@RequestMapping("/doMiningActivityFilter.do")
	public ModelAndView doMiningActivityFilter(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
			@RequestParam(required = false, value = "startDate", defaultValue = "") String startDate,
			@RequestParam(required = false, value = "endDate", defaultValue = "") String endDate,
			@RequestParam(required = false, value = "oreProcessed", defaultValue = "") String oreProcessed,
			@RequestParam(required = false, value = "producedMaterial", defaultValue = "") String producedMaterial,
			@RequestParam(required = false, value = "cutOffGrade", defaultValue = "") String cutOffGrade,
			@RequestParam(required = false, value = "production", defaultValue = "") String production,
			@RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {
		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualizing on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			// Get the mining activities
			WFSTransformedResponse response = this.mineralOccurrenceService.getMiningActivityGml(serviceUrl, mineName,
					startDate, endDate, oreProcessed, producedMaterial, cutOffGrade, production, maxFeatures, bbox);

			return generateJSONResponseMAV(true, response.getGml(), response.getTransformed(), response.getMethod());
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);
		}
	}

	/**
	 * Handles counting the number Mining Activities matched by a filter Returns
	 * Integer count
	 *
	 * @param serviceUrl
	 * @param mineName
	 * @param startDate
	 * @param endDate
	 * @param oreProcessed
	 * @param producedMaterial
	 * @param cutOffGrade
	 * @param production
	 * @param request
	 * @return Returns Integer count
	 * @throws Exception
	 */
	@RequestMapping("/doMiningActivityFilterCount.do")
	public ModelAndView doMiningActivityFilterCount(@RequestParam("serviceUrl") String serviceUrl,
			@RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
			@RequestParam(required = false, value = "startDate", defaultValue = "") String startDate,
			@RequestParam(required = false, value = "endDate", defaultValue = "") String endDate,
			@RequestParam(required = false, value = "oreProcessed", defaultValue = "") String oreProcessed,
			@RequestParam(required = false, value = "producedMaterial", defaultValue = "") String producedMaterial,
			@RequestParam(required = false, value = "cutOffGrade", defaultValue = "") String cutOffGrade,
			@RequestParam(required = false, value = "production", defaultValue = "") String production,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		// The presence of a bounding box causes us to assume we will be using
		// this GML for visualizing on a map
		// This will in turn limit the number of points returned to 200
		OgcServiceProviderType ogcServiceProviderType = OgcServiceProviderType.parseUrl(serviceUrl);
		FilterBoundingBox bbox = FilterBoundingBox.attemptParseFromJSON(bboxJson, ogcServiceProviderType);

		try {
			// Get the mining activities
			WFSCountResponse response = this.mineralOccurrenceService.getMiningActivityCount(serviceUrl, mineName,
					startDate, endDate, oreProcessed, producedMaterial, cutOffGrade, production, maxFeatures, bbox);

			return generateJSONResponseMAV(true, new Integer(response.getNumberOfFeatures()), "");
		} catch (Exception e) {
			log.warn(String.format("Error performing filter for '%1$s': %2$s", serviceUrl, e));
			log.debug("Exception: ", e);
			return this.generateExceptionResponse(e, serviceUrl);
		}
	}

	/**
	 * Handles Mining Activity Style request queries Returns WFS response
	 * converted into KML.
	 *
	 * @param mineName
	 * @param startDate
	 * @param endDate
	 * @param oreProcessed
	 * @param producedMaterial
	 * @param cutOffGrade
	 * @param production
	 * @param bbox
	 * @param maxFeatures
	 */
	@RequestMapping("/doMiningActivityFilterStyle.do")
	public void doMiningActivityFilterStyle(HttpServletResponse response,
			@RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
			@RequestParam(required = false, value = "startDate", defaultValue = "") String startDate,
			@RequestParam(required = false, value = "endDate", defaultValue = "") String endDate,
			@RequestParam(required = false, value = "oreProcessed", defaultValue = "") String oreProcessed,
			@RequestParam(required = false, value = "producedMaterial", defaultValue = "") String producedMaterial,
			@RequestParam(required = false, value = "cutOffGrade", defaultValue = "") String cutOffGrade,
			@RequestParam(required = false, value = "production", defaultValue = "") String production,
			@RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		FilterBoundingBox bbox = null;

		String filter = this.mineralOccurrenceService.getMiningActivityFilter(mineName, startDate, endDate,
				oreProcessed, producedMaterial, cutOffGrade, production, maxFeatures, bbox);

		String style = this.getStyle(filter, "er:MiningFeatureOccurrence", "Mining Activity", Styles.MINING_ACTIVITY);

		response.setContentType("text/xml");

		ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
		OutputStream outputStream = response.getOutputStream();

		FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

		styleStream.close();
		outputStream.close();
	}

	/**
	 * Handles getting the style of the Earth Resource Mine filter queries. (If
	 * the bbox elements are specified, they will limit the output response to
	 * 200 records implicitly)
	 *
	 * @param mineName
	 *            the name of the mine to query for
	 * @param bbox
	 * @param maxFeatures
	 * @throws Exception
	 */
	@RequestMapping("/doMineFilterStyle.do")
	public void doMineFilterStyle(HttpServletResponse response,
			@RequestParam(required = false, value = "mineName", defaultValue = "") String mineName,
			@RequestParam(required = false, value = "bbox", defaultValue = "") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		FilterBoundingBox bbox = null;
		String filter = "";
		
		if (mineName != null && !mineName.isEmpty() ) {
			filter = this.mineralOccurrenceService.getMineFilter(mineName, bbox);
		}

		String style = this.getStyle(filter, "er:MiningFeatureOccurrence", "Mine", Styles.MINE);

		response.setContentType("text/xml");

		ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
		OutputStream outputStream = response.getOutputStream();

		FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

		styleStream.close();
		outputStream.close();
	}

	/**
	 * Handles counting the results of a Earth Resource MineralOccerrence style
	 * request query.
	 *
	 * @param commodityName
	 * @param bbox
	 * @param maxFeatures
	 *
	 * @throws Exception
	 */
	@RequestMapping("/doMineralOccurrenceFilterStyle.do")
	public void doMineralOccurrenceFilterStyle(HttpServletResponse response,
			@RequestParam(value = "commodityName", required = false) String commodityName,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		FilterBoundingBox bbox = null;

		String unescapeCommodityName = "";
		if (commodityName != null) {
			unescapeCommodityName = URLDecoder.decode(commodityName, "UTF-8");
		}
		String filter = this.mineralOccurrenceService.getMineralOccurrenceFilter(unescapeCommodityName, bbox);

		String style = this.getStyle(filter, "gsml:MappedFeature", "Mineral Occurrence", Styles.MINERAL_OCCURRENCE);

		response.setContentType("text/xml");

		ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
		OutputStream outputStream = response.getOutputStream();

		FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

		styleStream.close();
		outputStream.close();
	}

	/**
	 * Handles counting the results of a Earth Resource MineralOccerrence SF0
	 * view style request query.
	 *
	 * @param commodityName
	 * @param bbox
	 * @param maxFeatures
	 *
	 * @throws Exception
	 */
	@RequestMapping("/doMinOccurViewFilterStyle.do")
	public void doMinOccurViewFilterStyle(HttpServletResponse response,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(value = "commodityName", required = false) String commodityName,
			@RequestParam(required = false, value = "size") String size,
			@RequestParam(required = false, value = "minOreAmount") String minOreAmount,
			@RequestParam(required = false, value = "minReserves") String minReserves,
			@RequestParam(required = false, value = "minResources") String minResources,
			@RequestParam(required = false, value = "bbox") String bboxJson,
			@RequestParam(required = false, value = "maxFeatures", defaultValue = "0") int maxFeatures)
			throws Exception {

		FilterBoundingBox bbox = null;

		String unescapeCommodityName = "";
		if (commodityName != null) {
			unescapeCommodityName = URLDecoder.decode(commodityName, "UTF-8");
		}
		String filter = this.mineralOccurrenceService.getMinOccViewFilter(name, unescapeCommodityName, minOreAmount,
				minReserves, minResources, bbox);

		String style = this.getStyle(filter, EarthResourcesDownloadController.MINERAL_OCCURRENCE_VIEW_FEATURE_TYPE,
				"Mineral Occurrence", Styles.MINERAL_OCCURRENCE);

		response.setContentType("text/xml");

		ByteArrayInputStream styleStream = new ByteArrayInputStream(style.getBytes());
		OutputStream outputStream = response.getOutputStream();

		FileIOUtil.writeInputToOutputStream(styleStream, outputStream, 1024, false);

		styleStream.close();
		outputStream.close();
	}

	public String getStyle(String filter, String name, String title, Styles styles) {
		String style = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<StyledLayerDescriptor version=\"1.0.0\" xmlns:mo=\"http://xmlns.geoscience.gov.au/minoccml/1.0\" xmlns:er=\"urn:cgi:xmlns:GGIC:EarthResource:1.1\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
				+ "<NamedLayer>" + "<Name>" + name + "</Name>" + "<UserStyle>" + "<Name>portal-style</Name>" + "<Title>"
				+ name + "</Title>" + "<Abstract>EarthResource</Abstract>" + "<IsDefault>1</IsDefault>"
				+ "<FeatureTypeStyle>" + "<Rule>" + "<Name>" + name + "</Name>" + "<Title>" + title + "</Title>"
				+ "<Abstract>" + name + "</Abstract>" + filter + "<PointSymbolizer>" + "<Graphic>" + "<Mark>"
				+ "<WellKnownName>" + styles.shape + "</WellKnownName>" + "<Fill>" + "<CssParameter name=\"fill\">"
				+ styles.fillColour + "</CssParameter>" + "</Fill>" + "<Stroke>" + "<CssParameter name=\"stroke\">"
				+ styles.borderColour + "</CssParameter>" + "<CssParameter name=\"stroke-width\">" + styles.borderWidth
				+ "</CssParameter>" + "</Stroke>" + "</Mark>" + "<Size>" + styles.size + "</Size>" + "</Graphic>"
				+ "</PointSymbolizer>" + "</Rule>" + "</FeatureTypeStyle>" + "</UserStyle>" + "</NamedLayer>"
				+ "</StyledLayerDescriptor>";
		return style;
	}

}
