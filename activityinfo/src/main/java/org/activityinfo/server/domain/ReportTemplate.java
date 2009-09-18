package org.activityinfo.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;

@Entity
@org.hibernate.annotations.Filters(
		{@org.hibernate.annotations.Filter(
				name="userVisible",
				condition="(:currentUserId = OwnerUserId or " + 
						  "(Visibility = 1 and (DatabaseId is null or " +
						  ":currentUserId in (select p.UserId from UserPermission p " +
						  						"where p.AllowView=1 and p.UserId=:currentUserId and p.DatabaseId=DatabaseId))))"
		),
		
		@org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="DateDeleted is null"
		)}
)
public class ReportTemplate implements Serializable {

	private int id;
	private User owner;
	private UserDatabase database;
	private int visibility;
	private String xml;
	private int version;
	private Date dateDeleted;
    private Set<ReportSubscription> subscriptions = new HashSet<ReportSubscription>(0);
	
	public ReportTemplate(){
		
	}

    @Id
	@Column(name="ReportTemplateId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OwnerUserId", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DatabaseId", nullable = true)
	public UserDatabase getDatabase() {
		return database;
	}

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}

	@Column
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	@Column(nullable=false)
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ReportSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<ReportSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Version
	@Column
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return dateDeleted;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}
}
