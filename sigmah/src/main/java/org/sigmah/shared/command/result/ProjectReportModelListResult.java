/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.io.Serializable;
import java.util.List;
import org.sigmah.shared.command.GetProjectReportModels;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportModelListResult extends ListResult<GetProjectReportModels.ModelReference> implements Serializable  {
    
    public ProjectReportModelListResult() {
    }

    public ProjectReportModelListResult(List<GetProjectReportModels.ModelReference> data) {
        super(data);
    }
}
