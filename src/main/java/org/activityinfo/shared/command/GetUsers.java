

package org.activityinfo.shared.command;

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

import org.activityinfo.shared.command.result.UserResult;

/**
 * Queries the list of users authorized to access a given
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}
 *
 * The resulting {@link org.activityinfo.shared.dto.UserPermissionDTO} are
 * a projection of the UserLogin,
 * UserPermission, and
 * Partner tables.
 *
 */
public class GetUsers extends PagingGetCommand<UserResult>  {

    private int databaseId;

    private GetUsers() {
        // required
    }

	public GetUsers(int databaseId) {
		super();
		this.databaseId = databaseId;
	}

    /**
     * Gets the id of the database for which to query the list of UserPermissions.
     *
     * @return the id of the database for which to query the list of authorized users.
     */
	public int getDatabaseId() {
		return databaseId;
	}

    /**
     * Sets the id of the database for which toquery the list of UserPermissions.
      * @param databaseId
     */
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}
