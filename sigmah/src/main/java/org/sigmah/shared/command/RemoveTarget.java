package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class RemoveTarget implements Command<VoidResult> {
	private int targetId;
	private int databaseId;
	
	public RemoveTarget(int databaseId, int targetId) {
		super();
		this.targetId = targetId;
		this.databaseId = databaseId;
	}
	public RemoveTarget() {
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int projectId) {
		this.targetId = projectId;
	}
	
	public int getDatabaseId() {
		return databaseId;
	}
	
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}