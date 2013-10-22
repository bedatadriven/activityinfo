package org.activityinfo.server.endpoint.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class SitesResources {

    private final DispatcherSync dispatcher;

    public SitesResources(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String query(
        @QueryParam("activity") List<Integer> activityIds, 
        @QueryParam("database") List<Integer> databaseIds,
        @QueryParam("format") String format) 
        throws IOException {

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity, activityIds);
        filter.addRestriction(DimensionType.Database, databaseIds);
        
        List<SiteDTO> sites = dispatcher.execute(new GetSites(filter)).getData();

        StringWriter writer = new StringWriter();
        JsonGenerator json = Jackson.createJsonFactory(writer);
        
        writeJson(sites, json);
       
        return writer.toString();
    }

    @GET
    @Path("/points")
    public Response queryPoints(
        @QueryParam("activity") List<Integer> activityIds,
        @QueryParam("database") List<Integer> databaseIds,
        @QueryParam("callback") String callback) throws JsonGenerationException, IOException {

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity, activityIds);
        filter.addRestriction(DimensionType.Database, databaseIds);
        
        List<SiteDTO> sites = dispatcher.execute(new GetSites(filter)).getData();
        
        StringWriter writer = new StringWriter();
        JsonGenerator json = Jackson.createJsonFactory(writer);
        writeGeoJson(sites, json);
       
        if(Strings.isNullOrEmpty(callback)) {
            return Response.ok(writer.toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            return Response
                .ok(callback + "(" + writer.toString() + ");")
                .type("application/javascript; charset=UTF-8")
                .build();
        }
    }

    
    private void writeJson(List<SiteDTO> sites, JsonGenerator json)
        throws IOException, JsonGenerationException {
        json.writeStartArray();

        for (SiteDTO site : sites) {
            json.writeStartObject();
            json.writeNumberField("id", site.getId());
            json.writeNumberField("activity", site.getActivityId());
            
            // write the location as a separate object
            json.writeObjectFieldStart("location");
            json.writeNumberField("id", site.getLocationId());
            json.writeStringField("name", site.getLocationName());

            if (site.hasLatLong()) {
                json.writeFieldName("latitude");
                json.writeNumber(site.getLatitude());
                json.writeFieldName("longitude");
                json.writeNumber(site.getLongitude());
            }
            json.writeEndObject();

            json.writeObjectFieldStart("partner");
            json.writeNumberField("id", site.getPartnerId());
            json.writeStringField("name", site.getPartnerName());
            json.writeEndObject();

            // write attributes as a series of ids
            Set<Integer> attributes = getAttributeIds(site);
            if(!attributes.isEmpty()) {
                json.writeFieldName("attributes");
                json.writeStartArray();
                for(Integer attributeId : attributes) {
                    json.writeNumber(attributeId);
                }
                json.writeEndArray();
            }
            
            // write indicators
            Set<Integer> indicatorIds = getIndicatorIds(site);
            if(!indicatorIds.isEmpty()) {
                json.writeObjectFieldStart("indicatorValues");
                for(Integer indicatorId : indicatorIds) {
                    json.writeNumberField(Integer.toString(indicatorId), site.getIndicatorValue(indicatorId));
                }
                json.writeEndObject();
            }

            // comments
            if (!Strings.isNullOrEmpty(site.getComments())) {
                json.writeFieldName("comments");
                json.writeString(site.getComments());
            }

            json.writeEndObject();
        }
        json.writeEndArray();
        json.close();
    }
    
    private void writeGeoJson(List<SiteDTO> sites, JsonGenerator json) throws JsonGenerationException, IOException {
        json.writeStartArray();

        for (SiteDTO site : sites) {
            if(site.hasLatLong()) {
                json.writeStartObject();
                json.writeStringField("type", "Feature");
                json.writeNumberField("id", site.getId());
                
                // write out the properties object
                json.writeObjectFieldStart("properties");
                json.writeStringField("locationName", site.getLocationName());
                json.writeStringField("partnerName", site.getPartnerName());         
                if(!Strings.isNullOrEmpty(site.getComments())) {
                    json.writeStringField("comments", site.getComments());
                }
                json.writeEndObject();
    
                // write out the geometry object
                json.writeObjectFieldStart("geometry");
                json.writeStringField("type", "Point");
                json.writeArrayFieldStart("coordinates");
                json.writeNumber(site.getX());
                json.writeNumber(site.getY());
                json.writeEndArray();
                json.writeEndObject();

                json.writeEndObject();
            }
        }
        json.writeEndArray();
        json.close();   
    }

    private Set<Integer> getIndicatorIds(SiteDTO site) {
        Set<Integer> ids = Sets.newHashSet();
        for(String propertyName : site.getPropertyNames()) {
            if(propertyName.startsWith(IndicatorDTO.PROPERTY_PREFIX) && 
                site.get(propertyName) != null) {
                ids.add(IndicatorDTO.indicatorIdForPropertyName(propertyName));
            }
        }
        return ids;
    }
    
    private Set<Integer> getAttributeIds(SiteDTO site) {
        Set<Integer> ids = Sets.newHashSet();
        for (String propertyName : site.getPropertyNames()) {
            if (propertyName.startsWith(AttributeDTO.PROPERTY_PREFIX)) {
                int attributeId = AttributeDTO
                    .idForPropertyName(propertyName);
                boolean value = (Boolean) site.get(propertyName, false);
                if (value) {
                    ids.add(attributeId);
                }
            }
        }
        return ids;
    }
}
