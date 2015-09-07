<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!-- Credits for icons from http://www.fatcow.com/free-icons/ under http://creativecommons.org/licenses/by/3.0/us/-->
<html xmlns:v="urn:schemas-microsoft-com:vml">
   <head>
      <title>GA Discovery Portal</title>

      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="description" content="Access geoscientific information from around Australia, via AuScopes national e-Research infrastructure.">
      <meta name="keywords" content="AuScope, Discovery, Resources, GeoSciML, Mineral Occurrence, Geologic Unit, Australia">
      <meta name="author" content="AuScope">
      <meta charset="UTF-8" />
                 

      <%-- Open Layers Imports --%>
      <link rel="stylesheet" href="portal-core/js/OpenLayers-2.12/theme/default/style.css" type="text/css">
      <script src="portal-core/js/OpenLayers-2.12/OpenLayers.js" type="text/javascript"></script>
      <script src="portal-core/js/threejs/three.min.js" type="text/javascript"></script>
      <script src="portal-core/js/threejs/controls/OrbitControls.js" type="text/javascript"></script>
      <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
      <script src="portal-core/js/d3Legends/d3.legend.js" type="text/javascript"></script>
      
      
      

      <script type="text/javascript">
         var VOCAB_SERVICE_URL = "${vocabServiceUrl}";
         var NVCL_WEB_SERVICE_IP = "${nvclWebServiceIP}";
         var MAX_FEATURES = "${maxFeatureValue}";
         var LOCALHOST = "${localhost}";
         var WEB_CONTEXT = '<%= request.getContextPath() %>';
      </script>

      <%-- JS imports - relative paths back to the webapp directory --%>
      <jsp:include page="../../portal-core/jsimports.htm"/>
      <jsp:include page="../../portal-core/jsimports-gmap.htm"/>
      <jsp:include page="../../portal-core/jsimports-openlayers.htm"/>
      <jsp:include page="../../jsimports.htm"/>

      <%-- CSS imports - relative paths back to the webapp directory--%>
      <jsp:include page="../../portal-core/cssimports.htm"/>      
      <jsp:include page="../../cssimports.htm"/>

      <script src="js/auscope/Main-UI.js" type="text/javascript"></script>
            

      <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
      <c:if test="${not empty analyticKey}">
          <script type="text/javascript">

          var _gaq = _gaq || [];
          _gaq.push(['_setAccount', '${analyticKey}']);
          _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
              })();

          </script>
      </c:if>
      
    <!-- Piwik -->
    <c:if test="${not empty piwikSiteId}">
		<script type="text/javascript">
		  var _paq = _paq || [];
		  _paq.push(['trackPageView']);	
		  _paq.push(['trackVisibleContentImpressions']);
		  _paq.push(['enableLinkTracking']);
		  (function() {
		    var u="//cg-admin.arrc.csiro.au/piwik/";
		    _paq.push(['setTrackerUrl', u+'piwik.php']);
		    _paq.push(['setSiteId', parseInt('${piwikSiteId}')]);
		    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
		    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
		  })();
		</script>
		<noscript><p><img src="//cg-admin.arrc.csiro.au/piwik/piwik.php?idsite=1" style="border:0;" alt="" /></p></noscript>
	</c:if>
    <!-- End Piwik Code -->
      
      
   </head>

    <body>
      <!-- Include Navigation Header -->
      <%@ include file="page_header.jsp" %>
   </body>


</html>