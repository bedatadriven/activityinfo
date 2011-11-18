/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler.crud;

import java.util.Date;

import javax.persistence.EntityManager;

import org.sigmah.server.database.hibernate.dao.ActivityDAO;
import org.sigmah.server.database.hibernate.dao.UserDatabaseDAO;
import org.sigmah.server.database.hibernate.entity.Activity;
import org.sigmah.server.database.hibernate.entity.LocationType;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.Published;

import com.google.inject.Inject;

public class ActivityPolicy implements EntityPolicy<Activity> {

	private final EntityManager em;
	private final ActivityDAO activityDAO;
	private final UserDatabaseDAO databaseDAO;

	@Inject
	public ActivityPolicy(EntityManager em, ActivityDAO activityDAO,
			UserDatabaseDAO databaseDAO) {
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

        if (changes.containsKey("published")) {
            activity.setPublished(((Published)changes.get("published")).getIndex());
        }
        
        if (changes.containsKey("sortOrder")) {
            activity.setSortOrder((Integer) changes.get("sortOrder"));
        }

        activity.getDatabase().setLastSchemaUpdate(new Date());
    }
}
