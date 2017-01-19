/**
 * This is the GA portal header.
 * It contains the logos of the state and federal agencies that contributed to the portal. 
 */
Ext.define('ga.widgets.GAHeader', {
    extend : 'Ext.panel.Panel',
    alias: 'widget.gaheader',

    map: null,
    registryStore: null,
    layerFactory: null,
    knownLayerStore: null,

    constructor : function(config){   
        
        var me = this;
        me.map = config.map;
        me.registryStore = config.registryStore;    
        me.layerFactory = config.layerFactory;
        me.knownLayerStore = config.knownLayerStore;
        
        //Create our advanced search control handler
        var advancedSearchLinkHandler = function() {
            
            var gaAdvancedSearchWindow = Ext.getCmp('gaAdvancedSearchWindow');
            if (!gaAdvancedSearchWindow) {
                gaAdvancedSearchWindow = new ga.widgets.GAAdvancedSearchWindow({
                    name : 'CSW Filter',
                    id : 'gaAdvancedSearchWindow',
                    map : me.map,
                    layerFactory : me.layerFactory,
                    listeners : {
                        filterselectcomplete : function(filteredResultPanels) {
                            var gaSearchResultsWindow = new GASearchResultsWindow({
                                title : 'Advanced Search Results',
                                id: 'gaSearchResultsWindow',
                                map : me.map,
                                layerFactory : me.layerFactory,
                                
                                resultpanels : filteredResultPanels,
                                listeners : {
                                    selectioncomplete : function(csws){
                                        var tabpanel =  Ext.getCmp('auscope-tabs-panel');
                                        var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                                        tabpanel.setActiveTab(customPanel);
                                        if(!(csws instanceof Array)){
                                            csws = [csws];
                                        }
                                        for(var i=0; i < csws.length; i++){
                                            csws[i].set('customlayer',true);
                                            customPanel.getStore().insert(0,csws[i]);
                                        }

                                    }
                                }
                            });
                            gaSearchResultsWindow.show();
                        }
                    }
                });
            }
            
            var basicSearchInput = Ext.get('basic-search-input');
            if (basicSearchInput) {
                //basicSearchInput.dom.disabled = 'true';
                basicSearchInput.dom.value = '';
            }           
            
            var gaSearchResultsWindow = Ext.getCmp('gaSearchResultsWindow');
            if (gaSearchResultsWindow) {
                gaSearchResultsWindow.destroy();
            }           
            
            gaAdvancedSearchWindow.show();
        };
        
    
        //Create our 'Basic Search' handler
        var basicSearchButtonHandler = function() {
    
            var basicSearchInput = Ext.get('basic-search-input');
            
            // hmmm... validate empty input or just ignore it?
            if (!basicSearchInput) {
                return false;
            }  
            
            if (basicSearchInput.dom.value === '') {
                Ext.Msg.alert('Search Term Required', 'Please enter a search term in the provided input field.');
                return false;
            }
                        
            var filteredResultPanels=[];
    
            for(arrayIndex in me.registryStore.data.items){
                filteredResultPanels.push(getTabPanels(me.registryStore.data.items[arrayIndex].data, basicSearchInput.dom.value));                
            }
            
            var gaAdvancedSearchWindow = Ext.getCmp('gaAdvancedSearchWindow');
            if (gaAdvancedSearchWindow) {
                gaAdvancedSearchWindow.destroy();
            }
            var gaSearchResultsWindow = Ext.getCmp('gaSearchResultsWindow');
            if (gaSearchResultsWindow) {
                gaSearchResultsWindow.destroy();
            }
            gaSearchResultsWindow = new GASearchResultsWindow({
                title : 'Search Results',
                id: 'gaSearchResultsWindow',
                map : me.map,
                layerFactory : me.layerFactory,
                resultpanels : filteredResultPanels,
                showControlButtons : false,
                listeners : {
                    selectioncomplete : function(csws){  
                        var tabpanel =  Ext.getCmp('auscope-tabs-panel');
                        var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                        tabpanel.setActiveTab(customPanel);
                        if(!(csws instanceof Array)){
                            csws = [csws];
                        }
                        for(var i=0; i < csws.length; i++){
                            csws[i].set('customlayer',true);
                            customPanel.getStore().insert(0,csws[i]);
                        }
                        
                    }
                }
            });
            portal.util.GoogleAnalytic.trackevent('Basic Search:', 'Service ids:All', 'Search parameters:' + basicSearchInput.dom.value);
            gaSearchResultsWindow.show();                  
        };
    
        var userGuideLinkHandler = function () {
            portal.util.GoogleAnalytic.trackevent('UserGuideClick', 'User Guide', 'http://www.geoscience.gov.au/Geoscience_Portal_User_Guide.pdf');
        };
        /**
         * Return configuration for the tabpanels in the basic search results
         *
         * params - the parameter used to filter results for each tab panel
         * cswServiceId - The id of the csw registry.
         */
        var getTabPanels = function(cswService,basicSearchValue) {
            //Convert our keys/values into a form the controller can read
            var keys = [];
            var values = [];
            var customRegistries=[];
    
            keys.push("basicSearchTerm");
            values.push(basicSearchValue);
            
            keys.push('cswServiceId');
            values.push(cswService.id);
            
            //Create our CSWRecord store (holds all CSWRecords not mapped by known layers)
            var filterCSWStore = Ext.create('Ext.data.Store', {
                model : 'portal.csw.CSWRecord',
                pageSize: 35,
                autoLoad: false,
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                proxy : {
                    type : 'ajax',
                    url : 'getFilteredCSWRecords.do',
                    reader : {
                        type : 'json',
                        rootProperty : 'data',
                        successProperty: 'success',
                        totalProperty: 'totalResults'
                    },
                    extraParams: {
                        key : keys,
                        value : values
                    }
    
                }
    
            });
            
            tabTitle = cswService.title || 'Error retrieving title';
    
            var result={
                    title : tabTitle,
                    xtype: 'gasearchresultspanel',
                    layout : 'fit',
                    map: me.map,
                    layerFactory : me.layerFactory,
                    store : filterCSWStore
                };
    
            return result;
        };
        
        //Create our 'Clear Search' handler
        var clearSearchLinkHandler = function() {
            var gaAdvancedSearchWindow = Ext.getCmp('gaAdvancedSearchWindow');
            if (gaAdvancedSearchWindow) {
                gaAdvancedSearchWindow.destroy();
            }
            var gaSearchResultsWindow = Ext.getCmp('gaSearchResultsWindow');
            if (gaSearchResultsWindow) {
                gaSearchResultsWindow.destroy();
            }
            
            var basicSearchInput = Ext.get('basic-search-input');
            if (basicSearchInput) {
                basicSearchInput.dom.value = '';
            }
        };
        
        //Create our 'Simple Search' handler for the Enter key. 
        var simpleSearchSubmitHandler = function(e) {
            if (e.getKey() == e.ENTER) {
                basicSearchButtonHandler();
            }
        };     
        
        // logo
        var northPanel = {
                id: 'ausgin-logo',
                xtype: 'box',
                height: '59px',
                autoEl: {tag : 'span'}   
        };
           
        // sponsor link
        var attributionPanel = {
                id: 'sponsor-link',
                xtype: 'box',
                height: '30px',
                autoEl: {
                    tag : 'span', 
                    id : "auscope_attrib",
                    html : 'Powered by <a href="//www.auscope.org.au/" target="_blank"><img src="img/logos/AuScope-pic.png" alt="link to Auscope partner"/>AuScope</a>'
                }   
        };

// search panel contains the search controls
        var searchPanel = {
                id: 'search-controls',
                height: '30px',
                xtype: 'box',
                autoEl: {
                    tag: 'span',
                    html: '<label for="basic-search-input">Search for data and publications</label>\
                            <input type="text" id="basic-search-input" name="searchBox" maxlength="50"/>\
                            <span id="basic-search-link"><a href="javascript:void(0)"></a></span>\
                            <a id="advanced-search-link" href="javascript:void(0)">Advanced Search</a>\
                            <a id="clear-search-link" href="javascript:void(0)">Clear Search</a>' 
                }  
        };
        
        // south panel contains the 'Contact Us' and 'Skip to content' links
        var linksPanel = {
            id : "ga-header-south-panel-links",
            height: '40px',
            items: [{
                xtype: 'box',
                id: 'header-controls',
                autoEl: {
                    tag: 'div',
                    html: 
                         '<ul>\
                              <li><a href="mailto:geoscience-portal-aws@ga.gov.au">Contact Us</a></li>\
                              <li><a id="user-guide-link" href="http://www.geoscience.gov.au/Geoscience_Portal_User_Guide.pdf" target="_blank">User Guide</a></li>\
                          </ul> '   
                }       
            }]    
        };

        // center panel contains the "powered by Auscope" logo, search controls and the links
        var centerPanel = {
                id: 'wrapper-search-and-links',
                xtype: 'panel',
                height: '30px',
                layout: 'hbox',
                items: [searchPanel, linksPanel]
            };
        
        // south panel contains the menu bar
        var southPanel = {
            xtype: 'gamenubar',
            map: me.map,
            knownLayerStore: me.knownLayerStore,
            layerFactory: me.layerFactory
        };

        Ext.apply(config, {
            items: [{
                id: 'header-container',
                xtype: 'panel',
                height: '140px',
                layout: 'vbox',
                items: [northPanel, attributionPanel, centerPanel, southPanel]
            }],
        
            listeners: {
                render: function (view) {
                    Ext.get('basic-search-link').on('click', basicSearchButtonHandler);
                    Ext.get('basic-search-input').on('keyup', simpleSearchSubmitHandler);   
                    Ext.get('advanced-search-link').on('click', advancedSearchLinkHandler);
                    Ext.get('clear-search-link').on('click', clearSearchLinkHandler);
                    Ext.get('user-guide-link').on('click', userGuideLinkHandler);
                }
            }
        });
    
        this.callParent(arguments);
    }

});