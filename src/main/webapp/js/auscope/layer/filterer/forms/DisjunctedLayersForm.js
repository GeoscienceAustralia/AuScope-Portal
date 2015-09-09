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
Ext.define('auscope.layer.filterer.forms.DisjunctedLayersForm', {
    extend : 'portal.layer.filterer.BaseFilterForm',

    /**
     * Accepts a config for portal.layer.filterer.BaseFilterForm
     */
    constructor : function(config) {
        var cswRecords = config.layer.get('cswRecords');

        var filterer = config.layer.get('filterer');

        var sliderHandler = function(caller, newValue) {
            filterer.setParameter('opacity', newValue);
        };

        if (!filterer.getParameter('opacity')) {
            filterer.setParameter('opacity', 1, true);
        }

        // Layers to add to Model to show
        var layerList = [];
        // seenLayers is to prevent multiple of the same layer being entered in the list
        var seenLayers = {};
        for (var i = 0; i < cswRecords.length; i++) {
            var allOnlineResources = cswRecords[i].get('onlineResources');

            for (var j = 0; j < allOnlineResources.length; j++) {
                var layerResource = allOnlineResources[j].data;
                if (layerResource.type === "WMS") {
                    if (!(layerResource.name in seenLayers)) {
                        seenLayers[layerResource.name] = true;
                        layerList.push({
                            displayText : layerResource.description,
                            serviceFilter : layerResource.name
                        });
                    }
                }
            }

        }

        var adminAreasStore = Ext.create('Ext.data.Store', {
            fields : [ 'displayText', 'serviceFilter' ],
            data : layerList
        });

        Ext.apply(config, {
            delayedFormLoading : false, // we don't need to use a delayed load as all of our info is ready now
            border : false,
            autoScroll : true,
            hideMode : 'offsets',
            width : '100%',
            buttonAlign : 'right',
            labelAlign : 'right',
            labelWidth : 70,
            bodyStyle : 'padding:5px',
            autoHeight : true,
            items : [ {
                xtype : 'slider',
                fieldLabel : 'Opacity',
                name : 'opacity',
                width : '100%',
                minValue : 0,
                increment : 0.01,
                decimalPrecision : false,
                maxValue : 1,
                value : config.layer.get('filterer').getParameter('opacity'),
                listeners : {
                    changecomplete : sliderHandler
                }
            }, {
                xtype : 'fieldset',
                itemId : 'borehole-fieldset',
                title : 'Choose a layer',
                autoHeight : true,
                items : [ {
                    xtype : 'combo',
                    anchor : '95%',
                    itemId : 'serviceFilter-field',
                    fieldLabel : 'Layer',
                    name : 'serviceFilter',
                    typeAhead : true,
                    triggerAction : 'all',
                    lazyRender : true,
                    mode : 'local',
                    store : adminAreasStore,
                    valueField : 'serviceFilter',
                    displayField : 'displayText',
                    hiddenName : 'serviceFilter'
                }, {
                    xtype : 'hidden',
                    name : 'postMethod',
                    value : 'true'
                } ]
            } ]
        });

        this.callParent(arguments);
    }
});
