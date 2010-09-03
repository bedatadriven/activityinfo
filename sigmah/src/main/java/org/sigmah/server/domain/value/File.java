package org.sigmah.server.domain.value;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.sigmah.server.domain.Deleteable;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "file_meta")
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null") })
public class File implements Serializable, Deleteable {

    private static final long serialVersionUID = -271699094058979365L;

    private Long id;
    private String name;
    private Date removedDate;
    private List<FileVersion> versions = new ArrayList<FileVersion>();
    // Deletion informations.
    private Date dateDeleted;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_file")
    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false, length = 4096)
    public String getName() {
        return name;
    }

    public void setRemovedDate(Date removedDate) {
        this.removedDate = removedDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "removed_date", nullable = true)
    public Date getRemovedDate() {
        return removedDate;
    }

    public void setVersions(List<FileVersion> versions) {
        this.versions = versions;
    }

    @OneToMany(mappedBy = "parentFile", cascade = CascadeType.ALL)
    public List<FileVersion> getVersions() {
        return versions;
    }

    /**
     * Adds a new version of the current file.
     */
    public void addVersion(FileVersion version) {
        version.setParentFile(this);
        versions.add(version);
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
        return getDateDeleted() == null;
    }
}
