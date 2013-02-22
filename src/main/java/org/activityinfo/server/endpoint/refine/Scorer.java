package org.activityinfo.server.endpoint.refine;

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

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.apache.commons.lang.StringUtils;

public class Scorer {
    private double distance = 0;
    private double maxPossibleDistance = 0;
    private Query query;

    public Scorer(Query query) {
        this.query = query;
    }

    private void score(String expectedText, String queryText) {
        String strippedQueryText = QueryUtils.cleanupQuery(queryText
            .toLowerCase());
        distance += StringUtils.getLevenshteinDistance(
            expectedText.toLowerCase(), strippedQueryText);
        maxPossibleDistance += Math.max(expectedText.length(),
            strippedQueryText.length());
    }

    public double score(AdminEntity entity) {
        score(entity.getName(), query.getQuery());

        for (PropertyValue property : query.getProperties()) {
            if (isAdminUnitProperty(property)) {
                scoreLevel(entity, property);
            }
        }
        return score();
    }

    private void scoreLevel(AdminEntity entity, PropertyValue property) {
        AdminEntity parent = entity.getParent();
        while (parent != null) {
            if (parent.getLevel().getId() == levelIdFromProperty(property)) {
                score(parent.getName(), property.stringValue());
            }
            parent = parent.getParent();
        }
    }

    private boolean isAdminUnitProperty(PropertyValue property) {
        return property.getPropertyId().startsWith("adminUnit/");
    }

    private int levelIdFromProperty(PropertyValue property) {
        return Integer.parseInt(property.getPropertyId().substring(
            "adminUnit/".length()));
    }

    public double score() {
        if (maxPossibleDistance == 0) {
            return 0;
        }
        return (maxPossibleDistance - distance) / maxPossibleDistance;
    }
}
