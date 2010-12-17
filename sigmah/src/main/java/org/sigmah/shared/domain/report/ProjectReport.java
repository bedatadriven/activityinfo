/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.report;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.element.FlexibleElement;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class ProjectReport implements Serializable {

    private static final long serialVersionUID = -7388489166961720683L;

    private Integer id;
    private String name;
    private ProjectReportModel model;
    private ProjectReportVersion currentVersion;
    private Project project;
    private FlexibleElement flexibleElement;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne
    public ProjectReportModel getModel() {
        return model;
    }

    public void setModel(ProjectReportModel model) {
        this.model = model;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public ProjectReportVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(ProjectReportVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    @ManyToOne
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @OneToOne
    public FlexibleElement getFlexibleElement() {
        return flexibleElement;
    }

    public void setFlexibleElement(FlexibleElement flexibleElement) {
        this.flexibleElement = flexibleElement;
    }
}
