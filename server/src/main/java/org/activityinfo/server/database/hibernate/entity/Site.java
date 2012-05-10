/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

/**
 * Concrete realization of
 *
 * 
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.Filters(
		{
@org.hibernate.annotations.Filter(
		name="userVisible",
		condition="(ActivityId in (select a.ActivityId from Activity a where a.DatabaseId in " +
				"(select d.DatabaseId from UserDatabase d where " +
					  "d.OwnerUserId = :currentUserId or " +
					  "d.DatabaseId in "  +
					  	"(select p.DatabaseId from UserPermission p where p.UserId = :currentUserId and p.AllowViewAll) or " +
					  "d.DatabaseId in " +
						"(select p.DatabaseId from UserPermission p where p.UserId = :currentUserId and p.AllowView and p.PartnerId = PartnerId))))"
),
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="DateDeleted is null"
)})
public class Site implements java.io.Serializable, Deleteable {

	private int id;
	private Project project;

	private Activity activity;
	private Location location;
	
	private String siteGuid;
	private Partner partner;
	
	private Date date1;
	private Date date2;
	private Date dateCreated;
	private Date dateEdited;
	private Date dateDeleted;
	private long timeEdited;

	private Date dateSynchronized;
	
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);
	private Set<ReportingPeriod> reportingPeriods = new HashSet<ReportingPeriod>(0);
	
	private String comments;
		

    public Site() {
		Date now = new Date();
		this.dateCreated = now;
		this.dateEdited = now;
	}

    /**
     *
     * @return the id of this Site
     */
	@Id
	@Column(name = "SiteId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

    /**
     * Sets the id of this Site
     */
	public void setId(int siteId) {
		this.id = siteId;
	}

    /**
     * @return the Activity to which this Site belongs
     */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ActivityId", nullable = false)
	public Activity getActivity() {
		return this.activity;
	}

    /**
     *
     * @param activity  the Activity to which this Site belongs
     */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

    /**
     *
     * @return the geographic Location of this Site
     */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LocationId", nullable = false)
	public Location getLocation() {
		return this.location;
	}

    /**
     * Sets the geographic Location of this Site
     */
	public void setLocation(Location location) {
		this.location = location;
	}

    /**
     * @return the Globally-Unique Identifier (GUID) for this Site, used to link this Site
     * to external systems
     */
	@Column(name = "SiteGuid", length = 36)
	public String getSiteGuid() {
		return this.siteGuid;
	}

    /**
     * Sets the Globally-Unique Identifier (GUID) of this Site. This GUID is optional but can be
     * used to link Site objects to external systems.
     *
     */
	public void setSiteGuid(String siteGuid) {
		this.siteGuid = siteGuid;
	}

    /**
     * Gets the OrgUnit who owns this Site. In some cases, the OrgUnit will
     * have been the actually implementer who produced the results (e.g. actually delivered
     *  the kits) but not necessarily: the meaning of partner is potentially more general.
     *
     * The only semantic meaning we enforce is that this is the OrgUnit
     * that owns the data and thus has control over its modification and visibility.
     *
     * @return the Partner who owns this Site
     */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PartnerId", nullable = false)
	public Partner getPartner() {
		return this.partner;
	}

    /**
     * Sets the Partner who owns this Site.
     */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}


    /**
     * @return  the date on which work at this Site began
     */
	@Temporal(TemporalType.DATE)
	@Column(name = "Date1", length = 23)
	public Date getDate1() {
		return this.date1;
	}

    /**
     * Sets the date on which work at this Site began
     */
	public void setDate1(Date date1) {
		this.date1 = date1;
	}

    /**
     *
     * @return the date on which work at this Site ended
     */
	@Temporal(TemporalType.DATE)
	@Column(name = "Date2", length = 23)
	public Date getDate2() {
		return this.date2;
	}

    /**
     * Sets the date on which work at this Site ended
     */
	public void setDate2(Date date2) {
		this.date2 = date2;
	}

    /**
     *
     * @return  the time at which this Site created. Used for synchronization.
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateCreated", nullable = false, length = 23)
	public Date getDateCreated() {
		return this.dateCreated;
	}

    /**
     * Sets the time at which this Site was created. Used for synchronisation.
     */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

    /**
     * @return the time at which this Site was last edited. Initially
     * equal to dateCreated.
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateEdited", nullable = false, length = 23)
	public Date getDateEdited() {
		return this.dateEdited;
	}

    /**
     * Sets the time at which this Site was last edited.
     */
	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
		this.timeEdited = dateEdited.getTime();
	}

    public long getTimeEdited() {
		return timeEdited;
	}

	public void setTimeEdited(long timeEdited) {
		this.timeEdited = timeEdited;
	}

	/**
     *
     * @return the plain-text comments associatedsetDateEdited with this Site
     */
	@Lob
	public String getComments() {
		return this.comments;
	}

    /**setDateEdited
     * Sets the plain-text comments associated with this Site
     */
	public void setComments(String comments) {
		this.comments = comments;
	}


    /**
     *
     * @return the time at which this Site was last synchronized with an external system
     */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateSynchronized", length = 23)
	public Date getDateSynchronized() {
		return this.dateSynchronized;
	}

    /**
     * Sets the time at which this Site was last synchronized with an external system. This field
     * is only used when an external system is used as the source of this Site's data
     */
	public void setDateSynchronized(Date dateSynchronized) {
		this.dateSynchronized = dateSynchronized;
	}
    /**
     * @return  the list of AttributeValues for this Site
     */
	@OneToMany(fetch=FetchType.LAZY, mappedBy="site")
	public Set<AttributeValue> getAttributeValues() {
		return attributeValues;
	}

    /**
     * Sets the AttributeValues for this Site
     */
	public void setAttributeValues(Set<AttributeValue> values) {
		attributeValues = values;
	}

    /**
     * Gets the ReportingPeriods for this Site. The number of ReportingPeriods depend on the
     * ReportingFrequency of this Site's Activity. The Sites of Activities with a ONCE ReportingFrequency will
     * have exactly one ReportingPeriod, while those with MONTHLY reporting will have zero or more
     * ReportingPeriods, one for each calendar month in which data is available.
     *
     * @return  the ReportingPeriods associated with this Site
     */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "site")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ReportingPeriod> getReportingPeriods() {
		return this.reportingPeriods;
	}

    /**
     * Sets the ReportingPeriods for this Site
     *
     * @param reportingPeriods
     */
	public void setReportingPeriods(Set<ReportingPeriod> reportingPeriods) {
		this.reportingPeriods = reportingPeriods;
	}

    /**
     *
     * @return the time at which this Site was deleted. Used for synchronization with clients.
     */
	@Column
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return this.dateDeleted;
	}

    /**
     * Sets the time at at which this Site was deleted.
     */
	public void setDateDeleted(Date date) {
		this.dateDeleted = date;
	}

    /**
     * Marks this Site as deleted.
     *
     * Note that deleted Sites are not physically removed from the database, they are retained to
     * allow for the possibility of undoing of catastrophic error as well as to retain a record
     * for synchronization with clients.
     */
	@Override
	public void delete() {
        Date now = new Date();
        setDateDeleted(now);
        setDateEdited(now);
        getActivity().getDatabase().setLastSchemaUpdate(new Date());
	}

    /**
     *
     * @return  true if this Site has been deleted
     */
	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() != null;
	}
        
	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ProjectId", nullable=true)
	public Project getProject() {
		return project;
	}
}
