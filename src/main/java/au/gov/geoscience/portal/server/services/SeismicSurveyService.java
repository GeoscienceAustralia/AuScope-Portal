package au.gov.geoscience.portal.server.services;

import java.io.InputStream;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.services.methodmakers.CSWMethodMakerGetDataRecords;
import org.auscope.portal.core.services.methodmakers.CSWMethodMakerGetDataRecords.ResultType;
import org.auscope.portal.core.services.methodmakers.filter.csw.CSWGetDataRecordsFilter;
import org.auscope.portal.core.services.namespaces.CSWNamespaceContext;
import org.auscope.portal.core.services.responses.csw.CSWRecord;
import org.auscope.portal.core.services.responses.csw.CSWRecordTransformer;
import org.auscope.portal.core.services.responses.csw.CSWRecordTransformerFactory;
import org.auscope.portal.core.util.DOMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class SeismicSurveyService {

    private HttpServiceCaller serviceCaller;

    private CSWMethodMakerGetDataRecords methodMaker;

    private CSWRecordTransformerFactory transformerFactory;

    // ----------------------------------------------------------- Constructors
    @Autowired
    public SeismicSurveyService(HttpServiceCaller serviceCaller) {
        this.serviceCaller = serviceCaller;
        this.methodMaker = new CSWMethodMakerGetDataRecords();
        this.transformerFactory = new CSWRecordTransformerFactory();

    }

    public CSWRecord getCSWRecord(String recordNumber, String url) throws Exception {

        CSWGetDataRecordsFilter alternateIdentifierFilter = new CSWGetDataRecordsFilter();
        alternateIdentifierFilter.setAlternateIdentifier(recordNumber);

        HttpRequestBase method = methodMaker.makeMethod(url, alternateIdentifierFilter, ResultType.Results, 1);

        InputStream responseString = this.serviceCaller.getMethodResponseAsStream(method);

        Document responseDoc = DOMUtil.buildDomFromStream(responseString);

        CSWNamespaceContext nc = new CSWNamespaceContext();
        XPathExpression exprRecordMetadata = DOMUtil
                .compileXPathExpr("/csw:GetRecordsResponse/csw:SearchResults/gmd:MD_Metadata", nc);

        NodeList nodes = (NodeList) exprRecordMetadata.evaluate(responseDoc, XPathConstants.NODESET);

        Node metadataNode = nodes.item(0);
        CSWRecordTransformer transformer = transformerFactory.newCSWRecordTransformer(metadataNode);
        CSWRecord newRecord = transformer.transformToCSWRecord();

        return newRecord;

    }

}
