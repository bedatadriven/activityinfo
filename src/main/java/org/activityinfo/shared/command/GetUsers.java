package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.dto.UserModel;

public class GetUsers extends PagingGetCommand<UserResult> {

    private GetUsers() {

    }

	public GetUsers(int databaseId) {
		super();
		this.databaseId = databaseId;
	}

	private int databaseId;

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
	

}
