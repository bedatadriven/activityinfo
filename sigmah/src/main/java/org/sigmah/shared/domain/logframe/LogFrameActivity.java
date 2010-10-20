package org.sigmah.shared.domain.logframe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents the activity of an expected result of a log frame.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_activity")
public class LogFrameActivity implements Serializable {

    private static final long serialVersionUID = -2247266774443718302L;

    private Integer id;
    private Integer code;
    private String content;
    private ExpectedResult parentExpectedResult;
    private LogFrameGroup group;

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

    @Column(name = "content", length = 8192)
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
}
