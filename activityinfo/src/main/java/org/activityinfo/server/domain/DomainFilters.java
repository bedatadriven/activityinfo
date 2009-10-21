/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.domain;

import org.hibernate.Filter;
import org.hibernate.ejb.HibernateEntityManager;

import javax.persistence.EntityManager;

/*
 * @author Alex Bertram
 */

public class DomainFilters {

    public static void applyUserFilter(User user, EntityManager em) {
         applyDeletedFilter(em);
        applyVisibleFilter(user,em);

    }

    public static void applyDeletedFilter(EntityManager em) {
        org.hibernate.Session session = ((HibernateEntityManager) em).getSession();

        /* Hide entities deleted by users  */
        session.enableFilter("hideDeleted");

    }

    public static void applyVisibleFilter(User user, EntityManager em) {
        /* Hide entities that this user does not have permission to view */
        org.hibernate.Session session = ((HibernateEntityManager) em).getSession();

        Filter filter = session.enableFilter("userVisible");
		filter.setParameter("currentUserId", user.getId());

    }


}
