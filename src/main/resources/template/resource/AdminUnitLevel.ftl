<#include "../page/Scaffolding.ftl">
<@scaffolding title="${name}">

	<@content>
	<h1>${name}</h1>
	
	<p>Administrative Unit Level in ${country.name}</p>
	
	<#if parent?has_content >
	<p>Subdivision of <a href="/resources/adminUnitLevel/${parent.id?c}">${parent.name}</a></p>
	</#if>
	
	<#if childLevels?has_content >
	<h2>Child Levels</h2>
	<ul>
	<#list childLevels as child>
	<li><a href="/resources/adminUnitLevel/${child.id?c}">${child.name}</a></li>
	</#list>
	</ul>
	</#if>
	
	<h2>Units</h2>
	<ul>
	<#list entities?sort_by("name") as entity >
		<li><a href="/resources/adminUnit/${entity.id?c}">${entity.name}</a></li>
	</#list>
	</ul>
	</@content>
</@scaffolding>
