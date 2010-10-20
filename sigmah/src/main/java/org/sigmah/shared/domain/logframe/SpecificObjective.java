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

/**
 * Represents an item of the specific objectives of a log frame.<br/>
 * A specific objective contains a list of expected results.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_specific_objective")
public class SpecificObjective implements Serializable {

    private static final long serialVersionUID = 7534655171979110984L;

    private Integer id;
    private Integer code;
    private String interventionLogic;
    private String risks;
    private String assumptions;
    private LogFrame parentLogFrame;
    private List<ExpectedResult> expectedResults = new ArrayList<ExpectedResult>();
    private LogFrameGroup group;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_objective")
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
    @JoinColumn(name = "id_log_frame", nullable = false)
    public LogFrame getParentLogFrame() {
        return parentLogFrame;
    }

    public void setParentLogFrame(LogFrame parentLogFrame) {
        this.parentLogFrame = parentLogFrame;
    }

    @OneToMany(mappedBy = "parentSpecificObjective", cascade = CascadeType.ALL)
    @OrderBy(value = "code asc")
    public List<ExpectedResult> getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(List<ExpectedResult> expectedResults) {
        this.expectedResults = expectedResults;
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
