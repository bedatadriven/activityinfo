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
 * Represents an item of the prerequisites list of a log frame.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_prerequisite")
public class Prerequisite implements Serializable {

    private static final long serialVersionUID = -3093621922617967414L;

    private Integer id;
    private String content;
    private LogFrame parentLogFrame;
    private LogFrameGroup group;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_prerequisite")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "content", length = 8192)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_log_frame", nullable = false)
    public LogFrame getParentLogFrame() {
        return parentLogFrame;
    }

    public void setParentLogFrame(LogFrame parentLogFrame) {
        this.parentLogFrame = parentLogFrame;
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
