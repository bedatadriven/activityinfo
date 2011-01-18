/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.domain.Amendment.Action;
import org.sigmah.shared.dto.ProjectDTO;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AmendmentAction implements Command<ProjectDTO> {
    private Amendment.Action action;
    private int projectId;

    public AmendmentAction() {
    }
    public AmendmentAction(int projectId, Action action) {
        this.projectId = projectId;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
