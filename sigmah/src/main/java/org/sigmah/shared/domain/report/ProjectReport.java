/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.report;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.sigmah.shared.domain.Deleteable;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.element.FlexibleElement;

/**
 * Report based on a {@link ProjectReportModel}.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "dateDeleted IS null") })
public class ProjectReport implements Serializable, Deleteable {

    private static final long serialVersionUID = -7388489166961720683L;

    /**
     * Identifier of the report.
     */
    private Integer id;
    /**
     * Name of the report.
     */
    private String name;
    /**
     * Model defining the structure of this report.
     */
    private ProjectReportModel model;
    /**
     * Current version of the report (contains the values of each section).
     */
    private ProjectReportVersion currentVersion;
    /**
     * Project hosting this report.
     */
    private Project project;
    /**
     * Flexible element hosting this report.
     */
    private FlexibleElement flexibleElement;
    /**
     * Date of deletion.
     */
    private Date dateDeleted;

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

    @ManyToOne
    public FlexibleElement getFlexibleElement() {
        return flexibleElement;
    }

    public void setFlexibleElement(FlexibleElement flexibleElement) {
        this.flexibleElement = flexibleElement;
    }

    @Temporal(value=TemporalType.TIMESTAMP)
    public Date getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    @Override
    public void delete() {
        dateDeleted = new Date();
    }

    @Override
    @Transient
    public boolean isDeleted() {
        return dateDeleted != null;
    }
}
