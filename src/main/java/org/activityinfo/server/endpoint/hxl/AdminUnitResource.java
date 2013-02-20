package org.activityinfo.server.endpoint.hxl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.endpoint.refine.AdminEntityPreview;

import com.sun.jersey.api.view.Viewable;


@Path("/adminUnit")
public class AdminUnitResource {

	private AdminEntity unit;
	
	public AdminUnitResource(AdminEntity unit) {
		super();
		this.unit = unit;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable get() {
		return new Viewable("/resource/AdminUnit.ftl", new AdminEntityPreview(unit));
	}
	
	
}
