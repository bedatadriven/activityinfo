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

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.AdminDAOImpl;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.AdminLevel;

import java.util.List;

@ImplementedBy(AdminDAOImpl.class)
public interface AdminDAO extends DAO<AdminEntity, Integer> {

    /**
     * @param levelId The id of the administrative level for which to return the entities
     * @return A list of administrative entities that constitute an administrative
     *         level. (e.g. return all provinces, return all districts, etc)
     */
    List<AdminEntity> findRootEntities(int levelId);

    /**
     * Returns
     *
     * @param levelId        See {@link AdminLevel}
     * @param parentEntityId
     * @return A list of the children of a given admin entity for at a given level.
     */
    List<AdminEntity> findChildEntities(int levelId, int parentEntityId);

    Query query();

    public interface Query {
        Query level(int levelId);

        Query withParentEntityId(int parentEntityId);

        Query withSitesOfActivityId(int activityId);

        List<AdminEntity> execute();
    }
}