package org.activityinfo.server.database.hibernate.entity;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.ejb.HibernateEntityManager;

/*
 * @author Alex Bertram
 */

public final class DomainFilters {

    private DomainFilters() {
    }

    public static void applyUserFilter(User user, EntityManager em) {
        applyUserFilter(user.getId(), em);
    }

    public static void applyUserFilter(int userId, EntityManager em) {
        applyDeletedFilter(em);
        applyVisibleFilter(userId, em);
    }

    public static void applyDeletedFilter(EntityManager em) {
        org.hibernate.Session session = ((HibernateEntityManager) em)
            .getSession();

        /* Hide entities deleted by users */
        session.enableFilter("hideDeleted");
    }

    public static void applyVisibleFilter(User user, EntityManager em) {
        applyVisibleFilter(user.getId(), em);
    }

    public static void applyVisibleFilter(int userId, EntityManager em) {
        /* Hide entities that this user does not have permission to view */
        org.hibernate.Session session = ((HibernateEntityManager) em)
            .getSession();

        Filter filter = session.enableFilter("userVisible");
        filter.setParameter("currentUserId", userId);
    }
}
