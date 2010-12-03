/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.sigmah.shared.command.result.CommandResult;

/**
 * Represents a project report.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportDTO implements Serializable, CommandResult {
    public final static long serialVersionUID = 1L;
    
    private Integer id;
    private Integer projectId;
    private String phaseName;
    private String name;
    private List<ProjectReportSectionDTO> sections;

    private Date lastEditDate;
    private String editorName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<ProjectReportSectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<ProjectReportSectionDTO> sections) {
        this.sections = sections;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
}
