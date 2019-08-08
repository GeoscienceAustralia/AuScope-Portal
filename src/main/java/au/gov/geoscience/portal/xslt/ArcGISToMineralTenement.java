package au.gov.geoscience.portal.xslt;

import org.auscope.portal.core.xslt.PortalXSLTTransformer;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

@Component
public class ArcGISToMineralTenement extends PortalXSLTTransformer {

	public ArcGISToMineralTenement() {
		super("/au/gov/geoscience/portal/xslt/ArcGISToMineralTenement.xslt");
	}
	
    /**
     * Utility method to transform a WFS response into kml
     *
     * @param wfs
     *            WFS response to be transformed
     * @param serviceUrl
     *            The WFS URL where the response came from
     * @return Kml output string
     */
    public String convert(String wfs, String serviceUrl) {
        return convert(new StreamSource(new StringReader(wfs)), serviceUrl);
    }

    /**
     * Utility method to transform a WFS response into kml
     *
     * @param wfs
     *            WFS response to be transformed
     * @param serviceUrl
     *            The WFS URL where the response came from
     * @return Xml output string
     */
    public String convert(InputStream wfs, String serviceUrl) {
        return convert(new StreamSource(wfs), serviceUrl);
    }

    /**
     * Utility method to transform a WFS response into kml
     *
     * @param wfs
     *            WFS response to be transformed
     * @param serviceUrl
     *            The WFS URL where the response came from
     * @return Xml output string
     */
    public String convert(StreamSource wfs, String serviceUrl) {
        Properties stylesheetParams = new Properties();
        stylesheetParams.setProperty("serviceURL", serviceUrl);
        return convert(wfs, stylesheetParams);
    }
}
