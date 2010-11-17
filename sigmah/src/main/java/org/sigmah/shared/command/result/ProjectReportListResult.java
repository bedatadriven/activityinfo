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

    public ProjectReportListResult() {
    }

    public ProjectReportListResult(List<GetProjectReports.ReportReference> data) {
        super(data);
    }
}
