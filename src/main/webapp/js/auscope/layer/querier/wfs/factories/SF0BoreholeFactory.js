/**
 * A factory for parsing a gsml:Borehole element.
 */
Ext.define('auscope.layer.querier.wfs.factories.SF0BoreholeFactory', {
    extend : 'portal.layer.querier.wfs.factories.BaseFactory',

    /**
     * Accepts all GenericParser.Factory.BaseFactory configuration options
     */
    constructor : function(cfg) {
        this.callParent(arguments);
    },

    supportsNode : function(domNode) {
        return (domNode.namespaceURI === this.XMLNS_GSMLP ||
                domNode.namespaceURI === 'http://xmlns.geosciml.org/geosciml-portrayal/4.0') &&
               (portal.util.xml.SimpleDOM.getNodeLocalName(domNode) === 'BoreholeView' ||
                portal.util.xml.SimpleDOM.getNodeLocalName(domNode) === 'gsmlp:BoreholeView');
    },

    /**
     * Generates a simple panel that represents the specified SF0 borehole node
     */
    parseNode : function(domNode, wfsUrl) {
        var bf = this;
        var gmlId = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, '@gml:id');
        var identifier = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:identifier');
        var name = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:name');
        var description = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:description');
        var purpose = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:purpose');
        var status = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:status');
        var drillingMethod = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:drillingMethod');
        var operator = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:operator');
        var driller = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:driller');
        var drillingStartDate = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:drillStartDate');
        var drillingEndDate = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:drillEndDate');
        var startPoint = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:startPoint');
        var inclinationType = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:inclinationType');
        var boreholeMaterialCustodian = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:boreholeMaterialCustodian');
        var boreholeLength = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:boreholeLength_m');
        var elevation_m = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:elevation_m');
        var elevation_srs = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:elevation_srs');
        var positionalAccuracy = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:positionalAccuracy');
        var source = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:source');
        var specification_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:specification_uri');
        var parentBorehole_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:parentBorehole_uri');
        var metadata_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:metadata_uri');
        var genericSymbolizer = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'gsmlp:genericSymbolizer');


        if(drillingStartDate){
            drillingStartDate = new Date(drillingStartDate.replace("Z", '')).toDateString();
        }else{
            drillingStartDate = "";
        }

        //generate items list - display the field only if it is not empty
        var items = [];
        if (name)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Name',
                value : this._makeGeneralPopupHtml(specification_uri, name, 'Click here for the raw Borehole Header WFS data')
            });
        if (description)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Description',
                value : description
            });
        if (purpose)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Purpose',
                value : purpose
            });
        if (status)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Status',
                value : status
            });
        if (drillingMethod)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Drilling Method',
                value : drillingMethod
            });
        if (operator)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Operator',
                value : operator
            });
        if (driller)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Driller',
                value : driller
            });
        if (drillingStartDate)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Drill Start Date',
                value : drillingStartDate
            });
        if (drillingEndDate)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Drill End Date',
                value : drillingEndDate
            });
        if (startPoint)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Start Point',
                value : startPoint
            });
        if (inclinationType)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Inclination Type',
                value : inclinationType
            });
        if (boreholeMaterialCustodian)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Borehole Material Custodian',
                value : boreholeMaterialCustodian
            });
       if (boreholeLength)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Borehole Length (m)',
                value : boreholeLength
            });
        if (elevation_m)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Elevation (m)',
                value : elevation_m
            });
       if (elevation_srs)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Elevation SRS',
                value : elevation_srs
            });
        if (positionalAccuracy)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Psitional Accuracy',
                value : positionalAccuracy
            });
        if (source)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Source',
                value : source
            });
       if (specification_uri)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Specification URI',
                value : this._makeGeneralPopupHtml(specification_uri, specification_uri, 'Click here for the raw Borehole Header WFS data')
            });
       if (parentBorehole_uri)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Parent Borehole URI',
                value : parentBorehole_uri
            });
       if (metadata_uri)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Metadata URI',
                value : this._makeGeneralPopupHtml(metadata_uri, metadata_uri, 'Click here for the metadata record for the Borehole')
            });
       if (genericSymbolizer)
            items.push({
                xtype : 'displayfield',
                fieldLabel : 'Generic Symbolizer',
                value : genericSymbolizer
            });




        //Build our component
        return Ext.create('portal.layer.querier.BaseComponent', {
            overrideInfoWindowSize : {
                width : 600,
                height : 400
            },
            border : false,
            tabTitle : name,  
            header : false,
            layout : 'fit',
            items : [{
                xtype : 'fieldset',
                title : 'Portrayal Borehole View',
                labelWidth : 120,
                autoScroll : true,
                items   : items
            }],
            buttonAlign : 'right',
            buttons : [{
                text : 'Download XML',
                iconCls : 'download',
                handler : function() {
                    var getXmlUrl = bf._makeFeatureRequestUrl(wfsUrl, 'gsmlp:BoreholeView', gmlId);
                    var url = 'downloadGMLAsZip.do?serviceUrls=' + escape(getXmlUrl);
                    portal.util.FileDownloader.downloadFile(url);
                }
            },{
                text : 'Download CSV',
                iconCls : 'download',
                handler : function() {
                    var optionalParam1={
                      key : "outputFormat",
                      value : "csv"
                    };

                    var optionalParam2={
                            key : "srsName",
                            value : "EPSG:4326"
                          };

                    var optionalParams=[optionalParam1,optionalParam2];

                    var getXmlUrl = bf._makeWFSFeatureRequestUrl(wfsUrl, 'gsmlp:BoreholeView', gmlId,optionalParams);
                    var url = 'downloadDataAsZip.do?filename=csvdownloads.zip&serviceUrls=' + escape(getXmlUrl);
                    portal.util.FileDownloader.downloadFile(url);
                }
            }]
        });
    }
});