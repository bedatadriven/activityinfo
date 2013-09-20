package org.activityinfo.server.endpoint.rest;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.AdminLevelVersion;
import org.activityinfo.server.database.hibernate.entity.LocationType;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.rest.model.NewAdminEntity;
import org.activityinfo.server.endpoint.rest.model.NewAdminLevel;
import org.activityinfo.server.endpoint.rest.model.UpdatedAdminEntity;
import org.activityinfo.server.endpoint.rest.model.UpdatedAdminLevel;
import org.activityinfo.server.endpoint.rest.model.VersionMetadata;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.hibernate.Session;

import com.bedatadriven.geojson.GeometrySerializer;
import com.google.common.base.Charsets;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

public class AdminLevelResource {

    private static final Logger LOGGER = Logger
        .getLogger(AdminLevelResource.class.getName());

    private Provider<EntityManager> entityManager;
    private AdminLevel level;
    

    // TODO: create list of geoadmins per country
    private static final int SUPER_USER_ID = 3;

    public AdminLevelResource(Provider<EntityManager> entityManager,
        AdminLevel level) {
        super();
        this.entityManager = entityManager;
        this.level = level;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable get() {
        return new Viewable("/resource/AdminLevel.ftl", level);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AdminLevel getJson() {
        return level;
    }

    @DELETE
    public Response deleteLevel(@InjectParam AuthenticatedUser user) {
        assertAuthorized(user);
        
        EntityManager em = entityManager.get();
        em.getTransaction().begin();
        
        AdminLevel level = entityManager.get().merge(this.level);
        level.setDeleted(true);
        
        em.getTransaction().commit();
      
        return Response.ok().build();
    }
    
    private void assertAuthorized(AuthenticatedUser user) {
        if (user.getId() != SUPER_USER_ID) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
    }

    @GET
    @Path("/entities")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdminEntity> getEntities(@InjectParam EntityManager em) {
        return em.createQuery("select e  from AdminEntity e where e.deleted = false and e.level = :level")
            .setParameter("level", level)
            .getResultList();
    }
    
    
    @GET
    @Path("/entities/polylines")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntityPolylines(@InjectParam EntityManager em) throws JsonGenerationException, IOException {
        
        List<AdminEntity> entities = em.createQuery("select e  from AdminEntity e where e.deleted = false and e.level = :level")
            .setParameter("level", level)
            .getResultList();
        
        GoogleMapsWriter writer = new GoogleMapsWriter();
        return writer.write(entities);
    }
    
    @GET
    @Path("/entities/features")
    public Response getFeatures(@InjectParam EntityManager em) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(
            baos, Charsets.UTF_8);

        List<AdminEntity> entities = em
            .createQuery("select e  from AdminEntity e where e.deleted = false and e.level = :level")
            .setParameter("level", level)
            .getResultList();

        JsonFactory jfactory = new JsonFactory();
        JsonGenerator json = jfactory.createJsonGenerator(writer);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        json.setPrettyPrinter(prettyPrinter);
        json.writeStartObject();
        json.writeStringField("type", "FeatureCollection");
        json.writeFieldName("features");
        json.writeStartArray();
        GeometrySerializer geometrySerializer = new GeometrySerializer();

        for (AdminEntity entity : entities) {
        	if(entity.getGeometry() != null) {
	            json.writeStartObject();
	            json.writeStringField("type", "Feature");
	            json.writeNumberField("id", entity.getId());
	            json.writeObjectFieldStart("properties");
	            json.writeStringField("name", entity.getName());
	            if(entity.getParentId() != null) {
	            	json.writeNumberField("parentId", entity.getParentId());
	            }
	            json.writeEndObject();
	
	            json.writeFieldName("geometry");
	            geometrySerializer.writeGeometry(json, entity.getGeometry());
	            json.writeEndObject();
        	}
        }

        json.writeEndArray();
        json.writeEndObject();
        json.close();

        return Response.ok()
            .entity(baos.toByteArray())
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    @GET
    @Path("/tiles/{z}/{x}/{y}.png")
    public Response tile(@InjectParam Session session,
        @PathParam("z") int zoom, @PathParam("x") int x, @PathParam("y") int y) throws IOException {

        AdminTileRenderer renderer = new AdminTileRenderer(session, level);
        byte[] image = renderer.render(zoom, x, y);

        return Response.ok(image).type("image/png").tag("version=" + level.getVersion()).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@InjectParam AuthenticatedUser user,
        UpdatedAdminLevel updatedLevel) throws ParseException {

        assertAuthorized(user);

        EntityManager em = entityManager.get();
        em.getTransaction().begin();
        
        AdminLevel level = entityManager.get().merge(this.level);

        level.setName(updatedLevel.getName());

        for (LocationType boundLocationType : level.getBoundLocationTypes()) {
            boundLocationType.setName(updatedLevel.getName());
        }

        if (updatedLevel.getEntities() != null) {
            for (UpdatedAdminEntity updatedEntity : updatedLevel.getEntities()) {

                // check geometry
                if (updatedEntity.getGeometry() != null && !isValid(updatedEntity.getGeometry())) {
                    throw new WebApplicationException(
                        Response
                        .status(Status.BAD_REQUEST)         
                            .entity("Geometry must be Polygon or MultiPolygon")
                            .build());
                }

                if(updatedEntity.isDeleted()) {
                    // mark the entity as deleted. we can't remove it from
                    // the database because we may have locations which refer to it
                    // on distant clients
                    em.find(AdminEntity.class, updatedEntity.getId())
                        .setDeleted(true);
   
                } else if(updatedEntity.isNew()) {
                    // create new entity
                    AdminEntity entity = new AdminEntity();
                    entity.setLevel(level);
                    if (updatedEntity.getParentId() != null) {
                        entity.setParent(em.getReference(AdminEntity.class, updatedEntity.getParentId()));
                    }
                    entity.setName(updatedEntity.getName());
                    entity.setCode(updatedEntity.getCode());
                    entity.setBounds(updatedEntity.getBounds());
                    entity.setGeometry(updatedEntity.getGeometry());
                    em.persist(entity);
                    
                } else {
                    // update existing entity
                    // TODO: bound locations that share this name?
                    AdminEntity entity = em.find(AdminEntity.class, updatedEntity.getId());
                    entity.setName(updatedEntity.getName());
                    entity.setCode(updatedEntity.getCode());
                    entity.setBounds(updatedEntity.getBounds());
                    entity.setGeometry(updatedEntity.getGeometry());
                }
            }
        }
        
        int newVersion = level.getVersion() + 1;
        level.setVersion(newVersion);
        
        AdminLevelVersion version = new AdminLevelVersion();
        version.setLevel(level);
        version.setVersion(newVersion);
        version.setUser(em.getReference(User.class, user.getId()));
        version.setTimeCreated(new Date().getTime());

        VersionMetadata metadata = updatedLevel.getVersionMetadata();
        if (metadata != null) {
            version.setSourceUrl(metadata.getSourceUrl());
            version.setSourceFilename(metadata.getSourceFilename());
            version.setSourceHash(metadata.getSourceMD5());
            version.setMessage(metadata.getMessage());
            version.setSourceMetadata(metadata.getSourceMetadata());
        }

        em.persist(version);

        em.getTransaction().commit();

        return Response.ok().build();
    }

    private boolean isValid(Geometry geometry) {
        return geometry instanceof Polygon || geometry instanceof MultiPolygon;
    }

    @POST
    @Path("/childLevels")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewLevel(@InjectParam AuthenticatedUser user,
        NewAdminLevel newLevel) throws ParseException {

        assertAuthorized(user);

        EntityManager em = entityManager.get();
        em.getTransaction().begin();
        em.setFlushMode(FlushModeType.COMMIT);

        AdminLevel child = new AdminLevel();
        child.setCountry(level.getCountry());
        child.setName(newLevel.getName());
        child.setParent(level);
        em.persist(child);

        for (NewAdminEntity entity : newLevel.getEntities()) {
            AdminEntity childEntity = new AdminEntity();
            childEntity.setName(entity.getName());
            childEntity.setLevel(child);
            childEntity.setCode(entity.getCode());
            childEntity.setBounds(entity.getBounds());
            childEntity.setParent(em.getReference(AdminEntity.class,
                entity.getParentId()));
            childEntity.setGeometry(entity.getGeometry());
            child.getEntities().add(childEntity);
            em.persist(childEntity);
        }

        // create bound location type
        LocationType boundType = new LocationType();
        boundType.setBoundAdminLevel(child);
        boundType.setCountry(level.getCountry());
        boundType.setName(child.getName());
        em.persist(boundType);

        em.getTransaction().commit();

        return Response.ok().build();
    }
}
