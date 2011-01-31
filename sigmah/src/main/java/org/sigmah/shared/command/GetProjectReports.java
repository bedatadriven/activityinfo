/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import java.util.Date;

import org.sigmah.shared.command.result.ProjectReportListResult;
import org.sigmah.shared.domain.report.ProjectReport;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.shared.dto.value.ListableValue;

/**
 * Request to retrieves the reports attached to a given project.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetProjectReports implements Command<ProjectReportListResult> {

    private static final long serialVersionUID = -5074144662654783191L;

    private Integer projectId;
    private Integer reportId;

    public GetProjectReports() {
    }

    public GetProjectReports(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

}
