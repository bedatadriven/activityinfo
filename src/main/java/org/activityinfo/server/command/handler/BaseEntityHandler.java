

package org.activityinfo.server.command.handler;

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

import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Activity;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.Attribute;
import org.activityinfo.server.database.hibernate.entity.AttributeGroup;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.LocationType;
import org.activityinfo.server.database.hibernate.entity.LockedPeriod;
import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.Project;
import org.activityinfo.server.database.hibernate.entity.Target;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.dto.LocationTypeDTO;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import com.bedatadriven.rebar.time.calendar.LocalDate;

/**
 * Provides functionality common to CreateEntityHandler and
 * UpdateEntityHandler
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class BaseEntityHandler {

    private final EntityManager em;

    public BaseEntityHandler(EntityManager em) {
        this.em = em;
    }

    protected void updateIndicatorProperties(Indicator indicator, Map<String, Object> changes) {
        if (changes.containsKey("name")) {
            indicator.setName((String) changes.get("name"));
        }

        if (changes.containsKey("aggregation")) {
            indicator.setAggregation((Integer) changes.get("aggregation"));
        }

        if (changes.containsKey("category")) {
            indicator.setCategory((String) changes.get("category"));
        }

        if (changes.containsKey("listHeader")) {
            indicator.setListHeader((String) changes.get("listHeader"));
        }

        if (changes.containsKey("description")) {
            indicator.setDescription((String) changes.get("description"));
        }

        if (changes.containsKey("units")) {
            indicator.setUnits((String) changes.get("units"));
        }

        if (changes.containsKey("sortOrder")) {
            indicator.setSortOrder((Integer) changes.get("sortOrder"));
        }

        indicator.getActivity().getDatabase().setLastSchemaUpdate(new Date());
    }

    protected void updateAttributeProperties(Map<String, Object> changes, Attribute attribute) {
        if (changes.containsKey("name")) {
            attribute.setName((String) changes.get("name"));
        }
        if (changes.containsKey("sortOrder")) {
            attribute.setSortOrder((Integer) changes.get("sortOrder"));
        }
        // TODO: update lastSchemaUpdate
    }

    protected void updateAttributeGroupProperties(AttributeGroup group, Map<String, Object> changes) {
        if (changes.containsKey("name")) {
            group.setName((String) changes.get("name"));
        }

        if (changes.containsKey("multipleAllowed")) {
            group.setMultipleAllowed((Boolean) changes.get("multipleAllowed"));
        }
        if (changes.containsKey("sortOrder")) {
            group.setSortOrder((Integer) changes.get("sortOrder"));
        }
    }
    
    protected void updateLockedPeriodProperties(LockedPeriod lockedPeriod, Map<String, Object> changes) {
        if (changes.containsKey("name")) {
        	lockedPeriod.setName((String) changes.get("name"));
        }
        if (changes.containsKey("toDate")) {
        	lockedPeriod.setToDate((LocalDate) changes.get("toDate"));
        }
        if (changes.containsKey("fromDate")) {
        	lockedPeriod.setFromDate((LocalDate) changes.get("fromDate"));
        }
        if (changes.containsKey("enabled")) {
        	lockedPeriod.setEnabled((Boolean) changes.get("enabled"));
        }
        
        lockedPeriod.getParentDatabase().setLastSchemaUpdate(new Date());
        entityManager().merge(lockedPeriod);
    }

    protected void updateActivityProperties(Activity activity, Map<String, Object> changes) {
        if (changes.containsKey("name")) {
            activity.setName((String) changes.get("name"));
        }

        if (changes.containsKey("locationType")) {
            activity.setLocationType(
                    entityManager().getReference(LocationType.class,
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
    
    protected void updateTargetProperties(Target target, Map<String, Object> changes) {
        if (changes.containsKey("name")) {
        	target.setName((String) changes.get("name"));
        }

        if (changes.containsKey("date1")) {
        	target.setDate1((Date) changes.get("date1"));
        }
        
        if (changes.containsKey("date2")) {
        	target.setDate2((Date) changes.get("date2"));
        }
        
        if (changes.containsKey("projectId")) {
            target.setProject(
                    entityManager().getReference(Project.class,
                            changes.get("projectId")));
        }
       
        if (changes.containsKey("partnerId")) {
            target.setPartner(
                    entityManager().getReference(Partner.class,
                             changes.get("partnerId")));
        }
        
        if (changes.containsKey("AdminEntityId")) {
            target.setAdminEntity(
                    entityManager().getReference(AdminEntity.class,
                             changes.get("AdminEntityId")));
        }
        
    }
    

    /**
     * Asserts that the user has permission to modify the structure of the given database.
     *
     * @param user     THe user for whom to check permissions
     * @param database The database the user is trying to modify
     * @throws IllegalAccessCommandException If the user does not have permission
     */
    protected void assertDesignPriviledges(User user, UserDatabase database) throws IllegalAccessCommandException {

        if (!database.isAllowedDesign(user)) {
            throw new IllegalAccessCommandException();
        }

    }

	public EntityManager entityManager() {
		return em;
	}

}
