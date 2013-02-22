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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Project implements Serializable {
    private int id;
    private String name;
    private String description;
    private Date dateDeleted;
    private UserDatabase userDatabase;
    private Set<LockedPeriod> lockedPeriods = new HashSet<LockedPeriod>();
    private Set<Target> targets = new HashSet<Target>(0);

    public Project() {
        super();
    }

    public Project(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ProjectId", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public Date getDateDeleted() {
        return dateDeleted;
    }

    public void setUserDatabase(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DatabaseId", nullable = false)
    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public void setLockedPeriods(Set<LockedPeriod> lockedPeriods) {
        this.lockedPeriods = lockedPeriods;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "project")
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    public Set<LockedPeriod> getLockedPeriods() {
        return lockedPeriods;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
    public Set<Target> getTargets() {
        return this.targets;
    }

    public void setTargets(Set<Target> targets) {
        this.targets = targets;
    }
}