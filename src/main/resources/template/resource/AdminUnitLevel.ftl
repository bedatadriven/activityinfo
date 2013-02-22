<#--
 #%L
 ActivityInfo Server
 %%
 Copyright (C) 2009 - 2013 UNICEF
 %%
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the 
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public 
 License along with this program.  If not, see
 <http://www.gnu.org/licenses/gpl-3.0.html>.
 #L%
-->
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
