package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

/*
 * Removes Project with given projectId from the persistence store
 */
public class RemoveProject implements Command<VoidResult> {
	private int projectId;
	private int databaseId;
	
	public RemoveProject(int projectId, int databaseId) {
		super();
		this.projectId = projectId;
		this.databaseId = databaseId;
	}
	public RemoveProject() {
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public int getDatabaseId() {
		return databaseId;
	}
	
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}