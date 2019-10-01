<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd http://remanentanomalies.csiro.au/schemas/anomaly.xsd"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:gml="http://www.opengis.net/gml"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:RemAnom="http://remanentanomalies.csiro.au"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>
        <Name>RemAnom:Anomaly</Name>
        <UserStyle>
            <Name>defaultRemAnomStyle</Name>
            <Title>defaultRemAnomStyle</Title>
            <Abstract>defaultRemAnomStyle</Abstract>
            <IsDefault>1</IsDefault>
            <FeatureTypeStyle>
                <Rule>
                    <Name>Anomaly</Name>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>square</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#FF0000</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>8</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
