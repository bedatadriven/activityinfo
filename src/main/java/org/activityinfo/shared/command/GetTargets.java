package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.TargetResult;

public class GetTargets implements Command<TargetResult> {

	private int databaseId;

	public GetTargets() {

	}

	public GetTargets(int databaseId) {
		this.databaseId = databaseId;
	}
	
	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}
