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

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.database.hibernate.entity.LocationType;
import org.activityinfo.server.endpoint.rest.model.NewAdminEntity;
import org.activityinfo.server.endpoint.rest.model.NewAdminLevel;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private Country country;

    public CountryResource(Country country) {
        this.country = country;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getPage() {
        return new Viewable("/resource/Country.ftl", country);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Country getJson() {
        return country;
    }

    
    @GET
    @Path("adminLevels")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AdminLevel> getAdminLevels() {
        return country.getAdminLevels();
    }

    @POST
    @Path("adminLevels")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewLevel(
        @InjectParam AuthenticatedUser user,
        @InjectParam EntityManager em,
        NewAdminLevel newLevel) {

        // assertAuthorized(user);

        em.getTransaction().begin();
        em.setFlushMode(FlushModeType.COMMIT);

        AdminLevel level = new AdminLevel();
        level.setCountry(country);
        level.setName(newLevel.getName());
        level.setVersion(1);
        em.persist(level);

        for (NewAdminEntity newEntity : newLevel.getEntities()) {
            AdminEntity entity = new AdminEntity();
            entity.setName(newEntity.getName());
            entity.setLevel(level);
            entity.setCode(newEntity.getCode());
            entity.setBounds(newEntity.getBounds());
            level.getEntities().add(entity);
            em.persist(entity);
        }

        // create bound location type
        LocationType boundType = new LocationType();
        boundType.setBoundAdminLevel(level);
        boundType.setCountry(level.getCountry());
        boundType.setName(level.getName());
        em.persist(boundType);

        em.getTransaction().commit();

        return Response.ok().build();
    }

}
