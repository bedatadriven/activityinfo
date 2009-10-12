package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.UserResult;

/**
 * Queries the list of users authorized to access a given
 * {@link org.activityinfo.server.domain.UserDatabase}
 *
 * The resulting {@link org.activityinfo.shared.dto.UserModel} are
 * a projection of the {@link org.activityinfo.server.domain.User},
 * {@link org.activityinfo.server.domain.UserPermission}, and
 * {@link org.activityinfo.server.domain.Partner} entities.
 *
 */
public class GetUsers extends PagingGetCommand<UserResult> {

    private int databaseId;

    private GetUsers() {

    }

	public GetUsers(int databaseId) {
		super();
		this.databaseId = databaseId;
	}

    /**
     *
     * @return The Id of the database for which to query the list of authorized users.
     */
	public int getDatabaseId() {
		return databaseId;
	}


	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}
