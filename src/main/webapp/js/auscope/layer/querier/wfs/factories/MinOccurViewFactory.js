/**
 * A factory for parsing a er:MiningFeatureOccurrence element.
 */
Ext.define('auscope.layer.querier.wfs.factories.MinOccurViewFactory', {
    extend : 'portal.layer.querier.wfs.factories.BaseFactory',

    /**
     * Accepts all GenericParser.Factory.BaseFactory configuration options
     */
    constructor : function(cfg) {
        this.callParent(arguments);
    },

    supportsNode : function(domNode) {
        return domNode.namespaceURI === this.XMLNS_MO &&
               portal.util.xml.SimpleDOM.getNodeLocalName(domNode) === 'MinOccView';
    },

    /**
     * Generates a simple tree panel that represents the specified node
     */
    parseNode : function(domNode, wfsUrl) {
        var bf = this;

        var gmlId = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, '@gml:id');

        var name = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:name');
        var earthResourceType = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:earthResourceType');
        var commodity = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:commodity');
        var totalOreAmount = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:totalOreAmount');
        var totalReserves = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:totalReserves');
        var totalResources = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:totalResources');
        var observationMethod = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:observationMethod');
        var positionalAccuracy = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:positionalAccuracy');
        var earthResourceType_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:earthResourceType_uri');
        var commodityClassifier_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:commodityClassifier_uri');
        var representativeAge_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:representativeAge_uri');
        var representativeOlderAge_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:representativeOlderAge_uri');
        var representativeYoungerAge_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:representativeYoungerAge_uri');
        var mineralDepositModel_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:mineralDepositModel_uri');
        var hostGeologicUnit_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:hostGeologicUnit_uri');
        var earthResourceSpecification_uri = portal.util.xml.SimpleXPath.evaluateXPathString(domNode, 'mo:earthResourceSpecification_uri');


        //Build our component
        return Ext.create('portal.layer.querier.BaseComponent', {
            border : false,
            tabTitle : name,
            layout : 'fit',
            items : [{
                xtype : 'fieldset',
                title : 'Mineral Occurrence View',
                margin : '0 20 0 0',
                autoScroll : true,
                items : [{
                    xtype : 'displayfield',
                    fieldLabel : 'Name',
                    value : name
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Type',
                    value : earthResourceType
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Commodity',
                    value : commodity
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Total Ore Amount',
                    value : totalOreAmount
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Total Reserves',
                    value : totalReserves
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Total Resources',
                    value : totalResources
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Observation Method',
                    value : observationMethod
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Positional Accuracy',
                    value : positionalAccuracy
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'EarthResource Type URI',
                    value : earthResourceType_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Commodity Classifier URI',
                    value : commodityClassifier_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Age URI',
                    value : representativeAge_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Older Age URI',
                    value : representativeOlderAge_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Younger Age URI',
                    value : representativeYoungerAge_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'MineralDeposit Model URI',
                    value : mineralDepositModel_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'GeologicUnit URI',
                    value : hostGeologicUnit_uri
                },{
                    xtype : 'displayfield',
                    fieldLabel : 'Specification URI',
                    value : this._makeWfsUriPopupHtml(earthResourceSpecification_uri, earthResourceSpecification_uri, 'Click here for more information about this feature.')
                }]
            }],
            buttonAlign : 'right',
            buttons : [{
                text : 'Download Feature',
                iconCls : 'download',
                margin : '0 20 0 0',
                handler : function() {
                    var getXmlUrl = bf._makeFeatureRequestUrl(wfsUrl, domNode.nodeName, gmlId);
                    portal.util.FileDownloader.downloadFile('downloadGMLAsZip.do',{
                        serviceUrls : getXmlUrl
                    });
                }
            }]
        });
    }
});