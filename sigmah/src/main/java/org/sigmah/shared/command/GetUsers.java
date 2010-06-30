/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.UserResult;

/**
 * Queries the list of users authorized to access a given
 * {@link org.sigmah.server.domain.UserDatabase}
 *
 * The resulting {@link org.sigmah.shared.dto.UserPermissionDTO} are
 * a projection of the {@link org.sigmah.server.domain.User},
 * {@link org.sigmah.server.domain.UserPermission}, and
 * {@link org.sigmah.server.domain.OrgUnit} entities.
 *
 */
public class GetUsers extends PagingGetCommand<UserResult> {

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
