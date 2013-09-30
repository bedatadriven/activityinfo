package org.activityinfo.server.endpoint.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.Location;
import org.activityinfo.server.database.hibernate.entity.LocationType;
import org.activityinfo.server.endpoint.rest.model.NewLocation;
import org.codehaus.jackson.JsonGenerator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.core.InjectParam;

public class LocationsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object query(@InjectParam EntityManager em, @QueryParam("type") int typeId) 
        throws IOException {

        List<Location> locations = em.createQuery("select distinct loc from" +
        		" Location loc left join fetch loc.adminEntities where loc.locationType.id = :typeId")
            .setParameter("typeId", typeId)
            .getResultList();
        

        StringWriter writer = new StringWriter();
        JsonGenerator json = Jackson.createJsonFactory(writer);

        json.writeStartArray();
        for(Location location : locations) {
            json.writeStartObject();
            json.writeNumberField("id", location.getId());
            json.writeStringField("name", location.getName());
            if(location.getX() != null && location.getY() != null) {
                json.writeNumberField("latitude", location.getY());
                json.writeNumberField("longitude", location.getX());
            }
            json.writeObjectFieldStart("adminEntities");
            for(AdminEntity entity : location.getAdminEntities()) {
                json.writeFieldName(Integer.toString(entity.getLevel().getId()));
                json.writeStartObject();
                json.writeNumberField("id", entity.getId());
                json.writeStringField("name", entity.getName());
                json.writeEndObject();
            }
            json.writeEndObject();
            json.writeEndObject();
        }
        json.writeEndArray();
        json.close();
        
        return Response.ok(writer.toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    @POST
    @Path("/{typeId}")
    public Response postNewLocations(@InjectParam EntityManager entityManager, 
        @PathParam("typeId") int locationTypeId, List<NewLocation> locations) {
        
        KeyGenerator generator = new KeyGenerator();
        
        entityManager.getTransaction().begin();

        LocationType locationType = entityManager.getReference(LocationType.class, locationTypeId);
        for(NewLocation newLocation : locations) {
            
            Location location = new Location();
            location.setId(generator.generateInt());
            
            System.out.println(location.getId());
            
            location.setName(newLocation.getName());
            location.setLocationType(locationType);
            location.setX(newLocation.getLongitude());
            location.setY(newLocation.getLatitude());
            location.setTimeEdited(new Date());
            location.setAdminEntities(new HashSet<AdminEntity>());
            for(int entityId : newLocation.getAdminEntityIds()) {
                location.getAdminEntities().add(entityManager.getReference(AdminEntity.class, entityId));
            }
            
            entityManager.persist(location);    
        }
        
        entityManager.getTransaction().commit();
        
        return Response.ok().build();
    }
}
    