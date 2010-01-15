package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.SchemaElement;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.HibernateEntityManager;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class SchemaDAOJPA implements SchemaDAO {

    private final EntityManager em;

    @Inject
    public SchemaDAOJPA(EntityManager em) {
        this.em = em;
    }


    public List<UserPermission> getUserPermissions(UserDatabase database, int offset, int limit) {
        return em.createQuery("select up from UserPermission up where up.database.id = ?1")
                .setParameter(1, database.getId())
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public int getUserCount(UserDatabase database) {
        return ((Number) em.createQuery("select count(up) from UserPermission up where up.database.id = ?1")
                .setParameter(1, database.getId())
                .getSingleResult())
                .intValue();
    }


    /* (non-Javadoc)
      * @see org.activityinfo.server.dao.SchemaDAO#getDatabases(org.activityinfo.server.domain.User)
      */
    public List<UserDatabase> getDatabases(User user) {
        return em.createQuery("select d from UserDatabase d " +
                "order by d.name")
                .getResultList();
    }


    public <T extends SchemaElement> T findById(Class<T> clazz, int id) {
        return em.getReference(clazz, id);
    }

    @Override
    public <T> List<T> get(Class<T> clazz, Collection<Integer> ids) {
        Criteria criteria = ((HibernateEntityManager) em).getSession().createCriteria(clazz);
        criteria.add(Restrictions.in("id", ids));

        return criteria.list();
    }

    @Override
    public void save(SchemaElement persistentEntity) {
        em.persist(persistentEntity);
    }


    @Override
    public int getLastActivitySortOrder(int databaseId) {

        Integer max = (Integer) em.createQuery("select max(a.sortOrder) from Activity a where a.database.id = ?1")
                .setParameter(1, databaseId)
                .getSingleResult();

        return max == null ? 0 : max;

    }


}
