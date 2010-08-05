/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.dto.ProjectDTO;

/**
 * Retrieves a project available to the user.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class GetProject implements Command<ProjectDTO> {
	
	private static final long serialVersionUID = 5675515456984800856L;
	
	private int projectId;
	
	public GetProject() {
		// required, or serialization exception
	}
	
	public GetProject(int projectId) {
		this.projectId = projectId;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + projectId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetProject other = (GetProject) obj;
		if (projectId != other.projectId)
			return false;
		return true;
	}
}

