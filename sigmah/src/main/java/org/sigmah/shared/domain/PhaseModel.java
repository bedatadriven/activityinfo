package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sigmah.shared.domain.layout.Layout;

/**
 * Phase model entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "phase_model")
public class PhaseModel implements Serializable {

    private static final long serialVersionUID = 6961563905925156300L;

    private Long id;
    private String name;
    private List<PhaseModel> successors = new ArrayList<PhaseModel>();
    private ProjectModel parentProjectModel;
    private Layout layout;
    private List<Report> reports = new ArrayList<Report>();
    private Integer displayOrder;
    private PhaseModelDefinition definition;
    private String guide;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_phase_model")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 8192)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany
    @JoinTable(name = "phase_model_sucessors", joinColumns = { @JoinColumn(name = "id_phase_model") }, inverseJoinColumns = { @JoinColumn(name = "id_phase_model_successor") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
            "id_phase_model", "id_phase_model_successor" }) })
    public List<PhaseModel> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<PhaseModel> successors) {
        this.successors = successors;
    }

    public void addSuccessor(PhaseModel successor) {
        if (successor != null) {
            successors.add(successor);
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project_model", nullable = false)
    public ProjectModel getParentProjectModel() {
        return parentProjectModel;
    }

    public void setParentProjectModel(ProjectModel parentProjectModel) {
        this.parentProjectModel = parentProjectModel;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_layout", nullable = true)
    public Layout getLayout() {
        return layout;
    }

    @OneToMany(mappedBy = "phaseModel", cascade = CascadeType.ALL)
    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Column(name = "display_order", nullable = true)
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "definition_id", nullable = true)
    public PhaseModelDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(PhaseModelDefinition definition) {
        this.definition = definition;
    }

    @Column(name = "guide", columnDefinition = "TEXT", nullable = true)
    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
