/**
 * GPT-40.  Display a collapsable window (RH Side) with AuScope Active (Selected) Layers and OpenLayers layers (eg. "Google Hybrid"). 
 * This is responsible for the  AuScope Active (Selected) Layers.
 */

Ext.define('auscope.widgets.panel.ActiveLayersDisplayPanel', {
    extend : 'Ext.grid.Panel',
    alias: 'widget.panel.activelayersdisplaypanel',
//    title: 'Active Layers widget',
//    map : null, //instance of portal.util.gmap.GMapWrapper
//    allowDebugWindow : false, //whether this panel will show the debug window if clicked by the user
    height: 200,
    width: 400,
    store : Ext.data.ArrayStore({
        //autoDestroy : true,
        storeId : 'activelayerstore',
        fields : [
                  { name : 'name' },
                  { name : 'layer'}]
    }),
    layersArray : [],
    
//    constructor : function(config) {
//        layers = [];
//  //xxx         this.activeLayerStore=config.activeLayerStore;
//    },

//    columns: [
//              { text: 'Name', dataIndex: 'name' },
//              { text: 'Email', dataIndex: 'email', flex: 1 },
//              { text: 'Phone', dataIndex: 'phone' }
//          ],
    columns : [{
        //xtype : 'clickcolumn',
        text : 'Layer Name',
        dataIndex : 'name',
        flex : 1,
        listeners : {
            columnclick : Ext.bind(this._nameClickHandler, this)
        }
    }, {
        text : 'Moves',
        dataIndex : 'layer'
    }],
    listeners : {
        addlayer : function(layerArray){
            var layer = layerArray[0];
            console.log("ActiveLayersDisplayPanel - listener - Added layer: ", layer);
            var newlayer = {name:layer.get('name'), layer:"Boogie woogie"};
            this.getStore().loadData([newlayer],true);
            this.layersArray.push(newLayer);
            //newlayer = ["Another name", "Disco King"];//layer];
//            this.getStore().loadData(this.layersArray,false);
            console.log("  Store now: ", this.getStore().getData());
        },
        removelayer : function(layer){
            console.log("ActiveLayersDisplayPanel - listener - Removed layer: ", layer);
        }
    }
});