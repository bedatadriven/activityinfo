package org.activityinfo.server.endpoint.hxl;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.entity.Country;

import com.sun.jersey.api.view.Viewable;

public class CountryResource {

	private Country country;

	public CountryResource(Country country) {
		this.country = country;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable get() {
		return new Viewable("/preview/Country.ftl", country);
	}

}
