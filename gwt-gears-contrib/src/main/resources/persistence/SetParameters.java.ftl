
<#macro setParametersFromEntity entity property>
    <@setParametersFromProperty value=(entity + "." + property.getterName + "()") property=property/>
</#macro>

<#macro setParametersFromProperty value property>
    <#if property.embedded == true>
        <#-- this property is an object, but it is embedded in this table, so we need to
             check if it's null and set multiple parameters -->

        if(${value} == null) {
            <#assign embeddedStartIndex = index>
            <#list property.embeddedClass.properties as embeddedProperty>
                stmt.setNull(${index}, 0);
                <#assign index = index + 1>
            </#list>
        } else {
            <#assign index = embeddedStartIndex>
            <#list property.embeddedClass.properties as embeddedProperty>
                stmt.${embeddedProperty.column.stmtSetter}(${index}, ${value}.${embeddedProperty.getterName}());
                <#assign index = index + 1>
            </#list>
        }
    <#elseif property.toOne == true>

        <#-- this property is an entity, so we only store the foreign key(s) -->

        if(${value} == null) {
            stmt.setNull(${index}, 0);
        } else {
            stmt.${property.column.stmtSetter}(${index}, ${value}.${property.relatedEntity.id.getterName}());
        }
        <#assign index = index + 1>

    <#elseif property.primitive == true>
        <#-- this primitive property corresponds to a single column OR it's a String/Date which we can
             safely pass nulls to, so we can just set it -->
        stmt.${property.column.stmtSetter}(${index}, ${value});

        <#assign index = index + 1>
    <#else>
        <#-- this property corresponds to a single column, but it is an object and might be null -->
        if(${value} == null)
            stmt.setNull(${index}, 0);
        else
            stmt.${property.column.stmtSetter}(${index}, ${value});
        <#assign index = index + 1>
    </#if>
</#macro>

<#macro putColumnValuesFromEntity entity property>
    <@setParametersFromProperty value=(entity + "." + property.getterName + "()") property=property/>
</#macro>


<#macro putColumnValuesFromProperty value property>
    <#if property.embedded == true>
        <#-- this property is an object, but it is embedded in this table, so we need to
             check if it's null and set multiple parameters -->

        if(${value} == null) {
            <#list property.embeddedClass.properties as embeddedProperty>
                map.put(${embeddedProperty.column}, null);
            </#list>
        } else {
            <#list property.embeddedClass.properties as embeddedProperty>
                map.put(${embeddedProperty.column}, ${value}.${embeddedProperty.getterName});
            </#list>
        }
    <#elseif property.toOne == true>

        <#-- this property is an entity, so we only store the foreign key(s) -->
        map.put(${property.column.name}, ${value} == null ? null : ${value}.${property.relatedEntity.id.getterName}());

    <#else>
        <#-- this primitive property corresponds to a single column OR it's a String/Date which we can
             safely pass nulls to, so we can just set it -->
        map.put(${property.column.name}, ${value});

    </#if>
</#macro>
