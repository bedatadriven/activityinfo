package org.activityinfo.server.endpoint.hxl;

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
