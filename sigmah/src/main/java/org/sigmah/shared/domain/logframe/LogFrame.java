package org.sigmah.shared.domain.logframe;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.sigmah.shared.domain.Project;

/**
 * Represents the entire log frame of a project.<br/>
 * A log frame defines a main objective and contains a list of specific
 * objectives and a list of prerequisites.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame")
public class LogFrame implements Serializable {

    private static final long serialVersionUID = 3670543377662206665L;

    private Integer id;
    private LogFrameModel logFrameModel;
    private String title;
    private String mainObjective;
    private List<SpecificObjective> specificObjectives = new ArrayList<SpecificObjective>();
    private List<Prerequisite> prerequisites = new ArrayList<Prerequisite>();
    private Project parentProject;
    private List<LogFrameGroup> groups = new ArrayList<LogFrameGroup>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_log_frame")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_log_frame_model", nullable = false)
    public LogFrameModel getLogFrameModel() {
        return logFrameModel;
    }

    public void setLogFrameModel(LogFrameModel logFrameModel) {
        this.logFrameModel = logFrameModel;
    }

    @Column(name = "title", columnDefinition = "TEXT")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "main_objective", columnDefinition = "TEXT")
    public String getMainObjective() {
        return mainObjective;
    }

    public void setMainObjective(String mainObjective) {
        this.mainObjective = mainObjective;
    }

    @OneToMany(mappedBy = "parentLogFrame", cascade = CascadeType.ALL)
    @OrderBy(value = "code asc")
    @Filter(name = "hideDeleted", condition = "DateDeleted is null")
    public List<SpecificObjective> getSpecificObjectives() {
        return specificObjectives;
    }

    public void setSpecificObjectives(List<SpecificObjective> specificObjectives) {
        this.specificObjectives = specificObjectives;
    }

    @OneToMany(mappedBy = "parentLogFrame", cascade = CascadeType.ALL)
    @OrderBy(value = "code asc")
    @Filter(name = "hideDeleted", condition = "DateDeleted is null")
    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project", nullable = false)
    public Project getParentProject() {
        return parentProject;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    @OneToMany(mappedBy = "parentLogFrame", cascade = CascadeType.ALL)
    public List<LogFrameGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<LogFrameGroup> groups) {
        this.groups = groups;
    }
}
