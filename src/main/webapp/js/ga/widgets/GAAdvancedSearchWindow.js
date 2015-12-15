
Ext.define('ga.widgets.GAAdvancedSearchWindow', {
    extend : 'Ext.window.Window',

    cswFilterFormPanel : null,   
    
    map : null,
    layerFactory : null,
    layerStore : null,
    
    constructor : function(cfg) {      
        
    	var me = this;
    	
        me.map = cfg.map;
        me.layerFactory = cfg.layerFactory;
        me.layerStore = cfg.layerStore;
                
        me.cswFilterFormPanel = new ga.widgets.GAAdvancedSearchPanel({
            name : 'Filter Form',
            map: me.map
        });
        
    	var controlButtons = [{
            xtype: 'button',
            text:'Reset Form',
            handler:function(button){
                me.cswFilterFormPanel.resetForm();                        
            }
    	},{            
            xtype: 'button',
            text: 'Search',
            scope : me,
            iconCls : 'add',
            handler: function(button) {
                var parent = button.findParentByType('window');
                var panel = parent.getComponent(0);

                if (panel.getForm().isValid()) {                 
                    var additionalParams = panel.getForm().getValues(false, false, false, false);
                    var filteredResultPanels=[];
                    for(additionalParamKey in additionalParams){
                        if(additionalParamKey == 'cswServiceId'){
                            if(!(additionalParams[additionalParamKey] instanceof Array)){
                                additionalParams[additionalParamKey]=[additionalParams[additionalParamKey]]
                            }
                            for(var j=0; j < additionalParams[additionalParamKey].length;j++){
                                //VT:
                                filteredResultPanels.push(this._getTabPanels(additionalParams,additionalParams[additionalParamKey][j]));
                            }
                        }
                    }
                    parent.fireEvent('filterselectcomplete',filteredResultPanels);
                    parent.hide();  
                    
                } else {
                    Ext.Msg.alert('Invalid Data', 'Please correct form errors.')
                }
            }
        }];
        
        Ext.apply(cfg, {
            title : 'Enter Parameters',
            layout : 'fit',
            modal : false,
            width : 500,
            items : [me.cswFilterFormPanel],
            buttons: controlButtons            
        });


        this.callParent(arguments);
    },
    
    /**
     * Return configuration for the tabpanels
     *
     * params - the parameter used to filter results for each tab panel
     * cswServiceId - The id of the csw registry.
     */
    _getTabPanels : function(params,cswServiceId) {
        var me = this;
        
        //Convert our keys/values into a form the controller can read
        var keys = [];
        var values = [];
        var customRegistries=[];

        var additionalParams = params;

        //Utility function
        var denormaliseKvp = function(keyList, valueList, kvpObj) {
            if (kvpObj) {
                for (key in kvpObj) {
                    if (kvpObj[key]) {
                        var value = kvpObj[key].toString();
                        if(value.length>0 && key != 'cswServiceId' && !(key.slice(0, 4) == 'DNA_')){
                            keyList.push(key);
                            valueList.push(value);
                        }
                    }
                }
            }
        };


        denormaliseKvp(keys, values, additionalParams);
        if(typeof cswServiceId.id == 'undefined'){
            keys.push('cswServiceId');
            values.push(cswServiceId);
        }

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
                    value : values,
                    customregistries : {
                        id: cswServiceId.id,
                        title: cswServiceId.title,
                        serviceUrl: cswServiceId.serviceUrl,
                        recordInformationUrl: cswServiceId.recordInformationUrl
                    }
                }

            }

        });

        var registriesArray = Ext.getCmp('registryTabCheckboxGroup').getChecked();
        var title = "Error retrieving title";
        for(var i = 0; i < registriesArray.length; i ++){
            if(registriesArray[i].inputValue === cswServiceId){
                title = registriesArray[i].boxLabel;
                break;
            }
        }


        var result={
                title : title,
                xtype: 'gasearchresultspanel',
                layout : 'fit',
                store : filterCSWStore,
                map : me.map,
                layerFactory : me.layerFactory,
                layerStore : me.layerStore
            };

        return result;

    }
});

