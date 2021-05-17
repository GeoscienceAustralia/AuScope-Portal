<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml">
    <xsl:output method="text" encoding="UTF-8"/>


    <xsl:variable name="fields">
        <xsl:for-each select="wfs:FeatureCollection">
            <xsl:for-each-group select="gml:featureMember/*/child::*" group-by="name()">
                <xsl:choose>
                    <xsl:when test="./gml:MultiSurface|./gml:MultiCurve"/>
                    <xsl:otherwise>
                        <field>
                            <xsl:value-of select="name()"/>
                        </field>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each-group>
        </xsl:for-each>
    </xsl:variable>
    <xsl:template match="/">

        <xsl:for-each select="$fields/field">
            <xsl:text>"</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>"</xsl:text>
            <xsl:if test="position() != last()">
                <xsl:text>,</xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>&#10;</xsl:text>

        <xsl:for-each select="wfs:FeatureCollection/gml:featureMember/*">
            <xsl:variable name="current-record" select="."/>
            <xsl:for-each select="$fields/field">
                <xsl:text>"</xsl:text>
                <xsl:value-of
                        select="$current-record/child::*[name() = current()[text()]]"/>
                <xsl:text>"</xsl:text>
                <xsl:if test="position() != last()">
                    <xsl:text>,</xsl:text>
                </xsl:if>
            </xsl:for-each>
            <xsl:if test="position() != last()">
                <xsl:text>&#10;</xsl:text>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>