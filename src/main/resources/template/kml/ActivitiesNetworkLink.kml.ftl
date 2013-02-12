<?xml version="1.0" encoding="UTF-8"?><kml xmlns="http://earth.google.com/kml/2.2">
<Document>
<#list schema.databases as db>
<Folder>
	<name>${db.name}</name>
	<open>1</open>
	<#list db.activities as activity>
		<NetworkLink>
		<name>${activity.name}</name>
		<refreshVisibility>0</refreshVisibility>
		<flyToView>1</flyToView>
		<Link>
			<href>${baseURL}${activity.id?c}</href>
		</Link>
		</NetworkLink>
	</#list>
</Folder>
</#list>
</Document>
</kml>