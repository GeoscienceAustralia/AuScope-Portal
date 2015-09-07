/**
 * AuScope implementation of the core portal FormFactory
 */
Ext.define('auscope.layer.filterer.AuScopeFormFactory', {
    extend : 'portal.layer.filterer.FormFactory',

    /**
     * map : [Required] an instance of portal.map.BaseMap
     */
    constructor : function(config) {
        this.callParent(arguments);
    },

    /**
     * Given an portal.layer.Layer, work out whether there is an appropriate portal.layer.filterer.BaseFilterForm to show
     *
     * Returns a response in the form
     * {
     *    form : Ext.FormPanel - the formpanel to be displayed when this layer is selected (can be EmptyFilterForm)
     *    supportsFiltering : boolean - whether this formpanel supports the usage of the filter button
     *    layer : portal.layer.Layer that was used to generate this object
     * }
     *
     */
    getFilterForm : function(layer) {
        var baseFilterForm = null;
        var baseFilterFormCfg = {
            layer : layer,
            map : this.map
        };

        //A number of known layer's have specific filter forms
        if (layer.get('sourceType') === portal.layer.Layer.KNOWN_LAYER) {
            switch (layer.get('source').get('id')) {
            case 'pressuredb-borehole':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.PressureDBFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'nvcl-borehole':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.NvclFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'mineral-tenements':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.MineralTenementFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'mineral-occ-view':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.MinOccurViewFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-mine':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.MineFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-miningactivity':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.MiningActivityFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'erml-mineraloccurrence':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.MineralOccurrenceFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'yilgarn-geochem':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.YilgarnGeochemistryFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'gsml-borehole':
            case 'mscl-borehole':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.BoreholeFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'portal-reports':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.ReportFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'sf0-borehole-nvcl' :
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.SF0BoreholeFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'remanent-anomalies':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.RemanentAnomaliesFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'remanent-anomalies-EMAG':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.RemanentAnomaliesFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'capdf-hydrogeochem':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.CapdfHydroGeoChemFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true); 
            case 'tima-geosample':
                baseFilterForm = Ext.create('auscope.layer.filterer.forms.TimaGeoSampleFilterForm', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            case 'geologicalMaps_SurfaceGeology__1to1M_GeologicalUnits__ALL_filter':	// GPT-40
            	baseFilterForm = Ext.create('auscope.layer.filterer.forms.GALayersConjunction', baseFilterFormCfg);
                return this._generateResult(baseFilterForm, true);
            }
        }
        
        if (layer.get('sourceType') === portal.layer.Layer.KML_RECORD){
            return this._generateResult(Ext.create('portal.layer.filterer.forms.EmptyFilterForm', baseFilterFormCfg), false);
        }
                
        //otherwise let's see if we can guess an appropriate filter based on layer renderer
        if (layer.get('renderer') instanceof portal.layer.renderer.wms.LayerRenderer || layer.get('renderer') instanceof portal.layer.renderer.wfs.FeatureWithMapRenderer) {
            baseFilterForm = Ext.create('portal.layer.filterer.forms.WMSLayerFilterForm', baseFilterFormCfg);
            //VT: Filtering is support but for WMS, we want the image to be displayed immediately after it has been added and
            //the opacity can be adjusted from there on
            return this._generateResult(baseFilterForm, false);
        }
        
        

        // TODO: Can I get rid of containsCSWService and make this check the renderer like the example above?
        // don't forget this method is used in AuScopeRendererFactory.js, too.
        // Once you work out what the renderer will be called you CAN change this to make it like the one above
        // and then remove the containsCSWService method from Layer.js
        if (layer.containsCSWService()) {
            baseFilterForm = Ext.create('auscope.layer.filterer.forms.CSWServiceFilterForm', baseFilterFormCfg);
            return this._generateResult(baseFilterForm, true);
        }


        //And otherwise we just show the empty filter form
        return this._generateResult(Ext.create('portal.layer.filterer.forms.EmptyFilterForm', baseFilterFormCfg), false);
    }
});