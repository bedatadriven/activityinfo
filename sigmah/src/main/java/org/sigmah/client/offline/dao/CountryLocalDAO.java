package org.sigmah.client.offline.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.domain.Country;

import com.google.inject.Inject;

public class CountryLocalDAO extends OfflineDAO<Country, Integer> implements
		CountryDAO {

	private LocationTypeLocalDAO locationTypeLocalDAO;
	
	@Inject
	protected CountryLocalDAO(EntityManager em, LocationTypeLocalDAO locationTypeLocalDAO) {
		super(em);
		// TODO Auto-generated constructor stub
		this.locationTypeLocalDAO = locationTypeLocalDAO;
	}

	@Override
	public List<Country> queryAllCountriesAlphabetically() {
		Query query = em.createNativeQuery(
				"select * from Country order by name", Country.class);
		return query.getResultList();
	}

	@Override
	public Country findById(Integer primaryKey) {
		return em.find(Country.class, primaryKey);
	}

}
