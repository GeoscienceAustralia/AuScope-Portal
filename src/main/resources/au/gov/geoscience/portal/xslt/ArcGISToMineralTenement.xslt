<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:mt="http://xmlns.geoscience.gov.au/mineraltenementml/1.0"
    xmlns:esri_wms="http://www.esri.com/wms" xmlns:wfs="http://www.opengis.net/wfs"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:gml="http://www.opengis.net/gml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.geoscience.gov.au/mineraltenementml/1.0 http://schemas.geoscience.gov.au/MineralTenementML/1.0/mineraltenementml.xsd http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd"
    exclude-result-prefixes="esri_wms">
    <xsl:output method="xml" indent="no" />
    <xsl:template match="/">
        <wfs:FeatureCollection
            xsi:schemaLocation="http://xmlns.geoscience.gov.au/mineraltenementml/1.0 http://schemas.geoscience.gov.au/MineralTenementML/1.0/mineraltenementml.xsd http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd">
            <xsl:apply-templates select="esri_wms:FeatureInfoResponse" />
        </wfs:FeatureCollection>
    </xsl:template>
    <xsl:template match="esri_wms:FeatureInfoResponse">
        <gml:featureMembers>
            <xsl:apply-templates select="esri_wms:FIELDS" />
        </gml:featureMembers>
    </xsl:template>
    <xsl:template match="esri_wms:FIELDS">
        <xsl:element name="mt:MineralTenement">
            <xsl:apply-templates select="@ObjectID"></xsl:apply-templates>
            <xsl:apply-templates
                select="@identifier | @name | @tenementType | @commodity | @owner | @status | @area | @applicationDate | @grantDate | @expireDate | @fileID | @genericSymbolizer | @tenementType_uri | @status_uri | @jurisdiction_uri" />
            <mt:shape></mt:shape>
        </xsl:element>
    </xsl:template>
    <xsl:template match="@ObjectID">
        <xsl:attribute name="gml:id">
            <xsl:value-of select="concat('mt.mineraltenement.', .)" />
        </xsl:attribute>
    </xsl:template>
    <xsl:template
        match="@identifier | @name | @tenementType | @commodity | @owner | @status | @area | @applicationDate | @grantDate | @expireDate | @fileID | @genericSymbolizer | @tenementType_uri | @status_uri | @jurisdiction_uri">
        <xsl:element name="mt:{name()}">
            <xsl:value-of select="." />
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>