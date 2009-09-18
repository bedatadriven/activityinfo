<?xml version='1.0' encoding="UTF-8" ?>
<WFS_Capabilities
   version="1.0.0"
   updateSequence="0"
   xmlns="http://www.opengis.net/wfs"
   xmlns:ogc="http://www.opengis.net/ogc"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengeospatial.net//wfs/1.0.0/WFS-capabilities.xsd">

<Service>
  <Name>ActivityInfo WFS</Name>
  <Title>ActivityInfo WFS Server</Title>
  <Abstract>
      ActivityInfo WFS provides access to the geographic data in the ActivityInfo database.
  </Abstract>
  <Keywords>
    monitoring
    evaluation
    post-conflict
    humanitarian
  </Keywords>
  <OnlineResource>http://www.activityinfo.org</OnlineResource>
  <Fees>None</Fees>
  <AccessConstraints>None</AccessConstraints>
</Service>
<Capability>
  <Request>
    <GetCapabilities>
      <DCPType>
        <HTTP>
          <Get onlineResource="${getUrl}" />
        </HTTP>
      </DCPType>
      <DCPType>
        <HTTP>
          <Post onlineResource="${postUrl}" />
        </HTTP>
      </DCPType>
    </GetCapabilities>
    <DescribeFeatureType>
      <SchemaDescriptionLanguage>
        <XMLSCHEMA/>
        <SFE_XMLSCHEMA/>
      </SchemaDescriptionLanguage>
      <DCPType>
        <HTTP>
          <Get onlineResource="${getUrl}" />
        </HTTP>
      </DCPType>
      <DCPType>
        <HTTP>
          <Post onlineResource="${postUrl}" />
        </HTTP>
      </DCPType>
    </DescribeFeatureType>
    <GetFeature>
      <ResultFormat>
        <GML2/>
        <GML3/>
      </ResultFormat>
      <DCPType>
        <HTTP>
          <Get onlineResource="${getUrl}" />
        </HTTP>
      </DCPType>
      <DCPType>
        <HTTP>
          <Post onlineResource="${postUrl}" />
        </HTTP>
      </DCPType>
    </GetFeature>
  </Request>
</Capability>

<#list activities as activity>
<FeatureTypeList>
  <Operations>
    <Query/>
  </Operations>
    <FeatureType>
        <Name>activity${activity.id}</Name>
        <Title>${activity.database.name} - ${activity.name}</Title>
        <Abstract>
         <#if activity.database.fullName??>
            ${activity.database.fullName}
            <#else>
            ${activity.database.name}
            </#if>
            ${activity.name}
        </Abstract>
        <SRS>EPSG:4326</SRS>
        <LatLongBoundingBox minx="-180" miny="-90" maxx="180" maxy="90" />
    </FeatureType>
</FeatureTypeList>
</#list>

<ogc:Filter_Capabilities>
  <ogc:Spatial_Capabilities>
    <ogc:Spatial_Operators>

      <ogc:Intersect/>
      <ogc:DWithin/>
      <ogc:BBOX/>
    </ogc:Spatial_Operators>
  </ogc:Spatial_Capabilities>
  <ogc:Scalar_Capabilities>
    <ogc:Logical_Operators />
    <ogc:Comparison_Operators>
      <ogc:Simple_Comparisons />

      <ogc:Like />
      <ogc:Between />
    </ogc:Comparison_Operators>
  </ogc:Scalar_Capabilities>
</ogc:Filter_Capabilities>

</WFS_Capabilities>
