/**
 * This is the window used to display the results from the CSW search.
 * Based on portal-core selectionWindow.js with Geoscience Australia customisation
 */
GASearchResultsWindow = Ext.extend(Ext.Window, {
    
    store : null,
    pageSize: 20,
    
    constructor : function(cfg) {      
        var me = this;        
        
        Ext.apply(cfg, {
            title: cfg.title,
            height: 500,
            width: 650,
            layout: 'fit',
            items: [{
                xtype:'tabpanel',
                layout: 'fit',
                map: cfg.map,
                layerFactory: cfg.layerFactory,
                layerStore: cfg.layerStore,                
                items : cfg.resultpanels // GASearchResultsPanel
            }],


            buttonAlign : 'right',
            buttons : [{
                xtype : 'button',
                text : 'Back to Search',
                iconCls : 'magglass',
                scope : me,
                handler : function(button, e) {                    
                    this.destroy();
                    var gaAdvancedSearchWindow = Ext.getCmp('gaAdvancedSearchWindow');
                    if (gaAdvancedSearchWindow) {
                        gaAdvancedSearchWindow.show();
                    }
                 }
            }]
        });

        //Call parent constructor
        GASearchResultsWindow.superclass.constructor.call(me, cfg);

    },
    
    // overridden close method to obtain a reference to the search window and close it as well
    close: function() {      
        var gaAdvancedSearchWindow = Ext.getCmp('gaAdvancedSearchWindow');
        if (gaAdvancedSearchWindow) {
            gaAdvancedSearchWindow.close();   
        }
        this.destroy();
    }

});

