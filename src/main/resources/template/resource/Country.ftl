<#include "../page/Scaffolding.ftl">
<@scaffolding title="${name}">

	
	<@content>
	<h1>${name}</h1>
	
	<h2>Administrative Unit Levels</h2>
	<@showLevels children=adminLevels/>
	</@content>
</@scaffolding>

<#macro showLevels children>
	<ul>
	<#list children as child>
	<li><a href="/resources/adminUnitLevel/${child.id?c}">${child.name}</a></li>
	</#list>
	</ul>
</#macro>