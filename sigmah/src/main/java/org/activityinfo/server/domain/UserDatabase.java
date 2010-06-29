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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * The UserDatabase is the broadest unit of organization within ActivityInfo.
 * Individual databases each has an owner who controls completely the activities,
 * indicators, partner organizations and the rights of other users to view, edit,
 * and design the database.
 * 
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.FilterDefs(
		{
@org.hibernate.annotations.FilterDef(
		name="userVisible",
		parameters = {
				@org.hibernate.annotations.ParamDef(
						name = "currentUserId", type="int"
				)
		}
),
@org.hibernate.annotations.FilterDef(
		name="hideDeleted"
)
		})
@org.hibernate.annotations.Filters(
		{@org.hibernate.annotations.Filter(
				name="userVisible",
				condition="(:currentUserId = OwnerUserId or " +
						  ":currentUserId in (select p.UserId from UserPermission p " +
						  						"where p.AllowView=1 and p.UserId=:currentUserId and p.DatabaseId=DatabaseId))"
		),

		@org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="DateDeleted is null"
		)}
)
@NamedQuery(name="queryAllUserDatabasesAlphabetically",
        query="select db from UserDatabase db order by db.name")
public class UserDatabase implements java.io.Serializable, Deleteable, SchemaElement {

	private int id;
    private Country country;
	private Date startDate;
	private String fullName;
	private String name;
	private User owner;
	private Set<Partner> partners = new HashSet<Partner>(0);
	private Set<Activity> activities = new HashSet<Activity>(0);
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>(0);
	private Date dateDeleted;
    private Date lastSchemaUpdate;
	
	public UserDatabase() {
	}

	public UserDatabase(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "DatabaseId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

    /**
     * At present, each database can contain data on activities that take place in one
     * and only one country.
     *
     * TODO: nullable? many-to-many?
     *
     * @return The country assocatited with this database.
     */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CountryId", nullable = false)
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

    /**
     *
     * @return The date on which the activities defined by this database started.
     * I.e. provides a minimum bound for the dates of activities.
     */
	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate", length = 23)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

    /**
     *
     * @return The full name of the database
     */
	@Column(name = "FullName", length = 50)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

    /**
     *
     * @return  The short name of the database (generally an
     * acronym)
     */
	@Column(name = "Name", length = 16, nullable=false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    /**
     *
     * @return The user who owns this database
     */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "OwnerUserId", nullable = false)
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

    /**
     *
     * // TODO transform to link to Office entity
     *
     * @return  The list of partner organizations involved in this database.
     * (Partner organizations can own activity sites)
     */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "PartnerInDatabase",
            joinColumns = {
                    @JoinColumn(name = "DatabaseId", nullable = false, updatable = false) },
            inverseJoinColumns = {
                    @JoinColumn(name = "PartnerId", nullable = false, updatable = false) })
	public Set<Partner> getPartners() {
		return this.partners;
	}

	public void setPartners(Set<Partner> partners) {
		this.partners = partners;
	}

    /**
     *
     * @return The list of activities followed by this database
     */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "database")
    @org.hibernate.annotations.OrderBy(clause="sortOrder")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Filter(
            name="hideDeleted",
            condition="DateDeleted is null")
	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}


    /**
     *
     * @return The list of users who have access to this database and their respective
     * permissions. (Read, write, read all partners)
     */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "database")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<UserPermission> getUserPermissions() {
		return this.userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> userPermissions) {
		this.userPermissions = userPermissions;
	}

    /**
     *
     * @param user
     * @return True if the given user has the right to view this database at all.
     */
	public boolean isAllowedView(User user) {
		if(getOwner().getId() == user.getId() || getOwner().equals(user)) {
            return true;
        }
		
		UserPermission permission = this.getPermissionByUser(user);
        return permission != null && permission.isAllowView();
    }

    /**
     *
     * @param user
     * @return  True if the given user has the right to view data from all partners in this database.
     * False if they have only the right to view the data from their partner organization
     */
	public boolean isAllowedViewAll(User user) {
		if(getOwner().getId() == user.getId() || getOwner().equals(user)) {
            return true;
        }
		
		UserPermission permission = this.getPermissionByUser(user);
        return permission != null && permission.isAllowViewAll();

    }


    /**
     *
     * @param user
     * @return  True if the given user has the right to create or modify sites on behalf of their
     * (partner) organization
     */
	public boolean isAllowedEdit(User user) {
		if(getOwner().getId() == user.getId()) {
            return true;
        }
		
		UserPermission permission = this.getPermissionByUser(user);
        return permission != null && permission.isAllowEdit();

    }

    /**
     *
     * @param user
     * @return  True if the given user has the right to modify the definition of the database,
     * such as adding or removing activities, indicators, etc
     */
    public boolean isAllowedDesign(User user) {
		if(getOwner().getId() == user.getId()) {
            return true;
        }
		
		UserPermission permission = this.getPermissionByUser(user);
        return permission != null && permission.isAllowDesign();
    }

    public boolean isAllowedManageUsers(User user, Partner partner) {
        if(getOwner().getId() == user.getId()) {
            return true;
        }

		UserPermission permission = this.getPermissionByUser(user);
        if(permission == null) {
            return false;
        }
        if(!permission.isAllowManageUsers()) {
            return false;
        }
        if(!permission.isAllowManageAllUsers() && permission.getPartner().getId() != partner.getId()) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param user
     * @return The permission descriptor for the given user, or null if this user has no
     * rights to this database.
     */
	public UserPermission getPermissionByUser(User user) {
		for(UserPermission perm : this.getUserPermissions()) {
			if(perm.getUser().getId() == user.getId() ||
			   perm.getUser().equals(user) ) {
				return perm;
			}
		}
		return null;
	}

    /**
     *
      * @param user
     * @return True if the given user has the right to create and modify sites on behalf of all
     * partner organizations.
     */
	public boolean isAllowedEditAll(User user) {
		if(getOwner().getId() == user.getId()) {
            return true;
        }
		
		UserPermission permission = this.getPermissionByUser(user);
        return permission != null && permission.isAllowEditAll();

    }

    /**
     *
     * @return  The date on which this database was deleted by the user, or null if this
     * database is not deleted.
     */
	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
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
        setLastSchemaUpdate(now);
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

    /**
     * Gets the timestamp on which structure of the database (activities, indicators, etc)
     * was last modified.
     *
     * @return The timestamp on which the structure of the database was last modified.
     */
    @Column(nullable = false)
    public Date getLastSchemaUpdate() {
        return lastSchemaUpdate;
    }

    /**
     * Sets the timestamp on which the structure of the database (activities, indicateurs, etc
     * was last modified.
     * 
     * @param lastSchemaUpdate
     */
    public void setLastSchemaUpdate(Date lastSchemaUpdate) {
        this.lastSchemaUpdate = lastSchemaUpdate;
    }

}
