package org.sigmah.shared.domain.value;

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
import javax.persistence.UniqueConstraint;

import org.sigmah.shared.domain.Deleteable;
import org.sigmah.shared.domain.User;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "file_version", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_file", "version_number" }) })
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null") })
public class FileVersion implements Serializable, Deleteable {

    private static final long serialVersionUID = -1143785858180618602L;

    // Use an Integer as identifier to be compatible with the EntityDTO
    // interface and the command handlers.
    private Integer id;
    private File parentFile;
    private Integer versionNumber;
    private String path;
    private User author;
    private Date addedDate;
    private Long size;
    private String comments;
    private String name;
    private String extension;
    // Deletion informations.
    private Date dateDeleted;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_file_version")
    public Integer getId() {
        return id;
    }

    public void setParentFile(File parentFile) {
        this.parentFile = parentFile;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_file", nullable = false)
    public File getParentFile() {
        return parentFile;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name = "version_number", nullable = false)
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "path", nullable = false, length = 4096)
    public String getPath() {
        return path;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_author", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date", nullable = false)
    public Date getAddedDate() {
        return addedDate;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name = "size", nullable = false)
    public Long getSize() {
        return size;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "comments", length = 4096, nullable = true)
    public String getComments() {
        return comments;
    }

    @Column(name = "name", nullable = false, length = 4096)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "extension", nullable = true, length = 16)
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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
}
