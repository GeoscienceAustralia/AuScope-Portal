/**
 * GPT-40.  Display a collapsable window (RH Side) with AuScope Active (Selected) Layers and OpenLayers layers (eg. "Google Hybrid"). 
 * This is responsible for the  AuScope Active (Selected) Layers.
 */

Ext.define('auscope.widgets.panel.ActiveLayersDisplayPanel', {
    extend : 'Ext.grid.Panel',
    alias: 'widget.panel.activelayersdisplaypanel',
    title: 'Active Layers widget',
    map : null, //instance of portal.util.gmap.GMapWrapper
    allowDebugWindow : false, //whether this panel will show the debug window if clicked by the user

    constructor : function(cfg) {
        var me = this;
        this.callParent(cfg);

        this.map = cfg.map;
        this.allowDebugWindow = cfg.allowDebugWindow ? true : false;
        console.log('auscope.widgets.panel.ActiveLayersDisplayPanel constructor');
    },
    columns : [{
        //legend column
        xtype : 'clickcolumn',
        dataIndex : 'renderer',
        renderer : this._legendIconRenderer,
        width : 32,
        listeners : {
            columnclick : Ext.bind(this._legendClickHandler, this)
        }
    },{
        //Loading icon column
        xtype : 'clickcolumn',
        dataIndex : 'loading',
        renderer : this._loadingRenderer,
        hasTip : true,
        tipRenderer : Ext.bind(this._loadingTipRenderer, this),
        width: 32,
        listeners : {
            columnclick : Ext.bind(this._serviceInformationClickHandler, this)
        }
    },{
        //Layer name column
        xtype : 'clickcolumn',
        text : 'Layer Name',
        dataIndex : 'name',
        flex : 1,
        listeners : {
            columnclick : Ext.bind(this._nameClickHandler, this)
        }
    },{
        //Visibility column
        xtype : 'renderablecheckcolumn',
        text : 'Visible',
        dataIndex : 'renderer',
        getCustomValueBool : function(header, renderer, layer) {
            return renderer.getVisible();
        },
        setCustomValueBool : function(header, renderer, checked, layer) {
            //update our bbox silently before updating visibility
            if (checked) {
                var filterer = layer.get('filterer');
                filterer.setSpatialParam(me.map.getVisibleMapBounds(), true);
            }

            return renderer.setVisible(checked);
        },
        width : 40
    },{
        //Download column
        xtype : 'clickcolumn',
        dataIndex : 'renderer',
        width : 32,
        renderer : this._downloadRenderer,
        listeners : {                                                            
            columnclick : function( column, record, recordIndex, cellIndex, e){
                var menu = Ext.create('Ext.menu.Menu', {
                    items: [
                            me.removeAction,
                            me.downloadLayerAction
                            ]                
                });
                menu.showAt(e.getXY());
            }
        }
    }],
    listeners : {
        addlayer : function(layer){
            console.log("ActiveLayersDisplayPanel - listener - Added layer: ", layer);
        },
        removelayer : function(layer){
            console.log("ActiveLayersDisplayPanel - listener - Removed layer: ", layer);
        }
    }
});