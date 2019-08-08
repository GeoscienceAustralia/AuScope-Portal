<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.opengis.net/kml/2.2"
    xmlns:er="urn:cgi:xmlns:GGIC:EarthResource:1.1" xmlns:geodesy="http://www.auscope.org/geodesy"
    xmlns:gml="http://www.opengis.net/gml" xmlns:gsml="urn:cgi:xmlns:CGI:GeoSciML:2.0"
    xmlns:ngcp="http://www.auscope.org/ngcp" xmlns:sa="http://www.opengis.net/sampling/1.0"
    xmlns:wfs="http://www.opengis.net/wfs" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mt="WFS"
    exclude-result-prefixes="er geodesy gml gsml mt ngcp sa wfs xsl xlink">
    <xsl:output method="text" encoding="utf-8"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="wfs:FeatureCollection">
        <xsl:text>mt:ObjectID,mt:identifier,mt:name,mt:tenementType,mt:commodity,mt:owner,mt:status,mt:applicationDate,mt:grantDate,mt:expireDate,mt:fileID,mt:tenementType_uri,mt:status_uri,mt:jurisdiction_uri,mt:shape</xsl:text>
        <xsl:text>&#10;</xsl:text>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="gml:boundedBy"/>
    <xsl:template match="gml:featureMember/mt:MineralTenement">

        <xsl:text>"</xsl:text>
        <xsl:value-of select="mt:ObjectID"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:identifier"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:name"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:tenementType"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:commodity"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:owner"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:status"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:applicationDate"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:grantDate"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:expireDate"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:fileID"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:tenementType_uri"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:status_uri"/>
        <xsl:text>","</xsl:text>
        <xsl:value-of select="mt:jurisdiction_uri"/>
        <xsl:text>"</xsl:text>
        
        <xsl:text>&#10;</xsl:text>


    </xsl:template>
</xsl:stylesheet>
