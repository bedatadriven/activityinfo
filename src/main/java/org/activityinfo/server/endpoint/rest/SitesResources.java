package org.activityinfo.server.endpoint.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class SitesResources {

    private final DispatcherSync dispatcher;

    public SitesResources(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String query(@QueryParam("activity") List<Integer> activityIds)
        throws IOException {

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity, activityIds);
        List<SiteDTO> sites = dispatcher.execute(new GetSites(filter))
            .getData();


        JsonFactory jfactory = new JsonFactory();

        StringWriter writer = new StringWriter();
        JsonGenerator json = jfactory.createJsonGenerator(writer);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        json.setPrettyPrinter(prettyPrinter);
        json.writeStartArray();

        for (SiteDTO site : sites) {
            json.writeStartObject();
            json.writeFieldName("id");
            json.writeNumber(site.getId());

            // write the location as a separate object
            json.writeFieldName("location");
            json.writeStartObject();
            json.writeFieldName("id");
            json.writeNumber(site.getLocationId());
            json.writeFieldName("name");
            json.writeString(site.getLocationName());

            if (site.hasLatLong()) {
                json.writeFieldName("latitude");
                json.writeNumber(site.getLatitude());
                json.writeFieldName("longitude");
                json.writeNumber(site.getLongitude());
            }
            json.writeEndObject();

            json.writeFieldName("partner");
            json.writeStartObject();
            json.writeFieldName("id");
            json.writeNumber(site.getPartnerId());
            json.writeFieldName("name");
            json.writeString(site.getPartnerName());
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

            // comments
            if (!Strings.isNullOrEmpty(site.getComments())) {
                json.writeFieldName("comments");
                json.writeString(site.getComments());
            }

            json.writeEndObject();
        }
        json.writeEndArray();
        json.close();

        return writer.toString();
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
