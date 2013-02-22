package org.activityinfo.server.endpoint.hxl;

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

import javax.management.ObjectName;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.DTOViews;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.codehaus.jackson.map.annotate.JsonView;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path("/resources")
public class HxlResources {

	private Provider<EntityManager> entityManager;
	private DispatcherSync dispatcher;

	@Inject
	public HxlResources(Provider<EntityManager> entityManager, DispatcherSync dispatcher) {
		super();
		this.entityManager = entityManager;
		this.dispatcher = dispatcher;
	}
	
	@Path("/adminUnit/{id}")
	public AdminUnitResource getAdminUnit(@PathParam("id") int id) {
		return new AdminUnitResource( entityManager.get().find(AdminEntity.class, id) );
	}
	
//	@Path("/country/{iso}")
//	public CountryResource getCountry(@PathParam("iso") String isoCode) {
//		Country country = (Country) entityManager.get().createQuery("select c from Country c where c.codeISO = :iso")
//			.setParameter("iso", isoCode)
//			.getSingleResult();
//		return new CountryResource( country );
//	}
	
	@GET
	@Path("/countries")
	@JsonView(DTOViews.List.class)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CountryDTO> getCountries() {
        return dispatcher.execute(new GetCountries()).getData();
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
		UserDatabaseDTO db = dispatcher.execute(new GetSchema()).getDatabaseById(id);
		return db;		
	}
	
	@Path("/adminUnitLevel/{id}")
	public AdminUnitLevelResource getAdminUnitLevel(@PathParam("id") int id) {
		return new AdminUnitLevelResource( entityManager.get().find(AdminLevel.class, id) );
	}
	
}
