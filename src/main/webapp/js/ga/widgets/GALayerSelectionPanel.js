/**
 * A Panel specialisation for allowing the user to select a layer to add to the map.
 */
Ext.define('ga.widgets.GALayerSelectionPanel', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.galayerselectionpanel',    
    selType: 'checkboxmodel',    
    xtype: 'checkbox-selection',
   
    
    /**
     * Accepts all Ext.grid.Panel options as well as
     * {
     *  layers : single instance of array of portal.csw.CSWRecord objects
     *  allow
     * }
     */
    constructor : function(cfg) {
        
        var me = this;       
            
        var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
            groupHeaderTpl: '{name} ({[values.rows.length]} {[values.rows.length > 1 ? "Items" : "Item"]})'
        });

        var sortable = false;
        var hideHeaders = false;

        var columns = [{
            dataIndex: 'name',
            text: 'Layer Title',            
            menuDisabled: true,
            sortable: false,
            hideheaders: false,
            cellWrap : true,
            flex: 1
        }];
        
        //Build our configuration object
        Ext.apply(cfg, {
            store: cfg.store,
            features : [groupingFeature],
            plugins : [{
                ptype : 'selectablegrid'
            }],
            hideHeaders : hideHeaders,
            columns: columns,
            viewConfig: {
                enableTextSelection: true
            },
            listeners : {
                afterrender : function(grid,eOpts) {
                    grid.store.load();
                    
                }
            }
        });

        this.callParent(arguments);
    }
});