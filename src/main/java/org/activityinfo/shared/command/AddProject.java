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

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ProjectDTO;

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