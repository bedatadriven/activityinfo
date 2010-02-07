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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import org.activityinfo.server.domain.*;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides functionality common to CreateEntityHandler and
 * UpdateEntityHandler
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class BaseEntityHandler {

    protected final EntityManager em;

    public BaseEntityHandler(EntityManager em) {
        this.em = em;
    }

    protected void updateIndicatorProperties(Indicator indicator, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            indicator.setName((String) changes.get("name"));

        if (changes.containsKey("aggregation"))
            indicator.setAggregation((Integer) changes.get("aggregation"));

        if (changes.containsKey("category"))
            indicator.setCategory((String) changes.get("category"));

        if (changes.containsKey("collectIntervention"))
            indicator.setCollectIntervention((Boolean) changes.get("collectIntervention"));

        if (changes.containsKey("collectMonitoring"))
            indicator.setCollectMonitoring((Boolean) changes.get("collectMonitoring"));

        if (changes.containsKey("listHeader"))
            indicator.setListHeader((String) changes.get("listHeader"));

        if (changes.containsKey("description"))
            indicator.setDescription((String) changes.get("description"));

        if (changes.containsKey("units"))
            indicator.setUnits((String) changes.get("units"));

        if (changes.containsKey("sortOrder"))
            indicator.setSortOrder((Integer) changes.get("sortOrder"));

        indicator.getActivity().getDatabase().setLastSchemaUpdate(new Date());
    }

    protected void updateAttributeProperties(Map<String, Object> changes, Attribute attribute) {
        if (changes.containsKey("name"))
            attribute.setName((String) changes.get("name"));
        if (changes.containsKey("sortOrder"))
            attribute.setSortOrder((Integer) changes.get("sortOrder"));

        // TODO: update lastSchemaUpdate
    }

    protected void updateAttributeGroupProperties(AttributeGroup group, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            group.setName((String) changes.get("name"));

        if (changes.containsKey("multipleAllowed")) {
            group.setMultipleAllowed((Boolean) changes.get("multipleAllowed"));
        }
        if (changes.containsKey("sortOrder")) {
            group.setSortOrder((Integer) changes.get("sortOrder"));
        }


    }

    protected void updateActivityProperties(Activity activity, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            activity.setName((String) changes.get("name"));

        if (changes.containsKey("assessment"))
            activity.setAssessment((Boolean) changes.get("assessment"));

        if (changes.containsKey("locationType"))
            activity.setLocationType(
                    em.getReference(LocationType.class,
                            ((LocationTypeModel) changes.get("locationType")).getId()));

        if (changes.containsKey("category"))
            activity.setCategory((String) changes.get("category"));

        if (changes.containsKey("mapIcon"))
            activity.setMapIcon((String) changes.get("mapIcon"));

        if (changes.containsKey("reportingFrequency"))
            activity.setReportingFrequency((Integer) changes.get("reportingFrequency"));

        if (changes.containsKey("sortOrder"))
            activity.setSortOrder((Integer) changes.get("sortOrder"));

        activity.getDatabase().setLastSchemaUpdate(new Date());
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

}
