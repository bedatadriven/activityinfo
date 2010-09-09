package org.sigmah.client.offline.dao;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.domain.UserDatabase;

import com.google.inject.Inject;

public class UserDatabaseLocalDAO extends OfflineDAO <UserDatabase, Integer> implements UserDatabaseDAO {

	private ActivityLocalDAO activityDAO = null;
	private LocationTypeLocalDAO locationTypeDAO = null;
	
	@Inject
	protected UserDatabaseLocalDAO(EntityManager em, ActivityLocalDAO activityDAO) {
		super(em);
		this.activityDAO = activityDAO;
		this.locationTypeDAO = new LocationTypeLocalDAO(em);
	}
	
	@Override
	public List<UserDatabase> queryAllUserDatabasesAlphabetically() {
		Query query = em.createNativeQuery("select * from UserDatabase order by Name", UserDatabase.class);
	
		List<UserDatabase> databases =  query.getResultList();
		
		for (UserDatabase db : databases ) {
			db.setActivities(activityDAO.getActivitiesByDatabaseId(db.getId()));
			Country c = db.getCountry();
			if (c != null) {
				List<LocationType> types = locationTypeDAO.findLocationTypesByCountry(c);
				c.setLocationTypes(new HashSet(types));
			}
		}
		return databases;
	}

	@Override
	public UserDatabase findById(Integer primaryKey) {
		return em.find(UserDatabase.class, primaryKey);
	}
}
