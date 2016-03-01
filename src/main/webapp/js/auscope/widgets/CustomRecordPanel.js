/**
 * A specialisation of portal.widgets.panel.CSWRecordPanel for rendering
 * records that are loaded directly from an external WMS source
 */
Ext.define('auscope.widgets.CustomRecordPanel', {
    extend : 'portal.widgets.panel.CSWRecordPanel',

    enableBrowse : false,

    constructor : function(cfg) {
        this.callParent(arguments);
        this.on('afterrender', this._loadQueryBar, this);
        this.enableBrowse = cfg.enableBrowse;
    },

    _loadQueryBar : function() {
        this._updateSearchBar(false);

        var me = this;
        if(this.enableBrowse){
            //var menuItems = [this._getRegistryAction(),this._getHandleKMLAction()];
            this.addDocked({
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    xtype : 'label',
                    text : 'WMS Url:',
                    width: 56
                },{
                    xtype : 'wmscustomsearchfield',
                    store : this.getStore(),
                    width : 286,
                    name : 'STTField',
                    paramName: 'service_URL',
                    emptyText : 'http://'
                }]
            });
            this.addDocked(this._getKMLURL());
            this.addDocked(this._getKMLFile());
        }else{
            this.addDocked({
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    xtype : 'label',
                    text : 'WMS Url:'
                },{
                    xtype : 'searchfield',
                    store : this.getStore(),
                    width : 286,
                    name : 'STTField',
                    paramName: 'service_URL',
                    emptyText : 'http://'
                }]
            });
        }

    },
    


    addKMLtoPanel : function(name,file){
        var csw = Ext.create('portal.csw.CSWRecord',{
            id : Ext.id(),
            name : name,
            resourceProvider : 'kml',
            geographicElements : [Ext.create('portal.util.BBox',{
                eastBoundLongitude : 180,
                westBoundLongitude : -180,
                northBoundLatitude : 90,
                southBoundLatitude : -90
            })],
            constraints : [],
            extensions : file,
            recordInfoUrl : "",
            noCache : true
        })
        csw.set('customlayer',true);
        this.getStore().insert(0,csw);
        return csw;
    },

  
    
    _getKMLURL : function() {
    	var baseform = this.filterForm;
    	var me = this;
		var panel = Ext.create('Ext.form.Panel', {
			bodyPadding : 8,
			items : [ {
				 xtype: 'textfield',
	                value : 'https://capdf-dev.csiro.au/gs-hydrogeochem/public/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=public:hydrogeochem&maxFeatures=50&outputFormat=application/vnd.google-earth.kml+xml',
	                name: 'url',
	                fieldLabel: 'KML URL',
	                labelWidth: 58,
	                msgTarget: 'side',
	                allowBlank: false,
	                anchor: '100%'
			} ],
			buttons : [ {
			text : 'Add KML',
				handler : function() {
					var form = this.up('form').getForm();
					if(form.isValid()){
                        form.submit({
                            url: 'addKMLUrl.do',
                            params:{
                              url : form.getFieldValues().file
                            },
                            waitMsg: 'Adding KML Layer...',
                            success: function(fp, o) {

                               var tabpanel =  Ext.getCmp('auscope-tabs-panel');
                               var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                               tabpanel.setActiveTab(customPanel);

                               customPanel.addKMLtoPanel(o.result.data.name,o.result.data.file);


                            },
                            failure : function(fp,action){
                                Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file.');
                            }
                        });
                    }
				}
			} ]
		});
		return panel;
	},
    
    _getKMLFile : function() {
    	var baseform = this.filterForm;
    	var me = this;
    	var panel = Ext.create('Ext.form.Panel',{
    		bodyPadding: 8,
            // title : 'KML URL',
            items: [{
                    xtype: 'filefield',
                    name: 'file',
                    fieldLabel: 'KML File',
                    labelWidth: 58,
                    msgTarget: 'side',
                    allowBlank: false,
                    anchor: '100%',
                    buttonText: 'Select KML...'
                }],
                buttons: [{
                    text: 'Add KML',
                    handler: function() {
                    	var form = this.up('form').getForm();
                    	if(form.isValid()){
                            form.submit({
                                url: 'addKMLLayer.do',
                                waitMsg: 'Adding KML Layer...',
                                success: function(fp, o) {

                                   var tabpanel =  Ext.getCmp('auscope-tabs-panel');
                                   var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                                   tabpanel.setActiveTab(customPanel);

                                   customPanel.addKMLtoPanel(o.result.name,o.result.file);


                                },
                                failure : function(fp,action){
                                    Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file.');
                                }
                            });
                        }
                    }
                }
                ]
    		}
    	);
    	return panel;
    },





});