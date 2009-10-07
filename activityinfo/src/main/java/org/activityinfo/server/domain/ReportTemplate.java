/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    @Lob
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
