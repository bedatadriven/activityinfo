package org.activityinfo.server.dao.jpa;

import com.google.inject.Inject;
import org.activityinfo.server.dao.AdminDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.ejb.HibernateEntityManager;

import javax.persistence.EntityManager;
import java.util.List;

public class AdminDAOJPA implements AdminDAO  {

	private final EntityManager em;
	
	@Inject
	public AdminDAOJPA(EntityManager em) {
		this.em = em;
	}

    /* (non-Javadoc)
      * @see org.activityinfo.server.dao.AdminDAO#getEntities(int)
      */
	public List<AdminEntity> getEntities(int levelId) {
		return em.createQuery("select entity from AdminEntity entity where entity.level.id = :levelId order by entity.name")
				.setParameter("levelId", levelId).getResultList();
	}

	/* (non-Javadoc)
	 * @see org.activityinfo.server.dao.AdminDAO#getEntities(int, java.lang.Integer)
	 */
	public List<AdminEntity> getEntities(int levelId, Integer parentId) {

		if(parentId == null) {
			return getEntities(levelId);
		} else {
			
			return em.createQuery("select entity from AdminEntity entity where entity.level.id = :levelId " +
					"and entity.parent.id = :parentId order by entity.name")
						.setParameter("levelId", levelId)
						.setParameter("parentId", parentId)
						.getResultList();
		}
	}

    public List<AdminEntity> getEntities(int levelId, Integer parentId, int activityId) {

        Session session = ((HibernateEntityManager)em).getSession();
        Criteria criteria = session.createCriteria(AdminEntity.class, "entity")
                  .createAlias("entity.level", "level")
                  .add( Restrictions.eq("level.id", levelId));

        if(parentId != null)
            criteria
                  .createAlias("entity.parent", "parent")
                  .add( Restrictions.eq("parent.id", parentId));

        DetachedCriteria havingActivities = DetachedCriteria.forClass(AdminEntity.class, "entity")
                .createAlias("locations", "location")
                .createAlias("location.sites", "site")
                .createAlias("site.activity", "activity")
                .add( Restrictions.eq("activity.id", activityId) )
                .setProjection( Projections.property("entity.id"));

        criteria.add(Subqueries.propertyIn("entity.id", havingActivities));

        return criteria.list();
    }
}
