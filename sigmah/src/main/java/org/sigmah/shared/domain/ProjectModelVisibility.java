package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Sets the visibility of a project model for an organization and the type of
 * this model for it.
 * 
 * @author tmi
 * @see ProjectModelVisibility
 */
@Entity
@Table(name = "project_model_visibility")
public class ProjectModelVisibility implements Serializable {

    private static final long serialVersionUID = -6641538192146900691L;

    private Integer id;
    private Organization organization;
    private ProjectModel model;
    private ProjectModelType type;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_visibility")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_organization", nullable = false)
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project_model", nullable = false)
    public ProjectModel getModel() {
        return model;
    }

    public void setModel(ProjectModel model) {
        this.model = model;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public ProjectModelType getType() {
        return type;
    }

    public void setType(ProjectModelType type) {
        this.type = type;
    }
}
