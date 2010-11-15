package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.element.FlexibleElement;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Project model entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "project_model")
public class ProjectModel extends BaseModelData implements Serializable {

    private static final long serialVersionUID = -1266259112071917788L;

    private Long id;
    private String name;
    private PhaseModel rootPhase;
    private List<PhaseModel> phases = new ArrayList<PhaseModel>();
    private List<FlexibleElement> elements = new ArrayList<FlexibleElement>();
    private ProjectBanner projectBanner;
    private ProjectDetails projectDetails;
    private List<ProjectModelVisibility> visibilities;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_project_model")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 512)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(optional = true)
    @JoinColumn(name = "id_root_phase_model", nullable = true)
    public PhaseModel getRootPhase() {
        return rootPhase;
    }

    public void setRootPhase(PhaseModel rootPhase) {
        this.rootPhase = rootPhase;
    }

    @OneToMany(mappedBy = "parentProjectModel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<PhaseModel> getPhases() {
        return phases;
    }

    public void setPhases(List<PhaseModel> phases) {
        this.phases = phases;
    }

    public void addPhase(PhaseModel phase) {
        if (phase != null) {
            phase.setParentProjectModel(this);
            phases.add(phase);
        }
    }

    public void setElements(List<FlexibleElement> elements) {
        this.elements = elements;
    }

    @OneToMany(mappedBy = "parentProjectModel", cascade = CascadeType.ALL)
    public List<FlexibleElement> getElements() {
        return elements;
    }

    @OneToOne(mappedBy = "projectModel", cascade = CascadeType.ALL)
    public ProjectBanner getProjectBanner() {
        return projectBanner;
    }

    public void setProjectBanner(ProjectBanner projectBanner) {
        this.projectBanner = projectBanner;
    }

    @OneToOne(mappedBy = "projectModel", cascade = CascadeType.ALL)
    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    public List<ProjectModelVisibility> getVisibilities() {
        return visibilities;
    }

    public void setVisibilities(List<ProjectModelVisibility> visibilities) {
        this.visibilities = visibilities;
    }

    /**
     * Gets the type of this model for the given organization. If this model
     * isn't visible for this organization, <code>null</code> is returned.
     * 
     * @param organization
     *            The organization.
     * @return The type of this model for the given organization,
     *         <code>null</code> otherwise.
     */
    public ProjectModelType getVisibility(Organization organization) {

        if (organization == null || visibilities == null) {
            return null;
        }

        for (final ProjectModelVisibility visibility : visibilities) {
            if (visibility.getOrganization().getId() == organization.getId()) {
                return visibility.getType();
            }
        }

        return null;
    }
}
