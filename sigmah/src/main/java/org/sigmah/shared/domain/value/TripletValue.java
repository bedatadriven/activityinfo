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
@org.hibernate.annotations.FilterDefs({
    @org.hibernate.annotations.FilterDef(name = "hideDeleted")
})
@org.hibernate.annotations.Filters({
    @org.hibernate.annotations.Filter(name = "hideDeleted",
    condition = "DateDeleted is null")
})
@Table(name = "triplet_value")
public class TripletValue implements Serializable, Deleteable, ListElementItem {

    private static final long serialVersionUID = -6149053567281316649L;
    private Long id;
    private Long idList;
    private String code;
    private String name;
    private String period;
    private Date dateDeleted;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_triplet")
    public Long getId() {
        return id;
    }

    @Column(name = "id_triplet_list", nullable = false)
    public Long getIdList() {
        return idList;
    }

    public void setIdList(Long idList) {
        this.idList = idList;
    }

    @Column(name = "code", nullable = false, length = 1024)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name", nullable = false, length = 4096)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "period", nullable = false, length = 1024)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     *
     * @return  The date on which this database was deleted by the user, or null if this
     * database is not deleted.
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
     * Marks this database as deleted. (Though the row is not removed from the database)
     */
    public void delete() {
        Date now = new Date();
        setDateDeleted(now);
    }

    /**
     *
     * @return  True if this database was deleted by its owner.
     */
    @Override
    @Transient
    public boolean isDeleted() {
        return getDateDeleted() == null;
    }

    @Override
    public boolean isLike(ListElementItem e) {
        if (e == null) {
            return false;
        }
        if (getClass() != e.getClass()) {
            return false;
        }
        final TripletValue other = (TripletValue) e;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.period == null) ? (other.period != null) : !this.period.equals(other.period)) {
            return false;
        }
        return true;
    }
}
