/**
 * This is the GA menu bar.
 * It contains navigation and function links at te left and a lay-long indicator at the right.
 */
Ext.define('ga.widgets.GAMenuBar', {
    extend : 'Ext.panel.Panel',
    alias: 'widget.gamenubar',

    map: null,
    layerStore: null,
    
    statics : {
        instructionManager : Ext.create('portal.util.help.InstructionManager', {}),
    },

    constructor : function(config){   
        
        var me = this;
        me.map = config.map;
        me.layerStore = config.layerStore;  
        
        //Create our Print Map handler
        var printMapHandler = function() {                    
            // get the html of the map div and write it to a new window then call the browser print function            
            var divToPrint = Ext.get('center_region');
            
            // hide the controls
            Ext.get('center_region-map').select('.olButton').hide();
            Ext.get('center_region-map').select('.olAlphaImg').hide();
            
            var html = divToPrint.dom.innerHTML;
            
            // show the controls
            Ext.get('center_region-map').select('.olButton').show();
            Ext.get('center_region-map').select('.olAlphaImg').show();
            
            var printWindow = window.open('', '', 
                    'width=' + divToPrint.dom.style.width 
                    + ',height=' + divToPrint.dom.style.height 
                    + ',top=0,left=0,toolbars=no,scrollbars=yes,status=no,resizable=yes');
            printWindow.document.writeln(html);
            
            printWindow.document.close();
            printWindow.focus();
            printWindow.print();
            printWindow.close();            
        };   
    
    
        //Create our Reset Map handler
        // revert to the default zoom level, map extent and remove all our layers
        var resetMapHandler = function() {
            me.map.map.zoomTo(4); 
            var center = new OpenLayers.LonLat(133.3, -26).transform('EPSG:4326', 'EPSG:3857');
            me.map.map.setCenter(center);
            
            // remove all of the layers we have added
            var items = me.map.layerStore.data.items;    
            for (i = items.length-1; i >=0; --i) {
                var layer = items[i];        
                AppEvents.broadcast('removelayer', {layer:layer});
            }
            me.layerStore = Ext.create('portal.layer.LayerStore', {});
        };              
        
        //Create our permalink generation handler
        var permalinkHandler = function() {
            var mss = Ext.create('portal.util.permalink.MapStateSerializer');
    
            mss.addMapState(me.map);
            mss.addLayers(me.layerStore);
    
            mss.serialize(function(state, version) {
                var popup = Ext.create('portal.widgets.window.PermanentLinkWindow', {
                    state : state,
                    version : version,
                    mapStateSerializer: mss
                });
    
                popup.show();
            });
        };
        
        var helpHandler = function() {
            ga.widgets.GAMenuBar.instructionManager.showInstructions([Ext.create('portal.util.help.Instruction', {
                highlightEl : 'auscope-tabs-panel',
                title : 'Find data/layers',
                description : 'In this panel a list of all available datasets in the form of layers will be presented to you.  Select the layer you would like to visualise.<br><br>Selecting a layer will expand any advanced filter options. If you do not wish to filter your datasets, you can visualise the data by clicking "Add to Map".<br/><br/>Further information about the data behind each layer can be displayed by clicking the icons alongside the layer name.'
            }),Ext.create('portal.util.help.Instruction', {
                highlightEl : 'hh-searchfield-Featured',
                title : 'Search Layer',
                description : 'Allow you to filter through the layers via the layer\'s name. Enter a key and click the magnifying glass to filter'
            }),Ext.create('portal.util.help.Instruction', {
                highlightEl : 'hh-filterDisplayedLayer-Featured',
                title : 'Filter Display Layer Option',
                description : 'Provide options to filter the list of displayed layers.'
            }),Ext.create('portal.util.help.Instruction', {
                highlightEl : 'latlng',
                anchor : 'left',
                title : 'Mouse Coordinate',
                description : 'Display the coordinate of the mouse on the map'
            }),Ext.create('portal.util.help.Instruction', {
                highlightEl : 'permanent-link',
                anchor : 'left',
                title : 'Permanent Link',
                description : 'Create a link that captures the current state of the user session.'
            }),Ext.create('portal.util.help.Instruction', {
                highlightEl : 'hh-userGuide',
                anchor : 'left',
                title : 'User guide',
                description : 'For more information, refer to the user guide.'
            })]);
        };
            
  
        Ext.apply(config, {
            height: '40px',
            items: [{
                xtype: 'box',
                id: 'menu-bar',                    
                autoEl: {
                    tag: 'div',
                    html: '<ul>\
                               <li><a href="http://www.geoscience.gov.au"><img src="img/home.png" width="16" height="16"/></a></li>\
                               <li><a id="print-map-link" href="javascript:void(0)">PRINT MAP</a></li>\
                               <li><a id="reset-map-link" href="javascript:void(0)">RESET MAP</a></li>\
                               <li><a id="permanent-link" href="javascript:void(0)"> PERMANENT LINK </a> </li>\
                               <li><a id="help-link" href="javascript:void(0)"> HELP </a> </li>\
                               <span id="latlng"></span>\
                           </ul>'
               }                    
            }],

                
            listeners: {
                render: function (view) {
                    Ext.get('print-map-link').on('click', printMapHandler);
                    Ext.get('reset-map-link').on('click', resetMapHandler);  
                    Ext.get('permanent-link').on('click', permalinkHandler); 
                    Ext.get('help-link').on('click', helpHandler); 
                }
            }
            
        });
    
        this.callParent(arguments);
    
    }

});