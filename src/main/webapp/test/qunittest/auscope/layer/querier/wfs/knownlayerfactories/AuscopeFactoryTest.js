//VT: This file tests everything in /AuScope-Portal/src/main/webapp/js/auscope/layer/querier/wfs/knownlayerfactories
//VT : plus AuscopeParser.js and AuScopeKnownLayerParser.js
module( "Test auscope.layer.querier.wfs.knownlayerfactories" );
test( "Test PortrayalBoreholeViewFactory", function() {
    var factory = new auscope.layer.querier.wfs.knownlayerfactories.PortrayalBoreholeViewFactory({
        name : 'Test'
    });
    var knownLayer = Ext.create('portal.knownlayer.KnownLayer', {
        id : 'sf0-borehole-nvcl'
    });
    ok(factory.supportsKnownLayer(knownLayer), 'PortrayalBoreholeView Supports sf0-borehole-nvcl' );

});


test( "Test NVCLFactory", function() {
    var factory = new auscope.layer.querier.wfs.knownlayerfactories.NVCLFactory({
        name : 'Test'
    });
    var knownLayer = Ext.create('portal.knownlayer.KnownLayer', {
        id : 'nvcl-borehole'
    });
    ok(factory.supportsKnownLayer(knownLayer), 'NVCLFactory Supports nvcl-borehole' );

});


test( "Test PressureDBFactory", function() {
    var factory = new auscope.layer.querier.wfs.knownlayerfactories.PressureDBFactory({
        name : 'Test'
    });
    var knownLayer = Ext.create('portal.knownlayer.KnownLayer', {
        id : 'pressuredb-borehole'
    });
    ok(factory.supportsKnownLayer(knownLayer), 'PressureDBFactory Supports pressuredb-borehole' );

});



module( "Test auscope.layer.querier.wfs" );
test( "Test AuScopeParser", function() {
    var auscopeParser = new auscope.layer.querier.wfs.AuScopeParser({
        name : 'test',
    })

    equal(auscopeParser.factoryNames.length,15, 'Count Number of factories configured ');

    var auscopeklParser = new auscope.layer.querier.wfs.AuScopeKnownLayerParser({
        name : 'test',
    })

   equal(auscopeklParser.factoryNames.length,4, 'Count Number of knownlayer factories configured ')

});

QUnit.jUnitReport = function(report) {
    console.log(report.xml);
};





































