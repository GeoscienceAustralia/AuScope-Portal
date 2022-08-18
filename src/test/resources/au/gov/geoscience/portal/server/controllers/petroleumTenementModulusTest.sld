<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:pt="http://xmlns.geoscience.gov.au/petroleumtenementml/1.0" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.3.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.1.0/StyledLayerDescriptor.xsd http://xmlns.geoscience.gov.au/petroleumtenementml/1.0 http://schemas.geoscience.gov.au/PetroleumTenementML/1.0/petroleumtenementml.xsd">
    <NamedLayer>
        <Name>pt:PetroleumTenement</Name>
        <UserStyle>
            <Name>petroleum-tenement-style</Name>
            <IsDefault>true</IsDefault>
            <FeatureTypeStyle>
                <Rule>
                    <ogc:Filter>
                        <ogc:And>
                            <ogc:PropertyIsLike escapeChar="!" matchCase="false" singleChar="#" wildCard="*">
                                <ogc:Function name="strReplace">
                                <ogc:PropertyName>pt:name</ogc:PropertyName>
                                    <ogc:Literal>\n</ogc:Literal>
                                    <ogc:Literal></ogc:Literal>
                                    <ogc:Literal>true</ogc:Literal>
                                </ogc:Function>
                                <ogc:Literal>Tenement</ogc:Literal>
                            </ogc:PropertyIsLike>
                            <ogc:PropertyIsLike escapeChar="!" matchCase="false" singleChar="#" wildCard="*">
                                <ogc:Function name="strReplace">
                                <ogc:PropertyName>pt:holder</ogc:PropertyName>
                                    <ogc:Literal>\n</ogc:Literal>
                                    <ogc:Literal></ogc:Literal>
                                    <ogc:Literal>true</ogc:Literal>
                                </ogc:Function>
                                <ogc:Literal>BHPBilliton Limited (100%)</ogc:Literal>
                            </ogc:PropertyIsLike>
                        </ogc:And>
                    </ogc:Filter>
                    <PolygonSymbolizer>
                        <Fill>
                            <CssParameter name="fill">#d3faff</CssParameter>          <!-- pale blue polygon fill colour -->
                            <CssParameter name="fill-opacity">0.6</CssParameter>
                        </Fill>
                        <Stroke>
                            <CssParameter name="stroke">#0813a8</CssParameter>        <!-- dark blue polygon outline colour -->
                        </Stroke>
                    </PolygonSymbolizer>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>pt:name</ogc:PropertyName>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">SansSerif</CssParameter>
                            <CssParameter name="font-size">10</CssParameter>
                            <CssParameter name="font-style">normal</CssParameter>
                            <CssParameter name="font-weight">normal</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <PointPlacement>
                                <AnchorPoint>
                                    <AnchorPointX>0.0</AnchorPointX>
                                    <AnchorPointY>0.0</AnchorPointY>
                                </AnchorPoint>
                                <Displacement>
                                    <DisplacementX>0</DisplacementX>
                                    <DisplacementY>0</DisplacementY>
                                </Displacement>
                            </PointPlacement>
                        </LabelPlacement>
                        <Halo>
                            <Radius>2</Radius>
                            <Fill>
                                <CssParameter name="fill">#ffffff</CssParameter>
                            </Fill>
                        </Halo>
                        <Fill>
                            <CssParameter name="fill">#000000</CssParameter>
                        </Fill>
                    </TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
