/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.dao.ActivityDAO;
import org.sigmah.shared.domain.Activity;

import com.google.inject.Inject;

/**
 * An implementation of ActivityDAO for use off-line. 
 * 
 * @author jon
 *
 */
public class ActivityLocalDAO extends OfflineDAO<Activity, Integer> implements
		ActivityDAO {

	LocationTypeLocalDAO locationTypeDAO;
	
	@Inject
	protected ActivityLocalDAO(EntityManager em, LocationTypeLocalDAO locationTypeDAO) {
		super(em);
		this.locationTypeDAO = locationTypeDAO;
	}

	@Override
	public void persist(Activity entity) {
		em.persist(entity);
	}

	@Override
	public Activity findById(Integer primaryKey) {
		return em.find(Activity.class, primaryKey);
	}

	@Override
	public Integer queryMaxSortOrder(int databaseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Activity> getActivitiesByDatabaseId(int databaseId) {
		StringBuilder  buff= new StringBuilder();
		buff.append(" select * from Activity where databaseId = ").append(databaseId).append(" order by name");
		Query query = em.createNativeQuery(buff.toString(), Activity.class);
		/*
		List <Activity> l = query.getResultList();
		for (Activity a: l) {
			LocationType t = a.getLocationType();
			//TODO force load until lazy loading gets fixed
			t = locationTypeDAO.findById(t.getId());
			Log.debug(t.getId() + " " + t.getName() + " ===>" + t.getBoundAdminLevel());
			a.setLocationType(t);
		}
		return new HashSet<Activity>(l);
		*/
		return new HashSet<Activity>(query.getResultList());
	}

}
