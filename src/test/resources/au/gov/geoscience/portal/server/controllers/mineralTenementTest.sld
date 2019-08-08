<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:mt="http://xmlns.geoscience.gov.au/mineraltenementml/1.0" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd http://xmlns.geoscience.gov.au/mineraltenementml/1.0 http://schemas.geoscience.gov.au/MineralTenementML/1.0/mineraltenementml.xsd">
    <NamedLayer>
        <Name>mt:MineralTenement</Name>
        <UserStyle>
            <Name>mineral-tenement-style</Name>
            <IsDefault>true</IsDefault>
            <FeatureTypeStyle>
                <Rule>
                    <ogc:Filter>
                        <ogc:And>
                            <ogc:PropertyIsLike escapeChar="!" matchCase="false" singleChar="#" wildCard="*">
                                <ogc:PropertyName>mt:name</ogc:PropertyName>
                                <ogc:Literal>Tenement</ogc:Literal>
                            </ogc:PropertyIsLike>
                            <ogc:PropertyIsLike escapeChar="!" matchCase="false" singleChar="#" wildCard="*">
                                <ogc:PropertyName>mt:owner</ogc:PropertyName>
                                <ogc:Literal>BHPBilliton Limited</ogc:Literal>
                            </ogc:PropertyIsLike>
                        </ogc:And>
                    </ogc:Filter>
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