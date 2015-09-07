/**
 * Builds a form panel for generic filter that presents a list of all Layers in a WMSSelectors list and allows the user
 * to choose one from a drop-down. To set this up apply a org.auscope.portal.core.view.knownlayer.WMSSelectors as the
 * knownLayerSelector constructor-arg to the org.auscope.portal.core.view.knownlayer.KnownLayer bean (ie. in
 * auscope-known-layers.xml). Set the constructor arg 'LayersMode' for WMSSelectors to 'OR' and define each layer in a
 * list to the 'LayerNames' constructor arg. This was developed as part of GPT-41 where there are 'AND' layers and 'OR'
 * conjunction layers (this one).
 * 
 * @param {number}
 *            id of this formpanel instance
 * @param {string}
 *            the service url for submit
 */
Ext.define('auscope.layer.filterer.forms.GALayersDisjunction', {
    extend: 'portal.layer.filterer.BaseFilterForm',

    /**
     * Accepts a config for portal.layer.filterer.BaseFilterForm
     */
    constructor : function(config) {
        var cswRecords = config.layer.get('cswRecords');
        // This cswRecords holds everything I need for the "OR" Conjunction in the "Service Name"


        //Set up a map of admin areas + URL's that belong to each
//        var adminAreasMap = {};
       
        var layerList = [];
        var seenLayers = {};
        for (var i = 0; i < cswRecords.length; i++) {
            var adminArea = cswRecords[i].get('adminArea');
            var allOnlineResources = cswRecords[i].get('onlineResources');
//            var bhOnlineResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WFS, 'gsmlp:BoreholeView');

//            for (var j = 0; j < bhOnlineResources.length; j++) {
//                if (adminAreasMap[adminArea]) {
//                    adminAreasMap[adminArea].push(bhOnlineResources[j].get('url'));
//                } else {
//                    adminAreasMap[adminArea] = [bhOnlineResources[j].get('url')];
//                }
//            }
            //Set up a list of each Layer
            // var adminAreasList = [];
            for (var j = 0; j < allOnlineResources.length; j++) {
                var layerResource = allOnlineResources[j].data;
                console.log("Inspect Layer Resource: ",layerResource );
                if (layerResource.type === "WMS") {
                    if (!(layerResource.name in seenLayers)) {
                        seenLayers[layerResource.name] = true;
                        console.log("  new layer is Included");
                        layerList.push({
                            displayText : layerResource.description,
                            serviceFilter : layerResource.name
                        });
                    }
                }
            }
          
        }


        var adminAreasStore = Ext.create('Ext.data.Store', {
            fields: ['displayText', 'serviceFilter'],
            data : layerList
        });

        Ext.apply(config, {
            delayedFormLoading: false, //we don't need to use a delayed load as all of our info is ready now
            border: false,
            autoScroll: true,
            hideMode: 'offsets',
            width:'100%',
            buttonAlign: 'right',
            labelAlign: 'right',
            labelWidth: 70,
            bodyStyle: 'padding:5px',
            autoHeight: true,
            items: [{
                xtype:'fieldset',
                itemId: 'borehole-fieldset',
                title: 'Choose a layer',
                autoHeight : true,
                items: [{
                    xtype: 'combo',
                    anchor: '95%',
                    itemId: 'serviceFilter-field',
                    fieldLabel: 'Provider',
                    name: 'serviceFilter',
                    typeAhead: true,
                    triggerAction: 'all',
                    lazyRender:true,
                    mode: 'local',
                    store: adminAreasStore,
                    valueField: 'serviceFilter',
                    displayField: 'displayText',
                    hiddenName: 'serviceFilter'
                },{
                	xtype: 'hidden',
                	name: 'postMethod',
                	value: 'true'              	                
                }]
            }]
        });

        this.callParent(arguments);
    }
});
