<?xml version='1.0' encoding="ISO-8859-1" ?>
<schema
        targetNamespace="http://www.activityinfo.org/schemas"
        xmlns:ai="http://www.activityinfo.org/schemas"
        xmlns:ogc="http://www.opengis.net/ogc"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns="http://www.w3.org/2001/XMLSchema"
        xmlns:gml="http://www.opengis.net/gml"
        elementFormDefault="qualified" version="0.1" >

    <import namespace="http://www.opengis.net/gml"
            schemaLocation="http://schemas.opengeospatial.net//gml/2.1.2/feature.xsd" />

    <#list activities as activity>

    <element name="activity${activity.id}"
             type="ai:activity${activity.id}"
             substitutionGroup="gml:_Feature" />

    <complexType name="CBC_PYType">
        <complexContent>
            <extension base="gml:AbstractFeatureType">
                <sequence>

                    <element name="msGeometry" type="gml:GeometryPropertyType" minOccurs="0" maxOccurs="1"/>
                    <element name="id" type="integer"/>
                    <element name="partner" type="string"/>
                    <element name="location" type="string"/>
                    <element name="axe" type="string"/>
                    <element name="date1" type="date"/>
                    <element name="date2" type="date"/>
                    <#list activity.indicators as indicator>
                    <element name="indicator${indicator.id}" type="decimal"/>
                    </#list>
                    <#list activity.attributeGroups as group>
                        <#list group.attributes as attribute>
                            <element name="attribute${attribute.id}" type="string"/>
                        </#list>
                    </#list>
                    <#list activity.database.country.adminLevels as level>
                    <element name="${level.name}" type="string"/>
                    <element name="${level.name}_code" type="string"/>
                    </#list>
                    <element name="latitude" type="decimal"/>
                    <element name="longitude" type="decimal"/>
                </sequence>
            </extension>
        </complexContent>
    </complexType>

    </#list>

</schema>
