package org.sigmah.shared.domain.logframe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.sigmah.shared.domain.Deleteable;

/**
 * Represents an item of the specific objectives of a log frame.<br/>
 * A specific objective contains a list of expected results.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_specific_objective")
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null") })
public class SpecificObjective implements Serializable, Deleteable {

    private static final long serialVersionUID = 7534655171979110984L;

    private Integer id;
    private Integer code;
    private String interventionLogic;
    private String risks;
    private String assumptions;
    private LogFrame parentLogFrame;
    private List<ExpectedResult> expectedResults = new ArrayList<ExpectedResult>();
    private LogFrameGroup group;
    private Date dateDeleted;
    private Integer position;

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

    @Column(name = "intervention_logic", columnDefinition = "TEXT")
    public String getInterventionLogic() {
        return interventionLogic;
    }

    public void setInterventionLogic(String interventionLogic) {
        this.interventionLogic = interventionLogic;
    }

    @Column(name = "risks", columnDefinition = "TEXT")
    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    @Column(name = "assumptions", columnDefinition = "TEXT")
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
    @Filter(name = "hideDeleted", condition = "DateDeleted is null")
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

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDateDeleted() {
        return this.dateDeleted;
    }

    public void setDateDeleted(Date date) {
        this.dateDeleted = date;
    }

    @Override
    public void delete() {
        setDateDeleted(new Date());
    }

    @Override
    @Transient
    public boolean isDeleted() {
        return getDateDeleted() != null;
    }

    @Column(name = "position")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
