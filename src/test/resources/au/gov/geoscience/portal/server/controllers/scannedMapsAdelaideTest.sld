<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
        <Name>Scanned_250K_Geological_Map_Index</Name>
        <UserStyle>
            <Name>scanned-maps</Name>
            <FeatureTypeStyle>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsLike escape="!" singleChar="#" wildCard="*">
                            <ogc:PropertyName>name</ogc:PropertyName>
                            <ogc:Literal>*ADELAIDE*</ogc:Literal>
                        </ogc:PropertyIsLike>
                    </ogc:Filter>
                    <Name>250K Scanned Geological Maps</Name>
                    <PolygonSymbolizer>
                        <Fill>
                            <CssParameter name="fill">#ed9c38</CssParameter>
                            <CssParameter name="fill-opacity">0.6</CssParameter>
                        </Fill>
                        <Stroke>
                            <CssParameter name="stroke">#ed9c38</CssParameter>
                            <CssParameter name="stroke-width">1</CssParameter>
                        </Stroke>
                    </PolygonSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>