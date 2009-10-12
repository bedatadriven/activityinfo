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

/**
 * After the {@link org.activityinfo.server.domain.UserDatabase}, the activity is the second level of organization in ActivityInfo.
 * Each activity has its set of indicators and attributes.
 *
 * Realized activities takes place at {@link org.activityinfo.server.domain.Site} sites.
 *
 *
 * @author Alex Bertram
 */
@Entity
@org.hibernate.annotations.Filter(
	name="hideDeleted",
	condition="DateDeleted is null"
)
public class Activity implements Serializable, Deleteable, Orderable, SchemaElement {

	private int id;
    private LocationType locationType;
	
	private UserDatabase database;
	private String name;
	private String category;
	
	private int reportingFrequency;
	
	private boolean assessment;
	private boolean allowEdit;
	private int sortOrder;
	
	private Date dateDeleted;
	
	private Set<Indicator> indicators = new HashSet<Indicator>(0);
	
	private Set<Site> sites = new HashSet<Site>(0);
	private Set<AttributeGroup> attributeGroups = new HashSet<AttributeGroup>(0);

    private String mapIcon;

    public Activity() {
	
	}

	public Activity(int id, String name) {
		this.id = id;
		this.name = name;
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ActivityId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LocationTypeId", nullable = false)
	public LocationType getLocationType() {
		return this.locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DatabaseId", nullable = false)
	public UserDatabase getDatabase() {
		return this.database;
	}

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}

	@Column(name = "Name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ReportingFrequency", nullable = false)
	public int getReportingFrequency() {
		return this.reportingFrequency;
	}

	public void setReportingFrequency(int reportingFrequency) {
		this.reportingFrequency = reportingFrequency;
	}

	@Column(name = "IsAssessment", nullable = false)
	public boolean isAssessment() {
		return this.assessment;
	}

	public void setAssessment(boolean assessment) {
		this.assessment = assessment;
	}

	@Column(name = "AllowEdit", nullable = false)
	public boolean isAllowEdit() {
		return this.allowEdit;
	}

	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}

	@Column(name = "SortOrder", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "AttributeGroupInActivity", 
			joinColumns = { 
				@JoinColumn(name = "ActivityId", nullable = false, updatable = false) }, 
			inverseJoinColumns = { 
				@JoinColumn(name = "AttributeGroupId", nullable = false, updatable = false) })
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	@org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="DateDeleted is null")
	public Set<AttributeGroup> getAttributeGroups() {
		return this.attributeGroups;
	}

	public void setAttributeGroups(Set<AttributeGroup> attributeGroups) {
		this.attributeGroups = attributeGroups;
	}
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
	@org.hibernate.annotations.OrderBy(clause="sortOrder")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	@org.hibernate.annotations.Filter(name="hideDeleted", condition="DateDeleted is null")
	public Set<Indicator> getIndicators() {
		return this.indicators;
	}
	
	public void setIndicators(Set<Indicator> indicators) {
		this.indicators = indicators;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
	public Set<Site> getSites() {
		return this.sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return this.dateDeleted;
	}
	
	public void setDateDeleted(Date date) { 
		this.dateDeleted = date;
	}
	
	public void delete() {
		setDateDeleted(new Date());
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() == null;
	}

    @Column(length=255, nullable=true)
    public String getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(String mapIcon) {
        this.mapIcon = mapIcon;
    }
}
