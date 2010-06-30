/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
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
					  	"(select p.DatabaseId from UserPermission p where p.UserId = :currentUserId and p.AllowViewAll = 1) or " +
					  "d.DatabaseId in " +
						"(select p.DatabaseId from UserPermission p where p.UserId = :currentUserId and p.AllowView = 1 and p.PartnerId = PartnerId))))"
),
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="DateDeleted is null"
)})
public class Site implements java.io.Serializable, Deleteable {

	private int id;
    private Site assessment;
	
	private Activity activity;
	private Location location;
	
	private String siteGuid;
	private OrgUnit partner;
	private int status;
	
	private Date date1;
	private Date date2;
	private Date dateCreated;
	private Date dateEdited;
	private Date dateDeleted;

    private int target;

	private Date dateSynchronized;
	
	///private Map<Integer, Boolean> attributeValues = new HashMap<Integer, Boolean>(0);
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);
	

	private Set<ReportingPeriod> reportingPeriods = new HashSet<ReportingPeriod>(0);
	
	private String comments;
	
	private Set<Site> interventions = new HashSet<Site>(0);

    public Site() {
		Date now = new Date();
		setDateCreated(now);
		setDateEdited(now);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SiteId", unique = true, nullable = false)
	
	public int getId() {
		return this.id;
	}

	public void setId(int siteId) {
		this.id = siteId;
	}

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AssessmentSiteId")
	public Site getAssessment() {
		return this.assessment;
	}

	public void setAssessment(Site assessment) {
		this.assessment = assessment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ActivityId", nullable = false)
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LocationId", nullable = false)
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Column(name = "SiteGuid", length = 36)
	public String getSiteGuid() {
		return this.siteGuid;
	}

	public void setSiteGuid(String siteGuid) {
		this.siteGuid = siteGuid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PartnerId", nullable = false)
	public OrgUnit getPartner() {
		return this.partner;
	}

	public void setPartner(OrgUnit partner) {
		this.partner = partner;
	}

	@Column(name = "Status", nullable = false)
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
				
		this.status = status;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Date1", length = 23)
	public Date getDate1() {
		return this.date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Date2", length = 23)
	public Date getDate2() {
		return this.date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
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

	@Lob
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateSynchronized", length = 23)
	public Date getDateSynchronized() {
		return this.dateSynchronized;
	}

	public void setDateSynchronized(Date dateSynchronized) {
		this.dateSynchronized = dateSynchronized;
	}

	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="site")
	public Set<AttributeValue> getAttributeValues() {
		return attributeValues;
	}
	
	public void setAttributeValues(Set<AttributeValue> values) {
		attributeValues = values;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "site")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ReportingPeriod> getReportingPeriods() {
		return this.reportingPeriods;
	}

	public void setReportingPeriods(Set<ReportingPeriod> reportingPeriods) {
		this.reportingPeriods = reportingPeriods;
	}

	public ReportingPeriod primaryReportingPeriod() {
		for(ReportingPeriod period : getReportingPeriods()) {
			if(!period.isMonitoring()) {
				return period;
			}
		}
		return null;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "assessment")
	public Set<Site> getInterventions() {
		return this.interventions;
	}

	public void setInterventions(Set<Site> interventions) {
		this.interventions = interventions;
	}

	@Column
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return this.dateDeleted;
	}
	
	public void setDateDeleted(Date date) {
		this.dateDeleted = date;
	}
	
	public void delete() {
        Date now = new Date();
        setDateDeleted(now);
        setDateEdited(now);
	}

	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() == null;
	}


    /**
     *
     * @return The type of site: 0 for work complete, 1 for program targets
     */
    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
