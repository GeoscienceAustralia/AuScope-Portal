/**
 * This is the GA portal footer.
 * It contains the logos of the state and federal agencies that contributed to the portal. 
 */
Ext.define('ga.widgets.GAFooter', {
    extend : 'Ext.panel.Panel',
    alias: 'widget.gafooter',

    constructor : function(config){   
        Ext.apply(config, {
            items: [{
                xtype : 'panel',
                id: 'footer-container',
                items : [{
                    xtype : 'box',
                    autoEl : {
                            tag : 'span',
                            html: '<img alt="Geoscience Australia Logo" src="img/logos/ga.jpg"/>'   
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="NSW Trade and Investment Resources and Energy Logo" src="img/logos/nsw.jpg"/>'        
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="Queensland Government Logo" src="img/logos/qld.jpg"/>'                  
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="Mineral Resources Tasmania Logo" src="img/logos/tas.jpg"/>'                  
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="Northern Territory Government Logo" src="img/logos/nt.jpg"/>'                  
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="Victorian Department of State Development Business and Innovation Logo" src="img/logos/vic.jpg"/>'                  
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="South Australia Department of Manufacturing, Innovation, Trade, Resources and Energy Logo" src="img/logos/sa.jpg"/>'                  
                        }
                    },{
                        xtype : 'box',
                        autoEl : {
                            tag : 'span',
                            html: '<img alt="Western Australia Department of Mines and Petroleum Logo" src="img/logos/wa.jpg"/>'                  
                        }
                    }]
                }]
            
            });
        
            this.callParent(arguments);
        
        }
});