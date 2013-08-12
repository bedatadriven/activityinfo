<?xml version="1.0" encoding="UTF-8"?>

<h:html xmlns="http://www.w3.org/2002/xforms"
        xmlns:h="http://www.w3.org/1999/xhtml"
        xmlns:ev="http://www.w3.org/2001/xml-events"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:jr="http://openrosa.org/javarosa">
    <h:head>
        <h:title>${name?xml}</h:title> 
    
        <model>
            <instance>
                <data id="siteform">
                    <meta>
                        <instanceID />
                    </meta>
                    <activity>${id?c}</activity>
          
                    <partner><#if database.partners?size == 1>${database.partners[0].id}</#if></partner>
                    <locationname />
                    <gps />
                    <date1>${.now?string("yyyy-MM-dd")}</date1>
                    <date2>${.now?string("yyyy-MM-dd")}</date2>

                    <#list indicators as indicator>
                    <indicator-${indicator.id?c} />
                    </#list>
          
                    <#list attributeGroups as attributeGroup>
                    <attributeGroup-${attributeGroup.id?c} />
                    </#list>
          
                    <comments/>
                </data>
            </instance>

            <bind nodeset="/data/meta/instanceID" type="string" readonly="true()" calculate="concat('uuid:',uuid())" />
            <bind nodeset="/data/activity" required="true()"/>
            <bind nodeset="/data/partner" required="true()"/>
            <bind nodeset="/data/gps" type="geopoint" required="true()"/>
            <bind nodeset="/data/date1" type="date" required="true()"/>
            <bind nodeset="/data/date2" type="date" required="true()"
                constraint=". >= /data/date1" jr:constraintMsg="date must be on or after /data/date1"/>
      
            <#list indicators as indicator>
            <bind nodeset="/data/indicator-${indicator.id?c}" type="decimal" ${indicator.mandatory?string("required=\"true()\"", "")}/>
            </#list>

            <#list attributeGroups as attributeGroup>
            <#if attributeGroup.mandatory>
            <bind nodeset="/data/attributeGroup-${attributeGroup.id?c}" required="true()"/>
            </#if>
            </#list>
        </model>
    </h:head>
  
    <h:body>
        <#if (database.partners?size > 1)>
        <select1 ref="/data/partner">
            <label>${label.odkPartner}</label>
            <#list database.partners as partner>
            <item>
                <label>${partner.name}</label>
                <value>${partner.id?c}</value>
            </item>
            </#list>
        </select1>
        </#if>
  
        <group>
            <label>${label.odkLocation}</label>
            <input ref="/data/locationname">
                <label>${label.odkLocationName}</label>
            </input>
            <input ref="/data/gps">
                <label>${label.odkLocationCoordinates}</label>
            </input>
        </group>
    
        <group>
            <input ref="/data/date1">
                <label>${label.odkStartDate}</label>
            </input>
            <input ref="/data/date2">
                <label>${label.odkEndDate}</label>
            </input>
        </group>

        <#list indicators as indicator>
        <input ref="/data/indicator-${indicator.id?c}">
            <label>${indicator.name}</label>
            <hint>${indicator.units}</hint>
        </input>
        </#list>

        <#list attributeGroups as attributeGroup>
        <${attributeGroup.multipleAllowed?string("select", "select1")} ref="/data/attributeGroup-${attributeGroup.id?c}">
            <label>${attributeGroup.name}</label>
            <#list attributeGroup.attributes as attribute>
            <item>
                <label>${attribute.name}</label>
                <value>${attribute.id?c}</value>
            </item>
            </#list>            
        </${attributeGroup.multipleAllowed?string("select", "select1")}>
        </#list>

        <input ref="/data/comments">
            <label>${label.odkComments}</label>
        </input>
    </h:body>
</h:html>
