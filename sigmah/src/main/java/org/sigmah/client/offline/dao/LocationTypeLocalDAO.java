/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.offline.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

/**
 * A LocationTypeDAO implementation for use off-line.
 * 
 * @author jon
 *
 */
public class LocationTypeLocalDAO extends OfflineDAO<LocationType, Integer> {

	@Inject
	protected LocationTypeLocalDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	public List<LocationType> findLocationTypesByCountry(Country c) {
		Query query = em.createNativeQuery(
				"select * from LocationType where countryid = " + c.getId() , LocationType.class);
		return query.getResultList();
	}

	@Override
	public LocationType findById(Integer primaryKey) {
		return em.find(LocationType.class, primaryKey);
	}
}