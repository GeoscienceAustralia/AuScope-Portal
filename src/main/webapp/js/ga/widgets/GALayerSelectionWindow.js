/**
 * A Ext.Window that shows a list of available WMS resources along with a button 
 * that allows the user to add selected layers to the map.
 */
Ext.define('ga.widgets.GALayerSelectionWindow', {
    extend : 'Ext.window.Window',
    
    map : null,
    layerFactory : null,
    
    // the parent csw record
    cswRecord : null,

    // available layers
    wmsRecordStore : null,
    
    getCapabilitiesUrl : null,
    
    /**
     * Constructor for this class, accepts all configuration options that can be
     * specified for a Ext.Window as well as the following values
     * {
     *  cswRecord : A single CSWRecord object
     * }
     */
    constructor : function(cfg) {

        var me = this;
        
        me.map = cfg.map;
        me.layerFactory = cfg.layerFactory;
        me.cswRecord = cfg.cswRecord;
        
        var getCapabilitiesUrl = null;
        var onlineResources = me.cswRecord.data.onlineResources;
        for (var i = 0; i < onlineResources.length; i++) {
            var onlineResource = onlineResources[i]; 
            var type = onlineResource.get('type');
            if (type == portal.csw.OnlineResource.WMS) {
                me.getCapabilitiesUrl = onlineResource.get('url');
                break;
            }
        }
        
        //This record store holds layers available from a specific WMS service address
        me.wmsRecordStore = Ext.create('Ext.data.Store', {
            model : 'portal.csw.CSWRecord',
            proxy : {
                type : 'ajax',
                url : 'getWMSLayersForCSWRecord.do',
                extraParams : { 
                    service_URL : me.getCapabilitiesUrl,
                    recordName : me.cswRecord.get('name'),
                    isService : me.cswRecord.get('service')
                }, 
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            },
            
            autoLoad : false,
            data : [],
            listeners : {
                load  :  function(store, records, successful, eopts){
                    if(!successful){
                        Ext.Msg.show({
                            title:'Error!',
                            msg: 'Your WMS service has to support EPSG:3857 to be supported by Google map. You are seeing this error because either the URL is not valid or it does not conform to EPSG:3857 WMS layers standard',
                            buttons: Ext.Msg.OK
                        });
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
        
        var controlButtons = [{
            xtype: 'button',
            text:'Add selected layers to map',
            scope: this,
            handler:function(button){      
                
                var galayerselectionpanel = this.down('gridpanel');
                if (galayerselectionpanel) {
                    var selectedResources = galayerselectionpanel.getSelection();
                    for (var i = 0; i < selectedResources.length; i++) {   
                        var layer = me.layerFactory.generateLayerFromCSWRecord(selectedResources[i]);      
                        
                        // set the source type to an arbitrary value to avoid the grouping logic
                        layer.set('sourceType', 'search');
                        
                        me.map.layerStore.suspendEvents(true);
                        me.map.layerStore.add(layer); //this adds the layer to our store
                        me.map.layerStore.resumeEvents();
                        
                        var filterer = layer.get('filterer');      

                        try {
                            layer.get('filterForm').writeToFilterer(filterer);
                        } catch(error) {
                            console.log(error);
                        }
                        AppEvents.broadcast('addlayer', {layer: layer});
                        
                        //VT: Tracking
                        portal.util.PiwikAnalytic.trackevent('Add:' + layer.get('sourceType'), 'Layer:' + layer.get('name'),'Filter:' + filterer.getParameters());
                    };
                }                       
                this.destroy();               
            }
        }];
        
        //Build our configuration object
        Ext.apply(cfg, {
            layout : 'fit',
            autoScroll : true,
            autoHeight: true,
            cellWrap : true,
            items: [{
                // inner panel
                layout: 'fit',
                height: '100%',
                items : [{
                    xtype : 'fieldset',
                    items : [{
                        xtype : 'displayfield',
                        fieldLabel : 'Title',
                        anchor : '100%',
                        value : cfg.cswRecord.get('name')
                    },{
                        xtype : 'displayfield',
                        fieldLabel : 'URL endpoint',
                        anchor : '100%',
                        value : cfg.cswRecord.get('recordInfoUrl')
                    }, {
                        xtype: 'galayerselectionpanel',
                        store: me.wmsRecordStore,
                        layout: 'fit'
                    }]
                }]
            }],
            buttons: controlButtons             
        }, {
            width : 800,
            //height : 450,
            title : me.cswRecord.get('name')
        });

        //Call parent constructor
        this.superclass.constructor.call(me, cfg);
    } 
   
});