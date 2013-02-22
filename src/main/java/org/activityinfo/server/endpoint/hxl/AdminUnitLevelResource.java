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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.sun.jersey.api.view.Viewable;

public class AdminUnitLevelResource {

	private AdminLevel level;

	public AdminUnitLevelResource(AdminLevel level) {
		super();
		this.level = level;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable get() {
		return new Viewable("/resource/AdminUnitLevel.ftl", level);
	}
	
	@GET
	@Path("/units")
	@Produces(MediaType.APPLICATION_JSON)
	public ObjectNode getUnits(@Context UriInfo uri) {
		ArrayNode array = JsonNodeFactory.instance.arrayNode();
		for(AdminEntity entity : level.getEntities()) {
			ObjectNode unit = JsonNodeFactory.instance.objectNode();
			unit.put("id", uri.getBaseUriBuilder()
					.path(HxlResources.class)
					.path("adminUnit")
					.path(Integer.toString(entity.getId())).build().toString());
			unit.put("name", entity.getName());
			array.add(unit);
		}
		ObjectNode wrapper = JsonNodeFactory.instance.objectNode();
		wrapper.put("units", array);
		return wrapper;
	}	
}
