package org.sigmah.shared.domain.logframe;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.sigmah.shared.domain.Deleteable;

/**
 * Represents the activity of an expected result of a log frame.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_activity")
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null") })
public class LogFrameActivity implements Serializable, Deleteable {

    private static final long serialVersionUID = -2247266774443718302L;

    private Integer id;
    private Integer code;
    private String content;
    private ExpectedResult parentExpectedResult;
    private LogFrameGroup group;
    private Date dateDeleted;
    private String title;
    private Date startDate;
    private Date endDate;
    private Integer position;

    /**
     * Duplicates this activity (omits its ID).
     * @param parentExpectedResult Expected result that will contains this copy.
     * @param groupMap Map of copied groups.
     * @return A copy of this activity.
     */
    public LogFrameActivity copy(final ExpectedResult parentExpectedResult, final Map<Integer, LogFrameGroup> groupMap) {
        final LogFrameActivity copy = new LogFrameActivity();
        copy.code = this.code;
        copy.content = this.content;
        copy.parentExpectedResult = parentExpectedResult;
        copy.group = groupMap.get(this.group.getId());
        copy.dateDeleted = this.dateDeleted;
        copy.title = this.title;
        copy.startDate = this.startDate;
        copy.endDate = this.endDate;
        copy.position = this.position;

        return copy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_activity")
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

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_result", nullable = false)
    public ExpectedResult getParentExpectedResult() {
        return parentExpectedResult;
    }

    public void setParentExpectedResult(ExpectedResult parentExpectedResult) {
        this.parentExpectedResult = parentExpectedResult;
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

    @Column(name = "title", columnDefinition = "TEXT")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "position")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
