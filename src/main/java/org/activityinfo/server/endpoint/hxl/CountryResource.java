package org.activityinfo.server.endpoint.hxl;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.entity.Country;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
@Api(value="/country", description = "Operations on countries and their geography")
public class CountryResource {
	
	private final Provider<EntityManager> entityManager;
	
	@Inject
	public CountryResource(Provider<EntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@GET
	@Path("{code}")
	@Produces(MediaType.TEXT_HTML)
	public Viewable getPageById(@PathParam("code") String code) {
		return new Viewable("/resource/Country.ftl", getByCode(code));
	}
	
	@GET
	@Path("{id: [0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Gets the country by id")
	public Country getById(
			@ApiParam(value = "ID of the country to be fetched", required = true, defaultValue="1") 
			@PathParam("id") int id) {
		return entityManager.get().find(Country.class, id);
	}
	
	@GET
	@Path("{code: [A-Z]+}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Gets the country by two-letter ISO code")
	public Country getByCode(
			@ApiParam(value = "Code of the country to be fetched", required = true, defaultValue="CD") 
			@PathParam("code") String code) {
		
		return (Country) entityManager.get().createQuery("select c from Country c where c.codeISO = :iso")
				.setParameter("iso", code)
				.getSingleResult();	
	}

}
