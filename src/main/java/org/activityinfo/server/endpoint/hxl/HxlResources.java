package org.activityinfo.server.endpoint.hxl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.dto.CountryDTO;

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
	
	@Path("/country/{iso}")
	public CountryResource getCountry(@PathParam("iso") String isoCode) {
		Country country = (Country) entityManager.get().createQuery("select c from Country c where c.codeISO = :iso")
			.setParameter("iso", isoCode)
			.getSingleResult();
		return new CountryResource( country );
	}
	
	@GET
	@Path("/countries")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CountryDTO> getCountries() {
        return dispatcher.execute(new GetCountries()).getData();
	}
	
	@Path("/adminUnitLevel/{id}")
	public AdminUnitLevelResource getAdminUnitLevel(@PathParam("id") int id) {
		return new AdminUnitLevelResource( entityManager.get().find(AdminLevel.class, id) );
	}
	
}
