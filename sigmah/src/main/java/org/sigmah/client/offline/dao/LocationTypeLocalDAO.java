package org.sigmah.client.offline.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

public class LocationTypeLocalDAO extends OfflineDAO<LocationType, Integer> {

	@Inject
	protected LocationTypeLocalDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	public List<LocationType> findLocationTypesByCountry(Country c) {
		Query query = em.createNativeQuery(
				"select * from LocationType where countryid = " + c.getId() , LocationType.class);
		Log.debug("result size " +query.getResultList().size());
		return query.getResultList();
	}

	@Override
	public LocationType findById(Integer primaryKey) {
		return em.find(LocationType.class, primaryKey);
	}
}