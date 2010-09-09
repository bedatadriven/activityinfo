/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Bertram
 */
public class AdminHibernateDAO extends GenericDAO<AdminEntity, Integer> implements AdminDAO {

    private final EntityManager em;

    @Inject
    public AdminHibernateDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public List<AdminEntity> findRootEntities(int levelId) {
        return em.createQuery("select entity from AdminEntity entity where entity.level.id = :levelId order by entity.name")
                .setParameter("levelId", levelId).getResultList();
    }

    @Override
    public List<AdminEntity> findChildEntities(int levelId, int parentEntityId) {

        return em.createQuery("select entity from AdminEntity entity where entity.level.id = :levelId " +
                "and entity.parent.id = :parentId order by entity.name")
                .setParameter("levelId", levelId)
                .setParameter("parentId", parentEntityId)
                .getResultList();
    }    
    

    public List<AdminEntity> find(int entityLevelId, int parentEntityId, int activityId) {
    	Query q =  query();
    	if (activityId > -1) {
    		q.withSitesOfActivityId(activityId);
    	} 
    	if (entityLevelId > -1) {
    		q.level(entityLevelId);
    	}
    	if (parentEntityId > -1) {
    		q.withParentEntityId(parentEntityId);
    	}
    	return q.execute();
    }
    
  
    public Query query() {

        final Criteria criteria = createCriteria();

        return new Query() {

            @Override
            public Query level(int levelId) {
                criteria.createAlias("entity.level", "level")
                        .add(Restrictions.eq("level.id", levelId));
                return this;
            }

            @Override
            public Query withParentEntityId(int id) {
                criteria.createAlias("entity.parent", "parent")
                        .add(Restrictions.eq("parent.id", id));
                return this;
            }

            @Override
            public Query withSitesOfActivityId(int id) {
                DetachedCriteria havingActivities =
                        DetachedCriteria.forClass(AdminEntity.class, "entity")
                                .createAlias("locations", "location")
                                .createAlias("location.sites", "site")
                                .createAlias("site.activity", "activity")
                                .add(Restrictions.eq("activity.id", id))
                                .setProjection(Projections.property("entity.id"));

                criteria.add(Subqueries.propertyIn("entity.id", havingActivities));
                return this;
            }


            @Override
            public List<AdminEntity> execute() {
                return criteria.list();
            }
        };
    }

    private Criteria createCriteria() {
        Session session = ((HibernateEntityManager) em).getSession();
        return session.createCriteria(AdminEntity.class, "entity");
    }

	@Override
	public List<AdminEntity> findBySiteIds(Set<Integer> siteIds) {
		// TODO Auto-generated method stub
		return null;
	}
}
