<!DOCTYPE html>
<html>
<head>
  <title>${name}</title>
  <meta http-equiv = "Content-Type" content = "text/html; charset=UTF-8">
</head>
<body>
	<h1>${name}</h1>
	
	<h2>Administrative Unit Levels</h2>
	<@showLevels children=adminLevels/>
	
</body>
</html>

<#macro showLevels children>
	<ul>
	<#list children as child>
	<li><a href="/resources/adminUnitLevel/${child.id?c}">${child.name}</a></li>
	</#list>
	</ul>
</#macro>