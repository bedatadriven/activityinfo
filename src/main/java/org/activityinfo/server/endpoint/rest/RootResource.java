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

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.DTOViews;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.codehaus.jackson.map.annotate.JsonView;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path("/resources")
public class RootResource {

    private Provider<EntityManager> entityManager;
    private DispatcherSync dispatcher;
    private DeploymentConfiguration config;

    @Inject
    public RootResource(Provider<EntityManager> entityManager, DispatcherSync dispatcher,
        DeploymentConfiguration config) {
        super();
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
        this.config = config;
    }

    @Path("/adminEntity/{id}")
    public AdminEntityResource getAdminEntity(@PathParam("id") int id) {
        return new AdminEntityResource(entityManager.get().find(
            AdminEntity.class, id));
    }

    @GET
    @Path("/countries")
    @JsonView(DTOViews.List.class)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountryDTO> getCountries() {
        return dispatcher.execute(new GetCountries()).getData();
    }
    

    @Path("/country/{id: [0-9]+}")
    public CountryResource getCountryById(@PathParam("id") int id) {
        Country result = (Country)entityManager.get().find(Country.class, id);
        if(result == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return new CountryResource(result);
    }

    @Path("/country/{code: [A-Z]+}")
    public CountryResource getCountryByCode(@PathParam("code") String code) {

        List<Country> results = entityManager.get()
            .createQuery("select c from Country c where c.codeISO = :iso")
            .setParameter("iso", code)
            .getResultList();
        
        if(results.isEmpty()) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        
        return new CountryResource(results.get(0));
    }

    @GET
    @Path("/databases")
    @JsonView(DTOViews.List.class)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDatabaseDTO> getDatabases() {
        return dispatcher.execute(new GetSchema()).getDatabases();
    }

    @GET
    @Path("/database/{id}/schema")
    @JsonView(DTOViews.Schema.class)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDatabaseDTO getDatabaseSchema(@PathParam("id") int id) {
        UserDatabaseDTO db = dispatcher.execute(new GetSchema())
            .getDatabaseById(id);
        return db;
    }

    @Path("/adminLevel/{id}")
    public AdminLevelResource getAdminLevel(@PathParam("id") int id) {
        return new AdminLevelResource(entityManager, entityManager.get().find(
            AdminLevel.class, id));
    }
    
    @Path("/sites")
    public SitesResources getSites() {
        return new SitesResources(dispatcher);
    }  

    @Path("/geocode")
    public GeocodingResource getGeocoder() {
        return new GeocodingResource();
    }
    
    @Path("/locations")
    public LocationsResource getLocations() {
        return new LocationsResource();
    }
}
