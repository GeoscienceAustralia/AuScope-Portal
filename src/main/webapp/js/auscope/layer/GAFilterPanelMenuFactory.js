/**
 * Geoscience Australia implementation of the core portal FilterPanelMenuFactory
 */
Ext.define('auscope.layer.GAFilterPanelMenuFactory', {
    extend : 'portal.widgets.FilterPanelMenuFactory',
 
    map : null,
    panel: null,
    recordPanel: null,
    
    // whether to add a reset form button on WMS forms. 
    // default to true because that is currently the AuScope portal default
    addResetFormActionForWMS : true,
    
    constructor : function(config) {
        this.map = config.map;
        this.recordPanel = config.recordPanel;
        this.callParent(arguments);
    },

    /**
     * Given an portal.layer.Layer, check if there are any additional action to display
     * 
     * returns an array of menu action items.
     *
     */
    appendAdditionalActions : function(menuItems,layer,group) {
        
        //VT:Should have a download action except for Insar data.
        if((layer.get('cswRecords').length > 0
            && layer.get('cswRecords')[0].get('noCache')==false)
            && layer.id != 'portal-reports'){
                
            var allOnlineResources = layer.getAllOnlineResources();
        
            var wmsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WMS);
            var wfsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WFS);
            var wcsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WCS);

            if (wfsResources.length > 0 || wcsResources.length > 0) {
                menuItems.push(this._getDownloadAction(layer));
            }
        }
    },
       
    _getDownloadAction : function(layer){
        var me = this;
        var downloadLayerAction = new Ext.Action({
            text : 'Download Layer',
            iconCls : 'download',
            handler : function(){              
                var downloader = layer.get('downloader');
                var renderer = layer.get('renderer');
                if (downloader) {// && renderer.getHasData() -> VT: It is too confusing when the download will be active. We will treat it as always active to 
                                 // make it easier for the user.
                    //We need a copy of the current filter object (in case the user
                    //has filled out filter options but NOT hit apply filter) and
                    //the original filter objects
                    var renderedFilterer = layer.get('filterer').clone();
                    var currentFilterer = Ext.create('portal.layer.filterer.Filterer', {});
                    var currentFilterForm = layer.get('filterForm');

                    currentFilterer.setSpatialParam(me.map.getVisibleMapBounds(), true);
                    currentFilterForm.writeToFilterer(currentFilterer);

                    //Finally pass off the download handling to the appropriate downloader (if it exists)
                    var onlineResources = layer.getAllOnlineResources();
                    downloader.downloadData(layer, onlineResources, renderedFilterer, currentFilterer);

                }
            }
        });
        return downloadLayerAction;
    }

});