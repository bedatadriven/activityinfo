package org.sigmah.shared.domain.logframe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.ProjectModel;

/**
 * Contains some attributes to parameterize a log frame.<br/>
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_model")
public class LogFrameModel implements Serializable {

    private static final long serialVersionUID = -8714555958028249713L;

    private ProjectModel projectModel;

    private Integer id;
    private String name;

    // Specific objectives parameters.
    private Boolean enableSpecificObjectivesGroups;
    private Integer specificObjectivesMax;
    private Integer specificObjectivesGroupsMax;
    private Integer specificObjectivesPerGroupMax;

    // Expected results parameters.
    private Boolean enableExpectedResultsGroups;
    private Integer expectedResultsMax;
    private Integer expectedResultsGroupsMax;
    private Integer expectedResultsPerGroupMax;
    private Integer expectedResultsPerSpecificObjectiveMax;

    // Activities parameters.
    private Boolean enableActivitiesGroups;
    private Integer activitiesMax;
    private Integer activitiesGroupsMax;
    private Integer activitiesPerGroupMax;
    private Integer activitiesPerExpectedResultMax;

    // Prerequisites parameters.
    private Boolean enablePrerequisitesGroups;
    private Integer prerequisitesMax;
    private Integer prerequisitesGroupsMax;
    private Integer prerequisitesPerGroupMax;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_log_frame")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "id_project_model")
    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    @Column(name = "name", nullable = false, length = 512)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "so_enable_groups")
    public Boolean getEnableSpecificObjectivesGroups() {
        return enableSpecificObjectivesGroups;
    }

    public void setEnableSpecificObjectivesGroups(Boolean enableSpecificObjectivesGroups) {
        this.enableSpecificObjectivesGroups = enableSpecificObjectivesGroups;
    }

    @Column(name = "so_max")
    public Integer getSpecificObjectivesMax() {
        return specificObjectivesMax;
    }

    public void setSpecificObjectivesMax(Integer specificObjectivesMax) {
        this.specificObjectivesMax = specificObjectivesMax;
    }

    @Column(name = "so_gp_max")
    public Integer getSpecificObjectivesGroupsMax() {
        return specificObjectivesGroupsMax;
    }

    public void setSpecificObjectivesGroupsMax(Integer specificObjectivesGroupsMax) {
        this.specificObjectivesGroupsMax = specificObjectivesGroupsMax;
    }

    @Column(name = "so_per_gp_max")
    public Integer getSpecificObjectivesPerGroupMax() {
        return specificObjectivesPerGroupMax;
    }

    public void setSpecificObjectivesPerGroupMax(Integer specificObjectivesPerGroupMax) {
        this.specificObjectivesPerGroupMax = specificObjectivesPerGroupMax;
    }

    @Column(name = "er_enable_groups")
    public Boolean getEnableExpectedResultsGroups() {
        return enableExpectedResultsGroups;
    }

    public void setEnableExpectedResultsGroups(Boolean enableExpectedResultsGroups) {
        this.enableExpectedResultsGroups = enableExpectedResultsGroups;
    }

    @Column(name = "er_max")
    public Integer getExpectedResultsMax() {
        return expectedResultsMax;
    }

    public void setExpectedResultsMax(Integer expectedResultsMax) {
        this.expectedResultsMax = expectedResultsMax;
    }

    @Column(name = "er_gp_max")
    public Integer getExpectedResultsGroupsMax() {
        return expectedResultsGroupsMax;
    }

    public void setExpectedResultsGroupsMax(Integer expectedResultsGroupsMax) {
        this.expectedResultsGroupsMax = expectedResultsGroupsMax;
    }

    @Column(name = "er_per_gp_max")
    public Integer getExpectedResultsPerGroupMax() {
        return expectedResultsPerGroupMax;
    }

    public void setExpectedResultsPerGroupMax(Integer expectedResultsPerGroupMax) {
        this.expectedResultsPerGroupMax = expectedResultsPerGroupMax;
    }

    @Column(name = "er_per_so_max")
    public Integer getExpectedResultsPerSpecificObjectiveMax() {
        return expectedResultsPerSpecificObjectiveMax;
    }

    public void setExpectedResultsPerSpecificObjectiveMax(Integer expectedResultsPerSpecificObjectiveMax) {
        this.expectedResultsPerSpecificObjectiveMax = expectedResultsPerSpecificObjectiveMax;
    }

    @Column(name = "a_enable_groups")
    public Boolean getEnableActivitiesGroups() {
        return enableActivitiesGroups;
    }

    public void setEnableActivitiesGroups(Boolean enableActivitiesGroups) {
        this.enableActivitiesGroups = enableActivitiesGroups;
    }

    @Column(name = "a_max")
    public Integer getActivitiesMax() {
        return activitiesMax;
    }

    public void setActivitiesMax(Integer activitiesMax) {
        this.activitiesMax = activitiesMax;
    }

    @Column(name = "a_gp_max")
    public Integer getActivitiesGroupsMax() {
        return activitiesGroupsMax;
    }

    public void setActivitiesGroupsMax(Integer activitiesGroupsMax) {
        this.activitiesGroupsMax = activitiesGroupsMax;
    }

    @Column(name = "a_per_gp_max")
    public Integer getActivitiesPerGroupMax() {
        return activitiesPerGroupMax;
    }

    public void setActivitiesPerGroupMax(Integer activitiesPerGroupMax) {
        this.activitiesPerGroupMax = activitiesPerGroupMax;
    }

    @Column(name = "a_per_er_max")
    public Integer getActivitiesPerExpectedResultMax() {
        return activitiesPerExpectedResultMax;
    }

    public void setActivitiesPerExpectedResultMax(Integer activitiesPerExpectedResultMax) {
        this.activitiesPerExpectedResultMax = activitiesPerExpectedResultMax;
    }

    @Column(name = "p_enable_groups")
    public Boolean getEnablePrerequisitesGroups() {
        return enablePrerequisitesGroups;
    }

    public void setEnablePrerequisitesGroups(Boolean enablePrerequisitesGroups) {
        this.enablePrerequisitesGroups = enablePrerequisitesGroups;
    }

    @Column(name = "p_max")
    public Integer getPrerequisitesMax() {
        return prerequisitesMax;
    }

    public void setPrerequisitesMax(Integer prerequisitesMax) {
        this.prerequisitesMax = prerequisitesMax;
    }

    @Column(name = "p_gp_max")
    public Integer getPrerequisitesGroupsMax() {
        return prerequisitesGroupsMax;
    }

    public void setPrerequisitesGroupsMax(Integer prerequisitesGroupsMax) {
        this.prerequisitesGroupsMax = prerequisitesGroupsMax;
    }

    @Column(name = "p_per_gp_max")
    public Integer getPrerequisitesPerGroupMax() {
        return prerequisitesPerGroupMax;
    }

    public void setPrerequisitesPerGroupMax(Integer prerequisitesPerGroupMax) {
        this.prerequisitesPerGroupMax = prerequisitesPerGroupMax;
    }
}
