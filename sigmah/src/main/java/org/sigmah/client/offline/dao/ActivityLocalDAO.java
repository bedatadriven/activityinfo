package org.sigmah.client.offline.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.dao.ActivityDAO;
import org.sigmah.shared.domain.Activity;

import com.google.inject.Inject;

public class ActivityLocalDAO extends OfflineDAO<Activity, Integer> implements
		ActivityDAO {

	@Inject
	protected ActivityLocalDAO(EntityManager em) {
		super(em);
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
		return new HashSet<Activity>(query.getResultList());
	}

}
