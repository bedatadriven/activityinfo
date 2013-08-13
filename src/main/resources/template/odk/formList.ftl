<?xml version="1.0" encoding="UTF-8"?>

<formList>

<#list schema.databases as db>
<#if db.editAllowed>
    <#list db.activities as activity>
    
    <form url="${host}form?id=${activity.id?c}">${db.name?xml} / ${activity.name?xml}</form>
    
    </#list>
</#if>
</#list>

</formList>
