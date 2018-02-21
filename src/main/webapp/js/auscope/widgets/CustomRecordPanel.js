/**
 * A specialisation of portal.widgets.panel.CSWRecordPanel for rendering records
 * that are loaded directly from an external WMS source
 */
Ext.define('auscope.widgets.CustomRecordPanel', {
    extend: 'portal.widgets.panel.CSWRecordPanel',

    enableBrowse: false,

    constructor: function(cfg) {
        this.callParent(arguments);
        this.on('afterrender', this._loadQueryBar, this);
        this.enableBrowse = cfg.enableBrowse;
    },

    _loadQueryBar: function() {
        this._updateSearchBar(false);

        var me = this;
        if (this.enableBrowse) {
            this.addDocked(this._getCustomWMS());
            this.addDocked(this._getKMLURL());
            this.addDocked(this._getKMLFile());
        } else {
            this.addDocked({
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    xtype: 'label',
                    text: 'WMS Url:'
                }, {
                    xtype: 'searchfield',
                    store: this.getStore(),
                    width: 286,
                    name: 'STTField',
                    paramName: 'serviceUrl',
                    emptyText: 'http://'
                }]
            });
        }
    },

    addKMLtoPanel: function(name, file) {
        var csw = Ext.create('portal.csw.CSWRecord', {
            id: Ext.id(),
            name: name,
            resourceProvider: 'kml',
            geographicElements: [Ext.create('portal.util.BBox', {
                eastBoundLongitude: 180,
                westBoundLongitude: -180,
                northBoundLatitude: 90,
                southBoundLatitude: -90
            })],
            constraints: [],
            extensions: file,
            recordInfoUrl: "",
            noCache: true
        })
        csw.set('customlayer', true);
        this.getStore().insert(0, csw);
        return csw;
    },

    _getCustomWMS: function() {
        var me = this;
        var panel = Ext.create('Ext.form.Panel', {
            bodyPadding: 8,
            items: [{
                xtype: 'gawmscustomsearchfield',
                fieldLabel: 'WMS URL',
                store: this.getStore(),
                anchor: '100%',
                labelWidth: 60,
                name: 'STTField',
                paramName: 'serviceUrl',
                emptyText: 'http://'
            }, {
                xtype: 'panel',
                html: 'Australian geoscience WMS services can be found by using the search function at the top of this web page ' + 'or by browsing the list of available WMS services <a href="http://www.geoscience.gov.au/wms.html" target="_blank">here</a>.',
                border: false
            }]
        })
        return panel;
    },

    _getKMLURL: function() {
        var baseform = this.filterForm;
        var me = this;
        var panel = Ext.create('Ext.form.Panel', {
            bodyPadding: 8,
            items: [{
                xtype: 'textfield',
                name: 'url',
                fieldLabel: 'KML URL',
                labelWidth: 60,
                msgTarget: 'side',
                allowBlank: false,
                anchor: '100%',
                emptyText: 'http://'
            }],
            buttons: [{
                text: 'Add KML',
                handler: function() {
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            url: 'addKMLUrl.do',
                            params: {
                                url: form.getFieldValues().file
                            },
                            waitMsg: 'Adding KML Layer...',
                            success: function(fp, o) {
                                var tabpanel = Ext.getCmp('auscope-tabs-panel');
                                var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                                tabpanel.setActiveTab(customPanel);
                                customPanel.addKMLtoPanel(o.result.data.name, o.result.data.file);
                            },
                            failure: function(fp, action) {
                                Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file.');
                            }
                        });
                    }
                }
            }]
        });
        return panel;
    },

    _getKMLFile: function() {
        var baseform = this.filterForm;
        var me = this;
        var panel = Ext.create('Ext.form.Panel', {
            bodyPadding: 8,
            items: [{
                xtype: 'filefield',
                name: 'file',
                fieldLabel: 'KML File',
                labelWidth: 60,
                msgTarget: 'side',
                allowBlank: false,
                anchor: '100%',
                buttonText: 'Select KML...'
            }],
            buttons: [{
                text: 'Add KML',
                handler: function() {
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            url: 'addKMLLayer.do',
                            waitMsg: 'Adding KML Layer...',
                            success: function(fp, o) {
                                var tabpanel = Ext.getCmp('auscope-tabs-panel');
                                var customPanel = me.ownerCt.getComponent('org-auscope-custom-record-panel');
                                tabpanel.setActiveTab(customPanel);
                                customPanel.addKMLtoPanel(o.result.name, o.result.file);
                            },
                            failure: function(fp, action) {
                                Ext.Msg.alert('Status', 'Unable to parse file. Make sure the file is a valid KML file.');
                            }
                        });
                    }
                }
            }]
        });
        return panel;
    },
});
