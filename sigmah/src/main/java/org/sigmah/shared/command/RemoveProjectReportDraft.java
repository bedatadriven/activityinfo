/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

/**
 * Entirely removes a project report draft.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class RemoveProjectReportDraft implements Command<VoidResult> {
    private int versionId;

    public RemoveProjectReportDraft() {
    }

    public RemoveProjectReportDraft(int versionId) {
        this.versionId = versionId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }
    
}
