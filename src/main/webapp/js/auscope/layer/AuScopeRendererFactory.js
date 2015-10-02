/**
 * A class for creating instances of portal.layer.renderer.Renderer
 */
Ext.define('auscope.layer.AuScopeRendererFactory', {

    extend : 'portal.layer.renderer.RendererFactory',

    /**
     * Creates a new instance of renderer based on the specified values
     */
    _generateRenderer : function(wfsResources, wmsResources, irisResources, knownLayer) {
        var proxyUrl;
        var proxyCountUrl;
        var iconUrl;
        var iconSize;
        var iconAnchor;
        var polygonColor;

        if (knownLayer != undefined) {
            proxyUrl = knownLayer.get('proxyUrl');
            proxyCountUrl = knownLayer.get('proxyCountUrl');
            iconUrl = knownLayer.get('iconUrl');
            iconSize = knownLayer.get('iconSize');
            iconAnchor = knownLayer.get('iconAnchor');
            polygonColor = knownLayer.get('polygonColor');
        }

        var icon = Ext.create('portal.map.Icon', {
            url : iconUrl,
            width : iconSize ? iconSize.width : 32,
            height : iconSize ? iconSize.height : 32,
            anchorOffsetX : iconAnchor ? iconAnchor.x : 16,
            anchorOffsetY : iconAnchor ? iconAnchor.y : 32
        });
        
        if(knownLayer != undefined && knownLayer.get('id')=='capdf-hydrogeochem'){
            return Ext.create('portal.layer.renderer.capdf.CapdfRenderer', {
                map : this.map,
                icon : icon,
                proxyUrl : proxyUrl ? proxyUrl : 'getAllFeatures.do',
                proxyCountUrl : proxyCountUrl
            });
        }

        // GPT-40 - Layers within Layers - OR/Disjunction case
        if (knownLayer != undefined
                && knownLayer.get('id').match(/^layers-disjunction/) != null
                && (knownLayer.get('id').match(/^layers-disjunction/).length > 0)
                && knownLayer.get('layerMode') === 'OR') {
            return Ext.create('portal.layer.renderer.wms.DisjunctionLayerRenderer', {map : this.map});
        }
        
        if(wmsResources.length > 0 && wfsResources.length > 0){
            return Ext.create('portal.layer.renderer.wfs.FeatureWithMapRenderer', {
                map : this.map,
                icon : icon,
                proxyUrl : proxyUrl ? proxyUrl : 'getAllFeatures.do',
                proxyCountUrl : proxyCountUrl
            });
        } else if (wmsResources.length > 0) {
            return Ext.create('portal.layer.renderer.wms.LayerRenderer', {map : this.map});
        } else if (wfsResources.length > 0) {
            return Ext.create('portal.layer.renderer.wfs.FeatureRenderer', {
                map : this.map,
                icon : icon,
                proxyUrl : proxyUrl ? proxyUrl : 'getAllFeatures.do',
                proxyCountUrl : proxyCountUrl
            });
        } else if (irisResources.length > 0) {
            return Ext.create('portal.layer.renderer.iris.IRISRenderer', {
                map : this.map,
                icon : icon,
                proxyUrl : proxyUrl,
                proxyCountUrl : proxyCountUrl
            });
        } else if (knownLayer && knownLayer.containsCSWService()) {
            // TODO: ADAM: I'm not sure what parameters I need to send in...
            return Ext.create('portal.layer.renderer.cswservice.UncachedCSWServiceRenderer', {
                map : this.map,
                icon : icon,
                polygonColor: polygonColor
            });
        }
        else {
            return Ext.create('portal.layer.renderer.csw.CSWRenderer', {
                map : this.map,
                icon : icon,
                polygonColor: polygonColor
            });
        }
    },

    /**
     * See parent class for defn
     */
    buildFromKnownLayer : function(knownLayer) {
        var allOnlineResources = knownLayer.getAllOnlineResources();

        var wmsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WMS);
        var wfsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WFS);
        var irisResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.IRIS);

        return this._generateRenderer(wfsResources, wmsResources, irisResources, knownLayer);
    },

    /**
     * See parent class for defn
     */
    buildFromCswRecord : function(cswRecord) {
        var allOnlineResources = cswRecord.get('onlineResources');

        var wmsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WMS);
        var wfsResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.WFS);
        var irisResources = portal.csw.OnlineResource.getFilteredFromArray(allOnlineResources, portal.csw.OnlineResource.IRIS);

        return this._generateRenderer(wfsResources, wmsResources, irisResources, undefined);
    },
    
    /**
     * See parent class for defn
     */
    buildFromKMLRecord : function(cswRecord){
        return Ext.create('portal.layer.renderer.csw.KMLRenderer', {
            map : this.map   
        });
    }
});