/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;

import com.google.inject.Inject;

/**
 * An AdminDAO implementation for off-line.
 * 
 * @author jon
 *
 */
public class AdminLocalDAO extends OfflineDAO<AdminEntity, Integer> implements AdminDAO {
	

	@Inject
    public AdminLocalDAO(EntityManager em) {
    	super(em);
	}

    @Override
    public List<AdminEntity> findRootEntities(int levelId) {
    	StringBuilder q = new StringBuilder()
    		.append("select * from AdminEntity where AdminLevelId = ")
    		.append(levelId)
    		.append(" order by name");
    	javax.persistence.Query query = em.createNativeQuery(q.toString(), AdminEntity.class);
		return query.getResultList();
    }

    @Override
    public List<AdminEntity> findChildEntities(int levelId, int parentEntityId) {
    	StringBuilder q = new StringBuilder()
    		.append("select * from AdminEntity where AdminLevelId = ")
    		.append(levelId)
    		.append(" and AdminEntityParentId = ")
    		.append(parentEntityId)
    		.append(" order by name");
    	javax.persistence.Query query = em.createNativeQuery(q.toString(), AdminEntity.class);
    	return query.getResultList();
    }

    @Override
    public List<AdminEntity> findBySiteIds(Set<Integer> siteIds) {
    	LocalQuery q = new LocalQuery();
    	q.setWithSiteIds(siteIds);
    	javax.persistence.Query query = em.createNativeQuery(q.toString(), AdminEntity.class);
    	return query.getResultList();
    }
    
    public List<AdminEntity> find(int entityLevelId, int parentEntityId, int activityId) {
    	LocalQuery q = new LocalQuery();
    	if (entityLevelId > -1) {
    		q.level(entityLevelId);
    	}
    	if (parentEntityId > -1) {
    		q.withParentEntityId(parentEntityId);
    	}
    	if (activityId > -1) {
    		q.withSitesOfActivityId(activityId);
    	}
    	javax.persistence.Query query = em.createNativeQuery(q.toString(), AdminEntity.class);
    	return query.getResultList();
    }
    

    public class LocalQuery implements AdminDAO.Query {
    	String query = "select a.AdminEntityId, a.x1, a.x2, a.y1, a.y2, a.Code, a.AdminLevelId, a.Name, a.AdminEntityParentId, a.Soundex from AdminEntity a where ";
    	StringBuilder w = new StringBuilder();
    	
 
    	@Override
		public Query level(int levelId) {
    		if (w.length() > 0) {
        		w.append(" and ");
        	}
        	w.append(" a.AdminLevelId = ").append(levelId);
        	return this;
		}
        
        
		@Override
		public Query withParentEntityId(int parentEntityId) {
			if (w.length() > 0) {
        		w.append(" and ");
        	}
        	w.append(" a.AdminEntityParentId = ").append(parentEntityId);
        	return this;
		}
	

		@Override
		public List<AdminEntity> execute() {
			javax.persistence.Query query = em.createNativeQuery(this.toString(), AdminEntity.class);
	    	return query.getResultList();
		}
        
       
		@Override
		public Query withSitesOfActivityId(int id) {
			if (w.length() > 0) {
        		w.append(" and ");
        	}
        	w.append("a.AdminEntityId in (");
        	w.append("  select");
        	w.append("     entity_.AdminEntityId as y0_ ");
        	w.append("  from");
        	w.append("     AdminEntity entity_ ");
        	w.append("     inner join LocationAdminLink locations5_ on entity_.AdminEntityId=locations5_.AdminEntityId ");
        	w.append("     inner join Location location1_ on locations5_.LocationId=location1_.LocationID ");
        	w.append("     inner join Site site2_ on location1_.LocationID=site2_.LocationId ");
        	w.append("     inner join Activity activity3_ on site2_.ActivityId=activity3_.ActivityId ");
        	w.append("  where activity3_.ActivityId=").append(id);
        	w.append(")");
        	return this;
		}
       
       
        public Query setWithSiteIds(Set<Integer> siteIds) {
        	
        	if (w.length() > 0) {
        		w.append(" and ");
        	}
        	w.append("a.AdminEntityId in (");
        	w.append("  select");
        	w.append("     entity_.AdminEntityId as y0_ ");
        	w.append("  from");
        	w.append("     AdminEntity entity_ ");
        	w.append("     inner join LocationAdminLink locations5_ on entity_.AdminEntityId=locations5_.AdminEntityId ");
        	w.append("     inner join Location location1_ on locations5_.LocationId=location1_.LocationID ");
        	w.append("     inner join Site site2_ on location1_.LocationID=site2_.LocationId ");
        	w.append("  where site2_.siteid in (");
        	append(w, siteIds);
        	w.append("))");
        	return this;
        }
        
        private StringBuilder append(StringBuilder buff, Set l){
        	boolean first = true;
        	for (Object o: l) {
        		if (!first) {
        			buff.append(", ");
        		} else {
        			first = false;
        		}
        		buff.append(o.toString());
        	}
        	return buff;
        }
        
        public String toString() {
        	return query + w.toString();
        }

    };
    

	@Override
	public Query query() {
		return new LocalQuery();
	}

	@Override
	public AdminEntity findById(Integer primaryKey) {
		return em.find(AdminEntity.class, primaryKey);
	}
}
