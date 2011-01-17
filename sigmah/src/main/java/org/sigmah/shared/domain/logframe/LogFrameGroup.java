package org.sigmah.shared.domain.logframe;

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
 * Represents a group of log frame elements displayed together.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "log_frame_group")
public class LogFrameGroup implements Serializable {

    private static final long serialVersionUID = 5457361875504176525L;

    private Integer id;
    private LogFrameGroupType type;
    private String label;
    private LogFrame parentLogFrame;

    /**
     * Duplicates this group.
     * @param parentLogFrame Log frame that will contains this group.
     * @return A copy of this group.
     */
    public LogFrameGroup copy(final LogFrame parentLogFrame) {
        final LogFrameGroup copy = new LogFrameGroup();
        copy.type = type;
        copy.label = label;
        copy.parentLogFrame = parentLogFrame;

        return copy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_group")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public LogFrameGroupType getType() {
        return type;
    }

    public void setType(LogFrameGroupType type) {
        this.type = type;
    }

    @Column(name = "label", columnDefinition = "TEXT")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_log_frame", nullable = false)
    public LogFrame getParentLogFrame() {
        return parentLogFrame;
    }

    public void setParentLogFrame(LogFrame parentLogFrame) {
        this.parentLogFrame = parentLogFrame;
    }
}
