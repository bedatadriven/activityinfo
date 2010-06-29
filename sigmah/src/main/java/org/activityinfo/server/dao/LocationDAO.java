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

package org.activityinfo.server.dao;

import org.activityinfo.server.domain.Location;

/**
 * DAO for the {@link org.activityinfo.server.domain.Location} domain object.
 *
 * @author Alex Bertram
 */
public interface LocationDAO extends DAO<Location, Integer> {

    /**
     * Adds a link between the given {@link org.activityinfo.server.domain.Location} and the
     * given {@link org.activityinfo.server.domain.AdminEntity AdminEntity}. If a link with another
     * AdminEntity exists belonging to the same {@link org.activityinfo.server.domain.AdminLevel AdminLevel},
     * it is removed.
     *
     */
    void updateAdminMembership(int locationId, int adminLevelId, int adminEntityId);


    /**
     * Adds a link between the given {@link org.activityinfo.server.domain.Location Location} and
     * {@link org.activityinfo.server.domain.AdminEntity AdminEntity}
     * @param locationId
     * @param adminEntityId
     */
    void addAdminMembership(int locationId, int adminEntityId);

    /**
     * Removes the link between the given {@link org.activityinfo.server.domain.Location Location}
     * and any {@link org.activityinfo.server.domain.AdminEntity AdminEntity} belonging to the
     * given {@link org.activityinfo.server.domain.AdminLevel}
     *
     * @param locationId
     * @param adminLevelId
     */
    void removeMembership(int locationId, int adminLevelId);
    
}
