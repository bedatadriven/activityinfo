package org.sigmah.shared.domain.logframe;

import java.io.Serializable;
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

/**
 * Represents an item of the expected results of a specific objective of a log
 * frame.<br/>
 * An expected result contains one activity.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_expected_result")
public class ExpectedResult implements Serializable {

    private static final long serialVersionUID = -4913269192377942381L;

    private Integer id;
    private Integer code;
    private String interventionLogic;
    private String risks;
    private String assumptions;
    private SpecificObjective parentSpecificObjective;
    private List<LogFrameActivity> activities;
    private LogFrameGroup group;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_result")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "code", nullable = false)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Column(name = "intervention_logic", length = 8192)
    public String getInterventionLogic() {
        return interventionLogic;
    }

    public void setInterventionLogic(String interventionLogic) {
        this.interventionLogic = interventionLogic;
    }

    @Column(name = "risks", length = 8192)
    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    @Column(name = "assumptions", length = 8192)
    public String getAssumptions() {
        return assumptions;
    }

    public void setAssumptions(String assumptions) {
        this.assumptions = assumptions;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_specific_objective", nullable = false)
    public SpecificObjective getParentSpecificObjective() {
        return parentSpecificObjective;
    }

    public void setParentSpecificObjective(SpecificObjective parentSpecificObjective) {
        this.parentSpecificObjective = parentSpecificObjective;
    }

    @OneToMany(mappedBy = "parentExpectedResult", cascade = CascadeType.ALL)
    @OrderBy(value = "code asc")
    public List<LogFrameActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<LogFrameActivity> activities) {
        this.activities = activities;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_group", nullable = true)
    public LogFrameGroup getGroup() {
        return group;
    }

    public void setGroup(LogFrameGroup group) {
        this.group = group;
    }
}
