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

import org.sigmah.shared.domain.value.File;

@Entity
@Table(name = "monitored_point")
public class MonitoredPoint implements Serializable {

    private static final long serialVersionUID = 3600773298461293280L;

    private Integer id;
    private String label;
    private Date expectedDate;
    private Date completionDate;
    private File file;
    private MonitoredPointList parentList;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_monitored_point")
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

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_file", nullable = true)
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_list", nullable = false)
    public MonitoredPointList getParentList() {
        return parentList;
    }

    public void setParentList(MonitoredPointList parentList) {
        this.parentList = parentList;
    }

    @Transient
    public boolean isCompleted() {
        return completionDate != null;
    }
}
