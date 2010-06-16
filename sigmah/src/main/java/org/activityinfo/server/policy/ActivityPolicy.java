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

package org.activityinfo.server.policy;

import com.google.inject.Inject;
import org.activityinfo.server.dao.ActivityDAO;
import org.activityinfo.server.dao.UserDatabaseDAO;
import org.activityinfo.server.domain.Activity;
import org.activityinfo.server.domain.LocationType;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.shared.dto.LocationTypeDTO;

import javax.persistence.EntityManager;
import java.util.Date;

public class ActivityPolicy implements EntityPolicy<Activity> {

    private final EntityManager em;
    private final ActivityDAO activityDAO;
    private final UserDatabaseDAO databaseDAO;

    @Inject
    public ActivityPolicy(EntityManager em, ActivityDAO activityDAO, UserDatabaseDAO databaseDAO) {
        this.em = em;
        this.activityDAO = activityDAO;
        this.databaseDAO = databaseDAO;
    }

    @Override
    public Object create(User user, PropertyMap properties) {

        UserDatabase database = getDatabase(properties);
        assertDesignPrivileges(user, database);

        // create the entity
        Activity activity = new Activity();
        activity.setDatabase(database);
        activity.setSortOrder(calculateNextSortOrderIndex(database.getId()));
        activity.setLocationType(getLocationType(properties));

        applyProperties(activity, properties);

        activityDAO.persist(activity);

        return activity.getId();
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        Activity activity = em.find(Activity.class, entityId);

        assertDesignPrivileges(user, activity.getDatabase());

        applyProperties(activity, changes);
    }

    private void assertDesignPrivileges(User user, UserDatabase database) {
        if (!database.isAllowedDesign(user)) {
            throw new IllegalAccessError();
        }
    }

    private UserDatabase getDatabase(PropertyMap properties) {
        int databaseId = (Integer) properties.get("databaseId");

        UserDatabase database = databaseDAO.findById(databaseId);
        return database;
    }

    private LocationType getLocationType(PropertyMap properties) {
        int locationTypeId = ((Integer) properties.get("locationTypeId"));
        LocationType type = em.getReference(LocationType.class, locationTypeId);
        return type;
    }

    private Integer calculateNextSortOrderIndex(int databaseId) {
        Integer maxSortOrder = activityDAO.queryMaxSortOrder(databaseId);
        return maxSortOrder == null ? 1 : maxSortOrder + 1;
    }

    private void applyProperties(Activity activity, PropertyMap changes) {
        if (changes.containsKey("name")) {
            activity.setName((String) changes.get("name"));
        }

        if (changes.containsKey("assessment")) {
            activity.setAssessment((Boolean) changes.get("assessment"));
        }

        if (changes.containsKey("locationType")) {
            activity.setLocationType(
                    em.getReference(LocationType.class,
                            ((LocationTypeDTO) changes.get("locationType")).getId()));
        }

        if (changes.containsKey("category")) {
            activity.setCategory((String) changes.get("category"));
        }

        if (changes.containsKey("mapIcon")) {
            activity.setMapIcon((String) changes.get("mapIcon"));
        }

        if (changes.containsKey("reportingFrequency")) {
            activity.setReportingFrequency((Integer) changes.get("reportingFrequency"));
        }

        if (changes.containsKey("sortOrder")) {
            activity.setSortOrder((Integer) changes.get("sortOrder"));
        }

        activity.getDatabase().setLastSchemaUpdate(new Date());
    }
}

