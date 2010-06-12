package org.activityinfo.server.dao.hibernate;

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

public class AdminDAOImpl extends AbstractDAO<AdminEntity, Integer> implements AdminDAO {

    private final EntityManager em;

    @Inject
    public AdminDAOImpl(EntityManager em) {
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


    @Override
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
}
