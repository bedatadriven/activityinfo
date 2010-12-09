package org.sigmah.shared.domain.value;

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

import org.hibernate.annotations.Filter;
import org.sigmah.shared.domain.Deleteable;

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

    private Integer id;
    private String name;
    private List<FileVersion> versions = new ArrayList<FileVersion>();
    // Deletion informations.
    private Date dateDeleted;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_file")
    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    public String getName() {
        return name;
    }

    public void setVersions(List<FileVersion> versions) {
        this.versions = versions;
    }

    @OneToMany(mappedBy = "parentFile", cascade = CascadeType.ALL)
    @Filter(name = "hideDeleted", condition = "DateDeleted is null")
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
        return getDateDeleted() != null;
    }

    /**
     * Returns the last version (with the higher version number).
     * 
     * @return the last version.
     */
    @Transient
    public FileVersion getLastVersion() {

        if (versions == null || versions.isEmpty()) {
            return null;
        }

        // Searches the max version number which identifies the last version.
        int index = 0;
        int maxVersionNumber = versions.get(index).getVersionNumber();
        for (int i = 1; i < versions.size(); i++) {
            if (versions.get(i).getVersionNumber() > maxVersionNumber) {
                index = i;
            }
        }

        return versions.get(index);
    }
}
