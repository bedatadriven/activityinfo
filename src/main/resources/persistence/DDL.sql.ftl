<#-- @ftlvariable name="entity" type="com.google.gwt.gears.persistence.mapping.EntityMapping" -->

<#macro createtable entity>
<@singleline>
    create table if not exists ${entity.tableName} (
        <@csv>
        <#list entity.properties as property>
            <#list property.columns as column>
                ${column.name}
                ${column.typeName}
                <#if property.id == true>
                    primary key
                    <#if property.autoincrement>
                        autoincrement
                    </#if>
                <#elseif property.unique == true>
                    unique
                </#if>
                 ,
            </#list>
        </#list>
        </@csv>
    )
</@singleline>
</#macro>

