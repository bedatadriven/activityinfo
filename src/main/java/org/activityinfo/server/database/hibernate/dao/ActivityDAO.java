package org.activityinfo.server.database.hibernate.dao;

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

import java.util.Set;

import org.activityinfo.server.database.hibernate.entity.Activity;

/**
 * DAO for the
 * {@link org.activityinfo.server.database.hibernate.entity.Activity} domain
 * object. Implemented automatically by proxy, see the Activity class for query
 * definitions.
 * 
 * @author Alex Bertram
 */
public interface ActivityDAO extends DAO<Activity, Integer> {

    Integer queryMaxSortOrder(int databaseId);

    Set<Activity> getActivitiesByDatabaseId(int databaseId);

}
