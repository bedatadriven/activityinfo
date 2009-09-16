package org.activityinfo.server.domain;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.Filter;

import javax.persistence.EntityManager;

/*
 * @author Alex Bertram
 */

public class DomainFilters {

    public static void applyUserFilter(User user, EntityManager em) {
        org.hibernate.Session session = ((HibernateEntityManager) em).getSession();

        /* Hide entities deleted by users  */
        session.enableFilter("hideDeleted");

        /* Hide entities that this user does not have permission to view */

        Filter filter = session.enableFilter("userVisible");
		filter.setParameter("currentUserId", user.getId());

    }


}
