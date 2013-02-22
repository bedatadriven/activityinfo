

package org.activityinfo.server.database.hibernate.entity;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.activityinfo.shared.report.model.EmailDelivery;

/**
 * Defines a Report and its subscriptions.
 *
 * @see org.activityinfo.shared.report.model.Report
 *
 */
@Entity
@Table(name="ReportTemplate")
@org.hibernate.annotations.Filters(
		{@org.hibernate.annotations.Filter(
				name="userVisible",
				condition="(:currentUserId = OwnerUserId or " + 
						  "(Visibility = 1 and (DatabaseId is null or " +
						  ":currentUserId in (select p.UserId from userpermission p " +
						  						"where p.AllowView and p.UserId=:currentUserId and p.DatabaseId=DatabaseId))))"
		),
		
		@org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="DateDeleted is null"
		)}
)
public class ReportDefinition implements Serializable {

	private int id;
	private User owner;
	private UserDatabase database;
	private int visibility;
	private String xml;
    private Date dateDeleted;
    private String title;
    private String description;
    private EmailDelivery frequency;
    private Integer day;
    private Set<ReportSubscription> subscriptions = new HashSet<ReportSubscription>(0);
    private String json;
    
	public ReportDefinition(){
		
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
	@JoinColumn(name = "DatabaseId", nullable = true, updatable = false)
	public UserDatabase getDatabase() {
		return database;
	}

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

    @Lob
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

    @Column(nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return dateDeleted;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}
	
	@Lob
	@Column(nullable=true)
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
