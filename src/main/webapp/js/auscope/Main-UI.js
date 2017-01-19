// modify comment
Ext.Loader.setConfig({
    enabled: true,
});

Ext.override(Ext.Window, {
    constrainHeader: true,
    modal: true
});

Ext.application({
    name : 'portal',

    //Here we build our GUI from existing components - this function should only be assembling the GUI
    //Any processing logic should be managed in dedicated classes - don't let this become a
    //monolithic 'do everything' function
    launch : function() {

        //Send these headers with every AJax request we make...
        Ext.Ajax.defaultHeaders = {
            'Accept-Encoding': 'gzip, deflate' //This ensures we use gzip for most of our requests (where available)
        };

        // default baseLayer to load (layer.name)
        var defaultBaseLayer = "Google Satellite";

        // Handle deserialisation
        // if we have a uri param called "state" then we'll use that to deserialise.
        // else if we have a value in localStorage called "portalStorageApplicationState" then use that
        // otherwise do nothing special
        var deserializationHandler, decodedString, decodedVersion, useStoredState = true;

        // separating the GET parameters from the current URL
        var urlParams = this.getParameters(window.location.href);
        if (urlParams['s'] && urlParams['v']) {
            decodedString = urlParams['s'];
            decodedVersion = urlParams['v'];
            useStoredState = false;
        } else {
            if(typeof(Storage) !== "undefined") {
                decodedString = localStorage.getItem("portalStorageApplicationState");
                var serialisedBaseLayer = localStorage.getItem("portalStorageDefaultBaseLayer");
                if (serialisedBaseLayer) {
                    defaultBaseLayer = serialisedBaseLayer;
                }
                decodedVersion = null;
            }
        }

        // main div
        var body = Ext.getBody();

        // WARNING - Terry IS playing dangerous games here!
        // if !(oldBrowser) then ....
        Ext.Ajax.method = 'GET';
        //True to add a unique cache-buster param to GET requests. Defaults to true.
        //http://www.sencha.com/forum/showthread.php?257086-Is-there-a-simple-way-to-disable-caching-for-an-entire-ExtJS-4-application
        Ext.data.Connection.disableCaching = false;
        Ext.data.proxy.Server.prototype.noCache = false;
        Ext.Ajax.disableCaching = false;
        // I think this is 300 seconds, 5 mins...
        Ext.Ajax.timeout = 300000;
        //IF END

        var urlParams = Ext.Object.fromQueryString(window.location.search.substring(1));
        var isDebugMode = urlParams.debug;

        //Create our CSWRecord store (holds all CSWRecords not mapped by known layers)
        var unmappedCSWRecordStore = Ext.create('Ext.data.Store', {
            model : 'portal.csw.CSWRecord',
            groupField: 'contactOrg',
            proxy : {
                type : 'ajax',
                url : 'getUnmappedCSWRecords.do',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            },
            autoLoad : true
        });

        // data store for service endpoints available for searching for CSW records
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
            autoLoad : true
        });


        //Our custom record store holds layers that the user has
        //added to the map using a OWS URL entered through the
        //custom layers panel
        var customRecordStore = Ext.create('Ext.data.Store', {
            model : 'portal.csw.CSWRecord',
            proxy : {
                type : 'ajax',
                url : 'getCustomLayers.do',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            },
            autoLoad : false,
            data : [],
            listeners : {
                load  :  function(store, records, successful, eopts){
                    if(!successful) {
                    	var errStr = "";
                    	var idx;
                    	
                    	// Compile the returned error message
                    	for (idx=0; idx<records.length; idx++) {
                    	    if (records[idx].hasOwnProperty('data')) {
                    	        errStr += records[idx].data;
                    	    }
                    	}
                    	
                    	// To prevent injection issues
                    	REPLACE_LIST = [/\'/g, /\"/g, /\=/g, /\</g, /\>/g, /\&/g, /\)/g, /\(/g, /\$/g, /\;/g, /\[/g, /\]/g, /\{/g, /\}/g];
                    	for (var i=0;i<REPLACE_LIST.length;i++) {
                    	    errStr = errStr.replace(REPLACE_LIST[i],"");
                    	}

                    	// If there was no returned error message, then use the generic message
                    	if (errStr.length===0) {
                    	    errStr =  "Your WMS service has to support EPSG:3857 or EPSG:4326 to be supported by Google map. You are seeing this error because either the URL is not valid or it does   not conform to EPSG:3857 or EPSG:4326 WMS layers standards"                       
                    	}
                    	
                    	if (errStr=="Your WMS does not appear to support EPSG:3857 WGS 84 / Pseudo-Mercator or EPSG:4326 WGS 84. This is required to be able to display your map in this Portal. If you are certain that your service supports EPSG:3857, click Yes for portal to attempt loading of the layer else No to exit.") {
                    	    // Display error message
                    	    Ext.Msg.show({
                    	        title:'Error!',
                    	        msg: errStr,
                    	        buttons: Ext.Msg.YESNO,
                    	        fn: function(btn) {
                    	              if (btn=='yes') {
                    	                  // This re-sends the URL to the server, but with fewer checks
                    	                  customRecordsPanel.getDockedComponent(2).items.getAt(1).searchClick(true);
                    	              }
                    	        }                                      
                    	    });
                    	} else {
                    	    // Display error message
                    	    Ext.Msg.show({
                    	        title:'Error!',
                    	        msg: errStr,
                    	        buttons: Ext.Msg.OK
                    	    });
                    	}
                    	
                    }else{
                        if(records.length === 0){
                            Ext.Msg.show({
                                title:'No WMS Layers!',
                                msg: 'There are no WMS Layers in the given URL',
                                buttons: Ext.Msg.OK
                            });
                        }
                    }
                }
            }
        });


        //Create our KnownLayer store
        var layersSorter = new Ext.util.Sorter({
            sorterFn: function(record1, record2) {
                var order1 = record1.data.order;
                    order2 = record2.data.order;
                return order1 > order2 ? 1 : (order1 < order2 ? -1 : 0);
            },
            direction: 'ASC'
        })
        var layersGrouper = new Ext.util.Grouper({
            groupFn: function(item) {
                return item.data.group;
            },
            sorterFn: function(record1, record2) {
                var order1 = record1.data.order;
                    order2 = record2.data.order;
                return order1 > order2 ? 1 : (order1 < order2 ? -1 : 0);
            },
            direction: 'ASC'
        })
        var knownLayerStore = Ext.create('Ext.data.Store', {
            model : 'portal.knownlayer.KnownLayer',
            proxy : {
                type : 'ajax',
                url : 'getKnownLayers.do',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            },
            sorters: [layersSorter],
            grouper: layersGrouper,
            autoLoad : true
        });

        //Create our store for holding the set of layers that have been added to the map
        var activeLayerStore = Ext.create('portal.layer.LayerStore');
        
        //Create our map implementations
        var mapCfg = {
            container : null,   //We will be performing a delayed render of this map
            layerStore : activeLayerStore,
            baseLayerName : defaultBaseLayer,
            portalIsHandlingLayerSwitcher : true
        };
        var urlParams = Ext.Object.fromQueryString(window.location.search.substring(1));

        var map = null;

        map = Ext.create('ga.map.openlayers.GAOpenLayersMap', mapCfg);         
        
        var defaultLayerFactory = Ext.create('portal.layer.LayerFactory', {
            map : map,
            formFactory : Ext.create('auscope.layer.filterer.GAFormFactory', {map : map, showWMSFilter : false}),
            downloaderFactory : Ext.create('auscope.layer.AuScopeDownloaderFactory', {map: map}),
            querierFactory : Ext.create('auscope.layer.AuScopeQuerierFactory', {map: map}),
            rendererFactory : Ext.create('auscope.layer.AuScopeRendererFactory', {map: map})
        });  

        var activeLayerFactory = Ext.create('portal.layer.LayerFactory', {
            map : map,
            formFactory : Ext.create('auscope.layer.filterer.GAFormFactory', {map : map, showWMSFilter : true}),
            downloaderFactory : Ext.create('auscope.layer.AuScopeDownloaderFactory', {map: map}),
            querierFactory : Ext.create('auscope.layer.AuScopeQuerierFactory', {map: map}),
            rendererFactory : Ext.create('auscope.layer.AuScopeRendererFactory', {map: map})
        });

        var customLayerFactory = Ext.create('portal.layer.LayerFactory', {
            map : map,
            formFactory : Ext.create('auscope.layer.filterer.GAFormFactory', {map : map, showWMSFilter : true}),
            //downloaderFactory : Ext.create('auscope.layer.AuScopeDownloaderFactory', {map: map}),
            querierFactory : Ext.create('auscope.layer.AuScopeQuerierFactory', {map: map}),
            rendererFactory : Ext.create('auscope.layer.AuScopeRendererFactory', {map: map})
        });
        

        
        var knownLayersPanel = Ext.create('auscope.widgets.panel.GAKnownLayerPanel', {
            title : 'Featured',
            id: 'knownLayersPanel',
            store : knownLayerStore,
            map : map,
            layerFactory : defaultLayerFactory,
            onlineResourcePanelType : 'gaonlineresourcespanel',
            serviceInformationIcon: 'img/information.png',
            mapExtentIcon: 'img/extent3.png',
            tooltip : {
                anchor : 'top',
                title : 'Featured Layers',
                text : '<p>This is where the portal groups data services with a common theme under a layer. This allows you to interact with multiple data providers using a common interface.</p><br><p>The underlying data services are discovered from a remote registry. If no services can be found for a layer, it will be disabled.</p>',
                showDelay : 100,
                icon : 'img/information.png',
                dismissDelay : 30000
            }
        });
        
        var knownLayersMenuFactory = Ext.create('auscope.layer.GAFilterPanelMenuFactory',{map : map, showFilter: false, addResetFormActionForWMS : false, recordPanel : knownLayersPanel})
        knownLayersPanel.menuFactory = knownLayersMenuFactory;

        var customRecordsPanel = Ext.create('auscope.widgets.CustomRecordPanel', {
            title : 'Custom',
            itemId : 'org-auscope-custom-record-panel',
            store : customRecordStore,
            onlineResourcePanelType : 'gaonlineresourcespanel',
            serviceInformationIcon: 'img/information.png',
            mapExtentIcon: 'img/extent3.png',
            enableBrowse : true,//VT: if true browse catalogue option will appear
            tooltip : {
                title : 'Custom Data Layers',
                text : '<p>This tab allows you to create your own layers from remote data services.</p>',
                showDelay : 100,
                dismissDelay : 30000
            },
            map : map,
            layerFactory : defaultLayerFactory
        });

        // header
        var northPanel = {
            layout: 'fit',
            region:'north',
            items:[{
                xtype: 'gaheader',
                map: map,
                registryStore: cswServiceItemStore,
                knownLayerStore: knownLayerStore,
                layerFactory: defaultLayerFactory
            }]
        };

        // footer
        var southPanel = {
            layout: 'fit',
            region:'south',
            items:[{
                xtype: 'gafooter'
            }]
        };
        
        // basic tabs 1, built from existing content
        var tabsPanel = Ext.create('Ext.TabPanel', {
            id : 'auscope-tabs-panel',
            activeTab : 0,
            region : 'center',
            split : true,
            height : '70%',
            width : '100%',
            enableTabScroll : true,
            items:[
                knownLayersPanel,
                customRecordsPanel
            ]
        });
        
        /**
         * Used as a placeholder for the tree and details panel on the left of screen
         */
        var westPanel = {
            id: "region-west",
        	layout: 'border',//VT: vbox doesn't support splitbar unless we custom it.
            region:'west',
            border: false,
            split:true,
            margin:'140 0 0 3',
            width: 370,
            items:[tabsPanel]
        };

        /**
         * This center panel will hold the google maps instance
         */
        var centerPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            id: 'center_region',
            margin: '140 0 0 0'  ,
            html : "<div style='width:100%; height:100%' id='center_region-map'></div>",
            listeners: {
                afterrender: function () {
                    map.renderToContainer(centerPanel,'center_region-map');   //After our centerPanel is displayed, render our map into it
                }
            }
        });
        
        /**
         * Add all the panels to the viewport
         */
        var viewport = Ext.create('Ext.container.Viewport', {
            layout:'border',
            items:[northPanel, westPanel, centerPanel, southPanel]
        });

        if(urlParams.kml){

            Ext.Ajax.request({
                url: 'addKMLUrl.do',
                params:{
                    url : urlParams.kml
                    },
                waitMsg: 'Adding KML Layer...',
                success: function(response) {
                   var responseObj = Ext.JSON.decode(response.responseText);
                   if(responseObj.data.file.indexOf('<kml') ==-1){
                       Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file and URL is properly encoded');
                   }else{
                       var tabpanel =  Ext.getCmp('auscope-tabs-panel');
                       var customPanel = tabpanel.getComponent('org-auscope-custom-record-panel')
                       tabpanel.setActiveTab(customPanel);
                       var cswRecord = customPanel.addKMLtoPanel(responseObj.data.name,responseObj.data.file);
                       var newLayer = defaultLayerFactory.generateLayerFromCSWRecord(cswRecord);
                       cswRecord.set('layer',newLayer);
                       var filterForm = newLayer.get('filterForm');
                       filterForm.setLayer(newLayer);
                       ActiveLayerManager.addlayer(newLayer);
                   }

                },
                failure : function(fp,action){
                    Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file.');
                }
            });

        }

        var activeLayersPanel = Ext.create('portal.widgets.panel.ActiveLayerPanel', {
            store : activeLayerStore,
            onlineResourcePanelType : 'gaonlineresourcespanel',
            serviceInformationIcon: 'img/information.png',
            mapExtentIcon: 'img/extent3.png',
            map : map,
            layerFactory : activeLayerFactory,
            tooltip : {
                anchor : 'top',
                title : 'Featured Layers',
                text : '<p>This is where the portal groups data services with a common theme under a layer. This allows you to interact with multiple data providers using a common interface.</p><br><p>The underlying data services are discovered from a remote registry. If no services can be found for a layer, it will be disabled.</p>',
                showDelay : 100,
                icon : 'img/information.png',
                dismissDelay : 30000
            }
        });
        var activeLayersMenuFactory = Ext.create('auscope.layer.GAFilterPanelMenuFactory',{map : map, showFilter: true, addResetFormActionForWMS : false, recordPanel : activeLayersPanel});
        activeLayersPanel.menuFactory = activeLayersMenuFactory;

        var activeLayersPanel = Ext.create('Ext.panel.Panel', {
            id : 'activeLayersPanel',
            title : 'Active Layers',
             layout: {
                 type: 'vbox',         // Arrange child items vertically
                 align: 'stretch',     // Each takes up full width
                 padding: 1
             },
             renderTo: body,
             items : [
                  activeLayersPanel,     //activeLayerDisplay,
                  {
                     xtype : 'label',
                     id : 'baseMap',
                     html : '<div id="layerSwitcher"></div>',
                     listeners: {
                         afterrender: function (view) {
                             map.renderBaseLayerSwitcher('layerSwitcher');
                         }
                     }
                  }
             ],
             minHeight: 170,
             width: 500,
             collapsible: true,
             animCollapse : true,
             collapseDirection : 'top',
             collapsed : false,
        });

        activeLayersPanel.show();
        activeLayersPanel.setZIndex(1000);
        activeLayersPanel.anchorTo(body, 'tr-tr', [0, 100], true);

        // deserialise and add the restored layers if necessary
        if (decodedString) {
            deserializationHandler = Ext.create('ga.widgets.GADeserializationHandler', {
                knownLayerStore : knownLayerStore,
                cswRecordStore : unmappedCSWRecordStore,
                layerFactory : defaultLayerFactory,
                map : map,
                stateString : decodedString,
                stateVersion : decodedVersion,
                useStoredState: useStoredState
            });
        }
    },

    /** gets the url parameters if any for deserialisation */
    getParameters : function (url){
        var urlParams = {};
        url = url.split('?');
        if (url.length > 1) {
            var regex = '=(.*?)(;|&|$)';
            urlParams['s'] = url[1].split(new RegExp('s' + regex,'gi'))[1];
            if (!urlParams['s']) {
                urlParams['s'] = url[1].split(new RegExp('state' + regex,'gi'))[1];
            }
            urlParams['v'] = url[1].split(new RegExp('v' + regex,'gi'))[1];
        }
        return urlParams;
    }

});
