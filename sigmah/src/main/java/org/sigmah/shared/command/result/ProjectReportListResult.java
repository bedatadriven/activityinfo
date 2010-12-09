/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.io.Serializable;
import java.util.List;
import org.sigmah.shared.command.GetProjectReports;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportListResult extends ListResult<GetProjectReports.ReportReference> implements Serializable {

    private static final long serialVersionUID = -4386277858499888179L;

    public ProjectReportListResult() {
        // Serialization.
    }

    public ProjectReportListResult(List<GetProjectReports.ReportReference> data) {
        super(data);
    }
}
