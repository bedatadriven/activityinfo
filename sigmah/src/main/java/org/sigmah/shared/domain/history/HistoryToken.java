package org.sigmah.shared.domain.history;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;

/**
 * Represents an history value.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "history_token")
public class HistoryToken implements Serializable {

    private static final long serialVersionUID = -4117487522284514885L;

    private Integer id;
    private Long elementId;
    private Integer projectId;
    private Date date;
    private String value;
    private User user;
    private ChangeType type;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_history_token")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "id_element", nullable = false)
    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    @Column(name = "id_project", nullable = false)
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Column(name = "history_date", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_user", nullable = true)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "change_type", nullable = true)
    @Enumerated(value = EnumType.STRING)
    public ChangeType getType() {
        return type;
    }

    public void setType(ChangeType type) {
        this.type = type;
    }
}
