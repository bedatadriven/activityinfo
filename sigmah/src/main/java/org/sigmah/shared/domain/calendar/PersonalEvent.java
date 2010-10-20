/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.calendar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.sigmah.shared.domain.Deleteable;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
@org.hibernate.annotations.FilterDefs({ @org.hibernate.annotations.FilterDef(name = "hideDeleted") })
@org.hibernate.annotations.Filters({ @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "dateDeleted IS null") })
public class PersonalEvent implements Serializable, Deleteable {
    /**
     * Event identifier.
     */
    private Integer id;

    /**
     * Identifier of the parent calendar of this event.
     */
    private Integer calendarId;
    
    /**
     * Title of the event (a short description).
     */
    private String summary;
    /**
     * Body of the event.
     */
    private String description;
    /**
     * Start date of the event.
     */
    private Date startDate;
    /**
     * End date of the event.
     */
    private Date endDate;
    /**
     * Creation date of the event.
     */
    private Date dateCreated;
    /**
     * Date of deletion.
     */
    private Date dateDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Temporal(value=TemporalType.TIMESTAMP)
    public Date getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    @Override
    public void delete() {
        dateDeleted = new Date();
    }

    @Override
    @Transient
    public boolean isDeleted() {
        return dateDeleted != null;
    }
}
