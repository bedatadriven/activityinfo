<?xml version="1.0" encoding="UTF-8"?><kml xmlns="http://earth.google.com/kml/2.2">
<Document>
<#list links as activity>
<NetworkLink>
<name>${activity.name}</name>
<refreshVisibility>0</refreshVisibility>
<flyToView>1</flyToView>
<Link>
<href>${activity.href}</href>
</Link>
</NetworkLink>
</#list>
</Document>
</kml>