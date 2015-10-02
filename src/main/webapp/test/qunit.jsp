<html>
<head>
  <meta charset="utf-8">
  <title>Auscope portal qunit test</title>
  <!-- QUNIT TEST IMPORT -->
  <link rel="stylesheet" href="portal-core/js/qunit/qunit-1.14.0.css">

  <script src="portal-core/js/qunit/qunit-1.14.0.js"></script>
  <script src="portal-core/js/qunit/qunit-reporter-junit.js"></script>

        <%-- Open Layers Imports --%>
      <link rel="stylesheet" href="portal-core/js/OpenLayers-2.12/theme/default/style.css" type="text/css">
      <script src="portal-core/js/OpenLayers-2.12/OpenLayers.js" type="text/javascript"></script>

     <%-- JS imports - relative paths back to the webapp directory --%>
      <jsp:include page="../portal-core/jsimports.htm"/>
      <jsp:include page="../portal-core/jsimports-gmap.htm"/>
      <jsp:include page="../portal-core/jsimports-openlayers.htm"/>
      <jsp:include page="../jsimports.htm"/>

      <%-- CSS imports - relative paths back to the webapp directory--%>
      <jsp:include page="../portal-core/cssimports.htm"/>


<!-- END REFERENCE FILE HERE -->

</head>
<body>
  <div id="qunit"></div>
  <div id="qunit-fixture"></div>
  <script src="test/qunittest/portal-core/portal/layer/querier/wfs/factories/BaseFactoryTest.js"></script>
  <script src="test/qunittest/auscope/layer/querier/wfs/knownlayerfactories/AuscopeFactoryTest.js"></script>
  <script src="test/qunittest/portal-core/portal/events/AppEventsTest.js"></script>
</body>
</html>