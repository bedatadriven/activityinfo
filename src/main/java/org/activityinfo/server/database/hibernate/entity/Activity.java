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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.activityinfo.shared.dto.Published;

/**
 * After the
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}, the
 * activity is the second level of organization in ActivityInfo. Each activity
 * has its set of indicators and attributes.
 * 
 * Realized activities takes place at
 * {@link org.activityinfo.server.database.hibernate.entity.Site} sites.
 * 
 * @author Alex Bertram
 */
@Entity
@org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null")
@NamedQuery(name = "queryMaxSortOrder", query = "select max(e.sortOrder) from Activity e where e.database.id = ?1")
public class Activity implements Serializable, Deleteable, Orderable {

    private int id;
    private LocationType locationType;

    private UserDatabase database;
    private String name;
    private String category;

    private int reportingFrequency;

    private boolean allowEdit;
    private int sortOrder;

    private Date dateDeleted;

    private Set<Indicator> indicators = new HashSet<Indicator>(0);

    private Set<Site> sites = new HashSet<Site>(0);
    private Set<AttributeGroup> attributeGroups = new HashSet<AttributeGroup>(0);
    private Set<LockedPeriod> lockedPeriods = new HashSet<LockedPeriod>();

    private String mapIcon;

    private int published = Published.NOT_PUBLISHED.getIndex();

    public Activity() {

    }

    public Activity(int id, String name) {
        this.id = id;
        this.name = name;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "AllowEdit", nullable = false)
    public boolean isAllowEdit() {
        return this.allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    @Override
    @Column(name = "SortOrder", nullable = false)
    public int getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "AttributeGroupInActivity", joinColumns = { @JoinColumn(name = "ActivityId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "AttributeGroupId", nullable = false, updatable = false) })
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null")
    public Set<AttributeGroup> getAttributeGroups() {
        return this.attributeGroups;
    }

    public void setAttributeGroups(Set<AttributeGroup> attributeGroups) {
        this.attributeGroups = attributeGroups;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
    @org.hibernate.annotations.OrderBy(clause = "sortOrder")
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Filter(name = "hideDeleted", condition = "DateDeleted is null")
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
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDateDeleted() {
        return this.dateDeleted;
    }

    public void setDateDeleted(Date date) {
        this.dateDeleted = date;
    }

    @Override
    public void delete() {
        setDateDeleted(new Date());
        getDatabase().setLastSchemaUpdate(new Date());
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

    @Column(length = 255, nullable = true)
    public String getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(String mapIcon) {
        this.mapIcon = mapIcon;
    }

    public void setLockedPeriods(Set<LockedPeriod> lockedPeriods) {
        this.lockedPeriods = lockedPeriods;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
    public Set<LockedPeriod> getLockedPeriods() {
        return lockedPeriods;
    }

    // the rebar synchronization library does not yet support enums :-(
    @Column(name = "published")
    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }
}
