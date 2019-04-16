<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
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
                    <MinScaleDenominator>1000000</MinScaleDenominator>
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
                <Rule>
                    <MaxScaleDenominator>3000000</MaxScaleDenominator>
                    <PolygonSymbolizer>
                        <Fill>
                            <CssParameter name="fill">#66ff66</CssParameter>
                            <CssParameter name="fill-opacity">0.6</CssParameter>
                        </Fill>
                        <Stroke>
                            <CssParameter name="stroke">#a3ffa3</CssParameter>
                        </Stroke>
                    </PolygonSymbolizer>
                    <TextSymbolizer>
                        <Label>
                            <!-- This horrific hack is required to get appschema labels to display correctly -->
                            <ogc:Function name="strSubstringStart">
                                <ogc:PropertyName>mt:name</ogc:PropertyName>
                                <ogc:Function name="parseInt">
                                    <ogc:Literal>27</ogc:Literal>
                                </ogc:Function>
                            </ogc:Function>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">SansSerif</CssParameter>
                            <CssParameter name="font-size">10</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <PointPlacement>
                                <AnchorPoint>
                                    <AnchorPointX>0.0</AnchorPointX>
                                    <AnchorPointY>0.0</AnchorPointY>
                                </AnchorPoint>
                            </PointPlacement>
                        </LabelPlacement>
                        <Halo>
                            <Radius>3</Radius>
                            <Fill>
                                <CssParameter name="fill">#ffffff</CssParameter>
                                <CssParameter name="fill-opacity">1</CssParameter>
                            </Fill>
                        </Halo>
                        <Fill>
                            <CssParameter name="fill">#013220</CssParameter>
                        </Fill>
                    </TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
