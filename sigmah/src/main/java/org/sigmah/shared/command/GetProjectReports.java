/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import com.extjs.gxt.ui.client.data.BaseModelData;
import java.util.Date;
import org.sigmah.shared.command.result.ProjectReportListResult;
import org.sigmah.shared.domain.report.ProjectReport;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetProjectReports implements Command<ProjectReportListResult> {
    public static class ReportReference extends BaseModelData {
        public ReportReference() {}
        public ReportReference(ProjectReport report) {
            this.set("id", report.getId());
            this.set("name", report.getName());
            this.set("lastEditDate", report.getLastEditDate());
            this.set("editorName", report.getEditorShortName());
            this.set("phaseName", report.getPhaseName());

            if(report.getFlexibleElement() != null)
                this.set("flexibleElementLabel", report.getFlexibleElement().getLabel());
        }

        public Integer getId() {
            return get("id");
        }
        public void setId(Integer id) {
            this.set("id", id);
        }

        public String getName() {
            return get("name");
        }
        public void setName(String name) {
            this.set("name", name);
        }

        public String getPhaseName() {
            return get("phaseName");
        }
        public void setPhaseName(String phaseName) {
            this.set("phaseName", phaseName);
        }

        public String getFlexibleElementLabel() {
           return get("flexibleElementLabel");
        }
        public void setFlexibleElementLabel(String label) {
            this.set("flexibleElementLabel", label);
        }

        public Date getLastEditDate() {
            return get("lastEditDate");
        }
        public void setLastEditDate(Date date) {
            this.set("lastEditDate", date);
        }

        public String getEditorName() {
            return get("editorName");
        }
        public void setEditorName(String editorName) {
            this.set("editorName", editorName);
        }
    }

    private Integer projectId;
    private Integer reportId;

    public GetProjectReports() {}

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
