/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.ProjectDTOLight;

/**
 * List of projects visible to the user.
 */
public class ProjectListResult implements CommandResult {

    private static final long serialVersionUID = 749007653017696900L;

    /**
     * List of 'light' mapped projects.
     */
    private List<ProjectDTOLight> list;

    public ProjectListResult() {
    }

    public ProjectListResult(List<ProjectDTOLight> list) {
        this.list = list;
    }

    public List<ProjectDTOLight> getList() {
        return list;
    }

    public void setList(List<ProjectDTOLight> list) {
        this.list = list;
    }
}
