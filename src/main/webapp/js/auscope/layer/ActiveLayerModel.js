/**
 * An Active Layer is a layer that is displayed.  Extra information for its display needs to be maintained.
 * Done as part of GPT-40
 * 
 */

Ext.define('auscope.layer.ActiveLayerModel', {
    extend: 'Ext.data.Model',

    visible : true,

    fields: [
        { name: 'layer', type: 'auto' },    // A portal.layer.Layer
        { name: 'name', type: 'string' },    // Layer name
        { name: 'info', type: 'string' },    // Link to the Service Information
        { name: 'visible', type: 'boolean' },    // If to show
        { name: 'legend', type: 'string' },    // Link to legend
        { name: 'remove', type: 'string' },    // To remove layer
    ],

    setLayerVisibility : function(visibility){
        this.get('renderer').setVisibility(visibility);
        this.visible = visibility;
    }
});



