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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Alex Bertram
 * 
 */
@Entity
@Table(name = "Partner")
public class Partner implements java.io.Serializable, SchemaElement {

    private static final long serialVersionUID = -5985734789552797994L;

    private int id;
    private String name;
    private String fullName;
    private Set<UserDatabase> databases = new HashSet<UserDatabase>(0);
    private Set<Target> targets = new HashSet<Target>(0);

    public Partner() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PartnerId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Name", nullable = false, length = 16)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FullName", length = 64)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return getName();
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PartnerInDatabase", joinColumns = { @JoinColumn(name = "PartnerId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "DatabaseId", nullable = false, updatable = false) })
    public Set<UserDatabase> getDatabases() {
        return this.databases;
    }

    public void setDatabases(Set<UserDatabase> databases) {
        this.databases = databases;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "partner")
    public Set<Target> getTargets() {
        return this.targets;
    }

    public void setTargets(Set<Target> targets) {
        this.targets = targets;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Partner)) {
            return false;
        }

        final Partner other = (Partner) obj;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
