package org.activityinfo.server.endpoint.hxl;

import javax.persistence.EntityManager;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path("/resources")
public class HxlResources {

	private Provider<EntityManager> entityManager;

	@Inject
	public HxlResources(Provider<EntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
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
	
	@Path("/adminUnitLevel/{id}")
	public AdminUnitLevelResource getAdminUnitLevel(@PathParam("id") int id) {
		return new AdminUnitLevelResource( entityManager.get().find(AdminLevel.class, id) );
	}
	
}
