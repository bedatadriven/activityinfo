/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.ProjectDTO;

/**
 * List of Projects visible to the user
 */
public class ProjectListResult implements CommandResult {
	
    private List<ProjectDTO> list;

    public ProjectListResult() {
    }

    public ProjectListResult(List<ProjectDTO> list) {
        this.list = list;
    }

    public List<ProjectDTO> getList() {
        return list;
    }

    public void setList(List<ProjectDTO> list) {
        this.list = list;
    }
}
