package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ProjectDTO;

/*
 * The user wants to add a project to a UserDatabase
 */
public class AddProject implements Command<CreateResult> {
	private int databaseId;
	private ProjectDTO projectDTO;
	
	public AddProject() {
		super();
	}

	public AddProject(int databaseId, ProjectDTO project2dto) {
		super();
		this.databaseId = databaseId;
		this.projectDTO = project2dto;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public ProjectDTO getProjectDTO() {
		return projectDTO;
	}

	public void setProject2DTO(ProjectDTO project2dto) {
		projectDTO = project2dto;
	}
}