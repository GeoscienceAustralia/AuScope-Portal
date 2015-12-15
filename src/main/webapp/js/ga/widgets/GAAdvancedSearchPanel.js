/**
 * This is the panel for the advanced search dialog.
 */
Ext.define('ga.widgets.GAAdvancedSearchPanel', {
    extend : 'Ext.form.Panel',
    alias: 'widget.gaadvancedsearchpanel',
    map : null,
    areaMapStore : null,
    yearStore : null,
    keywordIDCounter : 0,
    spacerHeight : 22,
    miniMap : null,
    boxLayer : null,
    me: null,
    
    constructor : function(cfg){   
        
        me = this;
        
        this.map = cfg.map;      
        
        this.areaMapStore = new Ext.data.Store({
            autoload: true,
            fields: [
                {name: 'Name', type: 'string'},
                {name: 'WestLon', type: 'float'},
                {name: 'NorthLat', type: 'float'}
            ],
            proxy : {
                type : 'ajax',
                url: 'getAreaMaps.do',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            }
        });
 
        var years = [];

        y = 1900
        while (y <= new Date().getFullYear()){
            years.push([y]);
            y++;
        }
        
        this.yearStore = new Ext.data.SimpleStore
        ({
            fields : ['year'],
            data : years
        });
        
        
        Ext.apply(cfg, {
            xtype : 'form',
            id : 'personalpanelcswfilterform',
            width : 500,
            autoScroll :true,
            border : false,
            //height : 520,
            
            fieldDefaults: {
                msgTarget: 'side',
                autoFitErrors: false
            },
            
            items : [
            {
                 xtype:'tabpanel',
                 layout: 'fit',
                 items : [
                     this.dataProvidersTab(),
                     this.moreSearchFiltersTab()                 
                 ]
            }]
        });
        
        // validator for the year range fields - from year must be before to
        // year
        Ext.apply(Ext.form.field.VTypes, {
            // vtype validation function
            yearRange: function(year, field) {
                var isValid = true;

                // if this field has a defined yearTo field then we are
                // validating the yearFrom field
                if (field.toYearField) {
                    var toYear = field.up('form').down('#' + field.toYearField).getValue();           
                    if (toYear) {
                        isValid = year <= toYear;
                    }                    
                }
                // else we are validating the yearFrom field
                if (field.fromYearField) {
                    var fromYear = field.up('form').down('#' + field.fromYearField).getValue();
                    if (fromYear) {
                        isValid = year >= fromYear;
                    }
                }
               
                return isValid;
            },
            
            // validate decimal numbers
            isFloat: function(value, field) {
                return value && parseFloat(value); 
            },
            
            // vtype Text property: The error text to display when the validation function returns false
            yearRangeText: 'from year must be before to year',
            isFloatText: 'value must be a valid decimal number'
        });

        this.callParent(arguments);
    },

    // tab where the user can select which registries to search on
    dataProvidersTab : function(){
        var me = this;
        var registriesTab = {
                title : '1. Choose Data Provider(s)',
                xtype : 'panel',
                style: 'padding: 5px',
                type: 'vbox',
                items:[{
                   xtype: 'displayfield',
                   value: 'Please select one or more data registries to search:'
                }, {
                    xtype: 'checkboxgroup',
                    name : 'cswServiceId',
                    id : 'registryTabCheckboxGroup',
                    fieldLabel: 'Registries',
                    labelStyle: 'visibility: hidden',
                    columns: 1,
                    vertical: true                    
                }]
        };

        var checkBoxItems = [];

        var cswServiceItemStore = new Ext.data.Store({
            model   : 'portal.widgets.model.CSWServices',
            proxy : {
                type : 'ajax',
                url : 'getCSWServices.do',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            },
            listeners : {
                load  :  function(store, records, successful, eopts){
                    for (var i = 0; i < records.length; i++) {
                        var cswServiceItemRec = records[i];
                        var title = cswServiceItemRec.get('title');
                        checkBoxItems.push({
                            boxLabel : title,
                            name : 'cswServiceId',
                            inputValue: cswServiceItemRec.get('id'),
                            checked : title.toLowerCase().includes('geoscience')
                        });
                    }
                    var registryTabCheckboxGroup=Ext.getCmp('registryTabCheckboxGroup');
                    registryTabCheckboxGroup.add(checkBoxItems);
                    registryTabCheckboxGroup.allowBlank = false;
                    registryTabCheckboxGroup.blankText = 'At least one registry must be selected for the search';
                }
            }

        });
        cswServiceItemStore.load();

        return registriesTab;
    },                                                           
    
    
    moreSearchFiltersTab : function() {
       
        var generalTab = {
                title : '2. Add More Search Filters',
                layout:{
                    type:'anchor',
                    defaultMargins: '0 5 0 0'
                },
                items : [{
                    items : [         
                    {                        
                        xtype : 'fieldset',
                        title:'Metadata',
                        flex : 1,
                        items : [{
                            xtype : 'textfield',
                            name : 'titleOrAbstract',
                            itemId: 'titleOrAbstract',
                            fieldLabel : 'Title/Abstract'
                        },{
                            xtype : 'textareafield',
                            name : 'keywords',
                            itemId: 'keywords',
                            fieldLabel : 'Keyword(s)',     
                        },{
                            xtype : 'textfield',
                            name : 'authorSurname',
                            itemId: 'authorSurname',
                            fieldLabel : 'Author surname'
                        },
                        {                        
                            xtype: 'fieldset',
                            flex:1,
                            layout: 'vbox',
                            title: 'Publication year',
                            style: 'float:left; border: 1px light-gray solid',
                            items: [{
                                xtype: 'combobox',
                                fieldLabel: 'from',
                                name: 'publicationDateFrom',
                                itemId: 'publicationDateStart',
                                vtype: 'yearRange',
                                toYearField: 'publicationDateEnd',
                                valueField: 'year',
                                displayField: 'year',
                                store: this.yearStore,
                                minChars: 0,
                                queryMode: 'local',
                                typeAhead: true,
                                width: 200
                            },{
                                xtype: 'combobox',
                                fieldLabel: 'to',
                                name: 'publicationDateTo',
                                itemId: 'publicationDateEnd',
                                vtype: 'yearRange',
                                fromYearField: 'publicationDateStart',
                                reference: 'publicationDateEnd',
                                valueField: 'year',
                                displayField: 'year',
                                store: this.yearStore,
                                minChars: 0,
                                queryMode: 'local',
                                typeAhead: true,
                                width: 200                                
                            }
                            ]
                        }
                        ]                   
                    },
                    {
                        xtype: 'box',
                        width: 400,
                        autoEl: {
                            tag: 'hr',
                            style: 'border:1px gray dashed'
                        }
                    },
                    {
                        xtype : 'fieldset',
                        title:'Select by location on the map',
                        flex : 1,
                        items : [{
                           xtype: 'panel',
                           border: false,
                           layout:{
                               type:'vbox',
                               align:'center'
                           },
                           items: [{
                               xtype: 'button',
                               scale: 'small',
                               text: 'Draw area on map',
                               handler: this.allowUserToDrawBoundingBox
                           },{
                               xtype: 'button',
                               scale: 'small',
                               margin: '5 0 0 0',
                               text: 'Use current map extent',
                               handler: this.populateCoordinatesFromCurrentMapExtent
                           }]
                        },{
                            html: 'Or choose a 1:250,000 map area:',
                            border: false
                            
                        },{
                            xtype: 'combobox',
                            fieldLabel: '1:250000 map area select box',
                            name: 'mapAreaSelect',
                            itemId: 'mapAreaSelect',
                            valueField: 'Name',
                            displayField: 'Name',
                            store: this.areaMapStore,
                            minChars: 0,
                            queryMode: 'remote',
                            typeAhead: true,
                            width: 300,
                            hideLabel: true,
                            listeners: {
                                select: this.populateCoordinatesFromAreaMap,
                                keyup: function(combo, event) {
                                    var key = String.fromCharCode(event.getKey()),
                                        boundList = combo.getPicker(),
                                        store = boundList.getStore(),
                                        record = store.findRecord(combo.displayField, key);
                                    if (record) {
                                        boundList.highlightItem(boundList.getNode(record));
                                        this.populateCoordinatesFromAreaMap(combo, record);
                                    } else {
                                    	combno.value = '';
                                    }
                                }
                            }
                        }, {
                            xtype: 'panel',
                            layout: 'vbox',
                            border: false,
                            items: [{
                                xtype: 'panel',
                                border: false,
                                html: 'Or type in a search area:<br/>(latitude-longitude, decimal degrees)',  
                                margin: '0 5 0 0'    
                            },{
                                xtype: 'textfield',
                                name : 'north',
                                itemId: 'north',
                                vtype: 'isFloat',
                                fieldLabel : 'North',                                    
                            },{
                                xtype: 'textfield',
                                name : 'east',
                                itemId: 'east',
                                vtype: 'isFloat',
                                fieldLabel : 'East',                                    
                            },{
                                xtype: 'textfield',
                                name : 'south',
                                itemId: 'south',
                                vtype: 'isFloat',
                                fieldLabel : 'South',                                    
                            },{
                                xtype: 'textfield',
                                name : 'west',
                                itemId: 'west',
                                vtype: 'isFloat',
                                fieldLabel : 'West',                                    
                            }
                            ]
                        }]  
                    },
                    {
                        xtype: 'box',
                        width: 400,
                        autoEl: {
                            tag: 'hr',
                            style: 'border:1px gray dashed'
                        }
                    },
                    {

                        xtype: 'radiofield',
                        name: 'sortType',
                        inputValue: 'serviceDefault',
                        itemId: 'defaultSortTypeRadio',
                        fieldLabel: 'Sort results by',
                        boxLabel: 'Best match (default)',
                        value: true
                    }, {
                        xtype: 'radiofield',
                        name: 'sortType',
                        inputValue: 'title',
                        fieldLabel: '',
                        labelSeparator: '',
                        hideEmptyLabel: false,
                        boxLabel: 'Title (A - Z)'
                    },
                    {
                        xtype: 'radiofield',
                        name: 'sortType',
                        inputValue: 'publicationDate',
                        fieldLabel: '',
                        labelSeparator: '',
                        hideEmptyLabel: false,
                        boxLabel: 'Publication date (newest first)'
                    }]
                }              
              ]
        };

        return generalTab;
    },

    // I copied this function from OpenLayersMap.js, including Josh's horrible horrible workaround
    // which, since it made my component work, I think is pretty sweet actually
    _getNewVectorLayer : function(){
        var vectorLayer = new OpenLayers.Layer.Vector("Vectors", {
            preFeatureInsert: function(feature) {
                // Google.v3 uses web mercator as projection, so we have to
                // transform our coordinates

                var bounds = feature.geometry.getBounds();

                //JJV - Here be dragons... this is a horrible, horrible workaround. I am so very sorry :(
                //Because we want to let portal core *think* its in EPSG:4326 and because our base map is in EPSG:3857
                //we automagically transform the geometry on the fly. That isn't a problem until you come across
                //various openlayers controls that add to the map in the native projection (EPSG:3857). To workaround this
                //we simply don't transform geometry that's already EPSG:3857. The scary part is how we go about testing for that...
                //The below should work except for tiny bounding boxes off the west coast of Africa
                if (bounds.top <= 90 && bounds.top >= -90) {
                    feature.geometry.transform('EPSG:4326','EPSG:3857');
                }
            },
            displayInLayerSwitcher : false
        });
        me.map.map.addLayer(vectorLayer);
        return vectorLayer;
    },
    
    /* Creates a box drawing tool in the map and activates it */
    allowUserToDrawBoundingBox : function(button) {
        
        this._drawCtrlVectorLayer = me._getNewVectorLayer();
        
        var drawControl = new OpenLayers.Control.DrawFeature(
                this._drawCtrlVectorLayer,
                OpenLayers.Handler.RegularPolygon, {
                    handlerOptions: {
                        sides: 4,
                        irregular: true
                    }
                });
        
        me.map.map.addControl( drawControl );         
        
        drawControl.events.register('featureadded', drawControl, boundingBoxCallback);
        drawControl.activate();    
        
        function boundingBoxCallback(evt) {
            var feature = evt.feature;
            
            feature.layer.setMap(me.map.map);
                    
            var originalBounds = feature.geometry.getBounds();
            var bounds = originalBounds.transform('EPSG:3857','EPSG:4326').toArray();
            
            // Adjust for the meridien if necessary
            var west = bounds[0] >= 180 ? -180 - (180-bounds[0]) : bounds[0];
            var east = bounds[2] >= 180 ? -180 - (180-bounds[2]) : bounds[2];            
            
            Ext.ComponentQuery.query('#north')[0].setValue(bounds[3]); 
            Ext.ComponentQuery.query('#east')[0].setValue(east); 
            Ext.ComponentQuery.query('#south')[0].setValue(bounds[1]); 
            Ext.ComponentQuery.query('#west')[0].setValue(west);  
            
            me.map.map.removeLayer(feature.layer);
            drawControl.deactivate();    
            me.map.map.removeControl( drawControl );   
            
        };
    
    },
    
    /* Fills the bounding box fields with coordinates from the current map bounds */
    populateCoordinatesFromCurrentMapExtent : function(button) {
        var extent = me.map.map.getExtent();
        var bounds = extent.transform('EPSG:3857','EPSG:4326').toArray();
        
        // Adjust for the meridien if necessary
        var west = bounds[0] >= 180 ? -180 - (180-bounds[0]) : bounds[0];
        var east = bounds[2] >= 180 ? -180 - (180-bounds[2]) : bounds[2];  
        
        Ext.ComponentQuery.query('#north')[0].setValue(bounds[3]); 
        Ext.ComponentQuery.query('#east')[0].setValue(east); 
        Ext.ComponentQuery.query('#south')[0].setValue(bounds[1]); 
        Ext.ComponentQuery.query('#west')[0].setValue(west);  
    },
    
    /* 
     * Fills the bounding box fields with coordinates from a 1:250K Area Map 
     */
    populateCoordinatesFromAreaMap : function(combo, record) {
        
        // compute the south and east points based these maps being 1.5 degrees longitude-wide and 1 degree latitude-tall
        var west = record.data['WestLong'];
        var east = west + 1.5;
        var north = record.data['NorthLat'];
        var south = north - 1;
                
        Ext.ComponentQuery.query('#north')[0].setValue(north); 
        Ext.ComponentQuery.query('#east')[0].setValue(east); 
        Ext.ComponentQuery.query('#south')[0].setValue(south); 
        Ext.ComponentQuery.query('#west')[0].setValue(west);          
    },
    
    resetForm : function() {
        Ext.ComponentQuery.query('#titleOrAbstract')[0].setValue(''); 
        Ext.ComponentQuery.query('#keywords')[0].setValue(''); 
        Ext.ComponentQuery.query('#authorSurname')[0].setValue(''); 
        Ext.ComponentQuery.query('#publicationDateStart')[0].setValue(''); 
        Ext.ComponentQuery.query('#publicationDateEnd')[0].setValue('');                         
        Ext.ComponentQuery.query('#mapAreaSelect')[0].setValue('');  
        Ext.ComponentQuery.query('#north')[0].setValue(''); 
        Ext.ComponentQuery.query('#south')[0].setValue(''); 
        Ext.ComponentQuery.query('#east')[0].setValue(''); 
        Ext.ComponentQuery.query('#west')[0].setValue(''); 
        Ext.ComponentQuery.query('#defaultSortTypeRadio')[0].setValue(true);  
    } 
    
});