/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class ReportingPeriod implements java.io.Serializable, Deleteable {

	private int id;
    private Site site;
	private Date date1;
	private Date date2;
	private String comments;
	
	private Date dateCreated;
	private Date dateEdited;
	private Date dateDeleted;
	
	private Set<IndicatorValue> indicatorValues = new HashSet<IndicatorValue>(0);
	
	public ReportingPeriod() {
		
		Date now = new Date();
		this.dateCreated = now;
		this.dateEdited = now; 
	}

    public ReportingPeriod(Site site) {
        this();
        setSite(site);
    }


    @Id
	@Column(name = "ReportingPeriodId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SiteId", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Date1", nullable = false, length = 23)
	public Date getDate1() {
		return this.date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Date2", nullable = false, length = 23)
	public Date getDate2() {
		return this.date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

    @Lob
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateCreated", nullable = false, length = 23)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateEdited", nullable = false, length = 23)
	public Date getDateEdited() {
		return this.dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}
	
	@OneToMany(mappedBy="reportingPeriod", fetch=FetchType.LAZY)
    @org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="(IndicatorId not in (select i.IndicatorId from indicator i where i.dateDeleted is not null))")
	public Set<IndicatorValue> getIndicatorValues() {
		return indicatorValues;
	}
	
	public void setIndicatorValues(Set<IndicatorValue> values) {
		this.indicatorValues = values;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this==other) {
            return true;
        }
		if(other==null) {
            return false;
        }
		if(!(other instanceof ReportingPeriod)) {
            return false;
        }
	
		ReportingPeriod that = (ReportingPeriod)other;
		
		return this.getId() == that.getId();
	}
	
	
	
	@Override
	public int hashCode() {
		return this.getId();
	}

	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	protected Date getDateDeleted() {
		return this.dateDeleted;
	}
	
	protected void setDateDeleted(Date date) {
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
