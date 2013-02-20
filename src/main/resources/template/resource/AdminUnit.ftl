<#include "../page/Scaffolding.ftl">
<@scaffolding title="${entity.name}">

	<@content>
	<h1>${entity.name}</h1>
	
	<p><a href="/resources/adminUnitLevel/${entity.level.id?c}">${entity.level.name}</a> in <a href="/resources/country/${entity.level.country.codeISO}">${entity.level.country.name}</a></p>
	<#if parents?has_content>
	<h2>Parents</h2>
	<ul>
		<#list parents as parent>
		<li>${parent.level.name}: <a href="/resources/adminUnit/${parent.id?c}">${parent.name}</a></li>
		</#list>
	</ul>
	</#if>
	<#if entity.level.childLevels?has_content>
		<h2>Child units</h2>
		<#list entity.level.childLevels as childLevel>
		<h3>${childLevel.name}</h3>
		<ul>
			<#list childLevel.entities?sort_by("name") as childEntity>
			<#if childEntity.parent.id == entity.id>
				<li><a href="/resources/adminUnit/${childEntity.id?c}">${childEntity.name}</a></li>
			</#if>
			</#list>
		</ul> 
		</#list>
	</#if>
	</@content>
</@scaffolding>
