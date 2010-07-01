/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.ProjectDTO;

import java.util.List;

/**
 * List of Projects visible to the user
 */
public class ProjectList implements CommandResult {
    private List<ProjectDTO> list;

    public ProjectList() {
    }

    public ProjectList(List<ProjectDTO> list) {
        this.list = list;
    }

    public List<ProjectDTO> getList() {
        return list;
    }

    public void setList(List<ProjectDTO> list) {
        this.list = list;
    }
}
