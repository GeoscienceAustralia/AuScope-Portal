/**
 * An Ext.data.Store specialisation for storing
 * portal.layer.Layer objects.
 */
Ext.define('auscope.layer.ActiveLayerStore', {
    extend: 'Ext.data.Store',

    /**
     * Creates an empty store - no configuration options
     */
    constructor : function() {
        this.callParent([{
            model : 'portal.layer.Layer', //auscope.layer.ActiveLayerModel',
            data : []
        }]);
        AppEvents.addListener(this);
    }, 
    listeners : {
        addactivelayer : function(args) {
            console.log("ActiveLayerStore - Add records to layerstore: ", args.layer[0]);
//            this.newModel(args.layer);
            this.add(args.layer[0]);
        },
        removeactivelayer : function(args) {
            console.log("ActiveLayerStore - Remove records from layerstore: ", args.layer);
            this.remove(args.layer[0]);
        }
    },
    newModel : function(layer) {
        var theLayer = layer[0];
        var model = {id : theLayer.id, name: theLayer.get('name'), layer: theLayer, info:'info', visible:true, legend:'legend', remove:'X'};
        this.add(model);
        console.log("Added new model: ", model);
        console.log("  # records in store is: ", this.count());
    }
});