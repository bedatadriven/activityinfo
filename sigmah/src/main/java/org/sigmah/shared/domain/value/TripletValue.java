package org.sigmah.shared.domain.value;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.sigmah.shared.domain.Deleteable;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null") })
@Table(name = "triplet_value")
public class TripletValue implements Serializable, Deleteable, ListEntity {

    private static final long serialVersionUID = -6149053567281316649L;
    private Long id;
    private String code;
    private String name;
    private String period;
    private Date dateDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_triplet")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "code", nullable = false, columnDefinition = "TEXT")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "period", nullable = false, columnDefinition = "TEXT")
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * 
     * @return The date on which this database was deleted by the user, or null
     *         if this database is not deleted.
     */
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDateDeleted() {
        return this.dateDeleted;
    }

    protected void setDateDeleted(Date date) {
        this.dateDeleted = date;
    }

    /**
     * Marks this database as deleted. (Though the row is not removed from the
     * database)
     */
    public void delete() {
        setDateDeleted(new Date());
    }

    /**
     * 
     * @return True if this database was deleted by its owner.
     */
    @Override
    @Transient
    public boolean isDeleted() {
        return getDateDeleted() != null;
    }
}
