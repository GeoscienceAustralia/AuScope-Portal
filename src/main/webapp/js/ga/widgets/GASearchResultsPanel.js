/**
 *
 * This is the paging panel used for GA Search Results
 *
 */
Ext.define('ga.widgets.GASearchResultsPanel', {
    extend : 'Ext.grid.Panel',
    alias: 'widget.gasearchresultspanel',

    cswRecordStore : null,
    pageSize: 20,
    displayTemplate: null,
    map : null,
    layerFactory : null,
    layerStore : null,

    constructor : function(cfg) {
        var me = this;

        me.cswRecordStore = cfg.store;
       
        me.map = cfg.map;
        me.layerStore = cfg.layerStore;
        
        me.layerFactory = cfg.layerFactory;
        
        Ext.apply(cfg, {
            hideHeaders : true,
            height: 200,
            layout : 'fit',

            columns: [{
                //Title column
                dataIndex: 'onlineResource',
                sortable: false,
                cellWrap : true,
                flex: 1,
                xtype: 'componentcolumn', 
                autoWidthComponents : false,
                renderer: Ext.bind(me._cellRenderer, me)
            }],
            store : this.cswRecordStore,
            viewConfig : {
                forceFit : true,
                enableRowBody:true
            },

            loadMask : {msg : 'Performing CSW Filter...'},
            multiSelect: true,
            dockedItems: [{
                xtype: 'pagingtoolbar',
                pageSize: me.pageSize,
                store: me.cswRecordStore,
                dock: 'bottom',
                displayInfo: true
            }],
            listeners : {
                afterrender : function(grid,eOpts) {
                    grid.cswRecordStore.load();
                    
                },
                
                // when the view is fully loaded we need to check for availability of some features and update the DOM                    
                viewready: function(view) {

                    me.cswRecordStore.each(function(record,id){
                        var hasWMSResource = false;
                        var onlineResources = record.data.onlineResources;
                        for (var i = 0; i < onlineResources.length; i++) {
                            var type = onlineResources[i].get('type');
                            if (type == portal.csw.OnlineResource.WMS) {
                                hasWMSResource = true;
                                break;
                            }                                          
                        };
                                   
                        var addWMSLinkId = '_' + record.get('name').replace(/\W/g, '') + '_addWMSLink'; 
                        var element = Ext.get(addWMSLinkId);
                        
                        if (!hasWMSResource) {
                            element.addCls('pretendLinkDisabled');
                            element.removeCls('pretendLinkEnabled');
                        } else {
                            // pretend it to be a link.
                            element.addCls('pretendLinkEnabled');
                            element.removeCls('pretendLinkDisabled');
                            element.addListener('click', function(){
                                me.displayWMSLayers(record);
                            });
                        };
                    });
                   
                }
            }

        });

      this.callParent(arguments);
    },

    /**
     * On single click, show a highlight of all BBoxes
     */
    showSpatialBounds : function(record) {
        var me = this;
        var spatialBoundsArray;
        if (record.internalId == 'portal-InSar-reports') {
            spatialBoundsArray = this.getWholeGlobeBounds();
        } else {
            spatialBoundsArray = this.getSpatialBoundsForRecord(record);
        }
        var nonPointBounds = [];

        //No point showing a highlight for bboxes that are points
        for (var i = 0; i < spatialBoundsArray.length; i++) {
            var bbox = spatialBoundsArray[i];
            if (bbox.southBoundLatitude !== bbox.northBoundLatitude ||
                bbox.eastBoundLongitude !== bbox.westBoundLongitude) {

                //VT: Google map uses EPSG:3857 and its maximum latitude is only 85 degrees
                // anything more will stretch the transformation
                if(bbox.northBoundLatitude>85){
                    bbox.northBoundLatitude=85;
                }
                if(bbox.southBoundLatitude<-85){
                    bbox.southBoundLatitude=-85;
                }
                nonPointBounds.push(bbox);
            }
        }

        me.map.highlightBounds(nonPointBounds);
    },    
    
    /**
     * Helper function.  Useful to define here for subclasses.
     *
     * Return the max bbox for insar layer as it is a dummy CSW.
     */
    getWholeGlobeBounds : function() {
        var bbox = new Array();
        bbox[0] = Ext.create('portal.util.BBox', {
            northBoundLatitude : 85,
            southBoundLatitude : -85,
            eastBoundLongitude : 180,
            westBoundLongitude : -180
        });
        return bbox;
    },
    
    getSpatialBoundsForRecord : function(record) {
        return record.data.geographicElements;
    },
        
    
    // renderer for the details of the resource (name, description, links)
    _cellRenderer : function(value, metaData, record, row, col, store) {        
        var me = this;       
        
        if (record) {
            var name;

            var name = record.get('name');            
            
            //Ensure there is a title (even it is just '<Untitled>'
            if (!name || name.length === 0) {
                name = '&lt;Untitled&gt;';
            }
            
            var description = record.get('description');
            //Truncate description
            var maxLength = 300;
            if (description.length > maxLength) {
                description = description.substring(0, maxLength) + '...';
            }        
            
            var recordInfoUrl = record.get('recordInfoUrl');
            
            var addWMSLinkId = '_' + name.replace(/\W/g, '') + '_addWMSLink'; 
            var elements = {
                xtype: 'panel',
                cls : 'gasearchresult',              
                items: [{
                    xtype: 'box',
                    cls: 'name',
                    autoEl: {
                      tag: 'div',
                      html: name
                    }  
                },{
                    xtype : 'displayfield',
                    value : Ext.util.Format.format('<span class="description">{0}</span>', description) 
                },{
                    xtype : 'panel',
                    cls : 'links', 
                    items : [{       
                        xtype: 'box',
                        autoEl: {
                          tag: 'a',
                          target : '_blank',
                          href: recordInfoUrl,
                          cn: 'Full metadata'
                        }                    
                    },
                    {
                        xtype: 'box',
                        listeners: {
                            render: function(component) {
                                component.getEl().on('click', function(e) {
                                    e.stopEvent();
                                    me.showSpatialBounds(record);
                                });    
                            }
                        },
                        autoEl: {
                          tag: 'a',
                          target : '_blank',
                          href: '#',
                          cn: 'Show extent'
                        }                      
                    },
                    {
                        xtype: 'box',
                        listeners: {
                            render: function(component) {            
                                component.getEl().on('click', function(e) {
                                    e.stopEvent();   
                                    me.displayFileDownloadWindow(record);
                                });    
                            }
                        },
                        autoEl: {
                          tag: 'a',
                          target : '_blank',
                          href: '#',
                          cn: 'Download data'
                        }
                    },
                    {
                        xtype: 'box', 
                        id: addWMSLinkId,
                        autoEl: {                            
                          tag: 'span',                          
                          html: 'Add WMS to map'
                        }
                    }]
                }]
                
            }
            return elements;    
        }       
    },
    
    // TODO HEYA
    displayFileDownloadWindow : function(record){
        Ext.Msg.show({
            title:'Not implemented',
            msg: 'The selected function is not currently implemented.',
            buttons: Ext.Msg.OK
        });
    },    

    // displays a popup window allowing the user to select layers to add to the map
    displayWMSLayers : function(record){   
        var me = this;     
        Ext.create('ga.widgets.GALayerSelectionWindow', {
            title : 'Add WMS Layers',
            map: me.map,
            layerFactory: me.layerFactory,
            layerStore: me.layerStore,
            cswRecord: record
        }).show();        
    }   

});