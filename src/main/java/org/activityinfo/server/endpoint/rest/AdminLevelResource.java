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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
import org.activityinfo.server.util.blob.BlobNotFoundException;
import org.activityinfo.server.util.blob.BlobService;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.common.io.ByteStreams;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

public class AdminLevelResource {

    private static final Logger LOGGER = Logger
        .getLogger(AdminLevelResource.class.getName());

    private Provider<EntityManager> entityManager;
    private AdminLevel level;
    private BlobService blobService;

    // TODO: create list of geoadmins per country
    private static final int SUPER_USER_ID = 3;

    public AdminLevelResource(Provider<EntityManager> entityManager,
        BlobService blobService, AdminLevel level) {
        super();
        this.entityManager = entityManager;
        this.blobService = blobService;
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@InjectParam AuthenticatedUser user,
        UpdatedAdminLevel updatedLevel) {

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
                AdminEntity entity;
                if (updatedEntity.getId() != null && updatedEntity.getId() > 0) {
                    // update existing entity
                    entity = em.find(AdminEntity.class, updatedEntity.getId());
                    if (updatedEntity.isDeleted()) {
                        entity.setDeleted(true);
                    } else {
                        entity.setName(updatedEntity.getName());
                        entity.setCode(updatedEntity.getCode());
                        entity.setBounds(updatedEntity.getBounds());
                    }
                } else {
                    // create new entity
                    entity = new AdminEntity();
                    entity.setLevel(level);
                    entity.setName(updatedEntity.getName());
                    entity.setCode(updatedEntity.getCode());
                    entity.setBounds(updatedEntity.getBounds());
                    if (updatedEntity.getParentId() != null) {
                        entity.setParent(em.getReference(AdminEntity.class, updatedEntity.getParentId()));
                    }
                    entity.setDeleted(updatedEntity.isDeleted());
                    em.persist(entity);
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

    @GET
    @Path("/geometry/wkb")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getWkbGeometry() throws IOException {
        try {
            LOGGER.info("Get");

            return Response
                .ok(ByteStreams.toByteArray(blobService.get(wkbKey())))
                .build();

        } catch (BlobNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @PUT
    @Path("/geometry/wkb")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void putWkbGeometry(@InjectParam AuthenticatedUser user, byte[] wkb)
        throws IOException {
        assertAuthorized(user);

        blobService.put(wkbKey(), ByteStreams.newInputStreamSupplier(wkb));
    }

    @POST
    @Path("/childLevels")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewLevel(@InjectParam AuthenticatedUser user,
        NewAdminLevel newLevel) {

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

    private String wkbKey() {
        return "/adminGeometry/" + level.getId() + ".wkb.gz";
    }
}
