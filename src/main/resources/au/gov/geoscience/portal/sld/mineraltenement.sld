<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd http://xmlns.geoscience.gov.au/mineraltenementml/1.0 http://schemas.geoscience.gov.au/MineralTenementML/1.0/mineraltenementml.xsd"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:mt="http://xmlns.geoscience.gov.au/mineraltenementml/1.0"
                       xmlns:gml="http://www.opengis.net/gml">
    <NamedLayer>
        <Name>mt:MineralTenement</Name>
        <UserStyle>
            <Name>mineral-tenement-style</Name>
            <IsDefault>true</IsDefault>
            <FeatureTypeStyle>
                <Rule>
                    <PolygonSymbolizer>
                        <Fill>
                            <CssParameter name="fill">#66ff66</CssParameter>
                            <CssParameter name="fill-opacity">0.6</CssParameter>
                        </Fill>
                        <Stroke>
                            <CssParameter name="stroke">#a3ffa3</CssParameter>
                        </Stroke>
                    </PolygonSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
