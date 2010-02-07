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
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.LocationDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.Location;

import javax.persistence.EntityManager;

public class LocationHibernateDAO extends AbstractDAO<Location, Integer> implements LocationDAO {

    @Inject
    public LocationHibernateDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void updateAdminMembership(int locationId, int adminLevelId, int adminEntityId) {
        removeExistingRow(locationId, adminLevelId);
        addRow(locationId, adminEntityId);
    }

    @Override
    public void addAdminMembership(int locationId, int adminEntityId) {
        addRow(locationId, adminEntityId);
    }

    private void addRow(int locationId, int adminEntityId) {
        Location location = em.find(Location.class, locationId);
        location.getAdminEntities().add(em.getReference(AdminEntity.class, adminEntityId));
    }

    private void removeExistingRow(int locationId, int adminLevelId) {
        em.createNativeQuery("delete from LocationAdminLink where " +
                "LocationId = ?1 and " +
                "AdminEntityId in " +
                "(select e.AdminEntityId from AdminEntity e where e.AdminLevelId = ?2)")
                .setParameter(1, locationId)
                .setParameter(2, adminLevelId)
                .executeUpdate();
    }
}
