package org.sigmah.shared.domain.reminder;

import java.io.Serializable;
import java.util.Date;

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

@Entity
@Table(name = "reminder")
public class Reminder implements Serializable {

    private static final long serialVersionUID = 2360748872630231054L;

    private Integer id;
    private String label;
    private Date expectedDate;
    private Date completionDate;
    private ReminderList parentList;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reminder")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "label", length = 8192, nullable = false)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "expected_date", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    @Column(name = "completion_date", nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_list", nullable = false)
    public ReminderList getParentList() {
        return parentList;
    }

    public void setParentList(ReminderList parentList) {
        this.parentList = parentList;
    }

    @Transient
    public boolean isCompleted() {
        return completionDate != null;
    }
}
