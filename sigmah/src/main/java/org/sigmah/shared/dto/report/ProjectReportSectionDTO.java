/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.report;

import java.util.List;

/**
 * Section of a project report.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportSectionDTO implements ProjectReportContent {
    public final static long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private List<ProjectReportContent> children;

    public List<ProjectReportContent> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectReportContent> children) {
        this.children = children;
    }

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
}
