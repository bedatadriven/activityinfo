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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Alex Bertram
 */
@Entity
@Table(name = "AdminEntity")
@JsonAutoDetect(JsonMethod.NONE)
public class AdminEntity implements java.io.Serializable {

    private int id;
    private AdminLevel level;
    private AdminEntity parent;
    private String name;
    private String soundex;
    private String code;
    private Bounds bounds;
    private boolean deleted;

    private Set<Location> locations = new HashSet<Location>(0);
    private Set<AdminEntity> children = new HashSet<AdminEntity>(0);
    private Set<Target> targets = new HashSet<Target>(0);
    
    private Geometry geometry;

    public AdminEntity() {
    }

    public AdminEntity(int id, AdminLevel adminLevel, String name) {
        this.id = id;
        this.level = adminLevel;
        this.name = name;
    }

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AdminEntityId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int adminEntityId) {
        this.id = adminEntityId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AdminLevelId", nullable = false)
    public AdminLevel getLevel() {
        return this.level;
    }

    public void setLevel(AdminLevel level) {
        this.level = level;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "LocationAdminLink",
        joinColumns = {
            @JoinColumn(name = "AdminEntityId", nullable = false, updatable = false) },
        inverseJoinColumns = {
            @JoinColumn(name = "LocationId", nullable = false, updatable = false) }
        )
        public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "AdminEntityParentId", nullable = true)
    public AdminEntity getParent() {
        return this.parent;
    }

    public void setParent(AdminEntity parent) {
        this.parent = parent;
    }

    @JsonProperty
    @Transient
    public Integer getParentId() {
        return parent == null ? null : parent.getId();
    }

    @JsonProperty
    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Soundex", length = 50)
    public String getSoundex() {
        return this.soundex;
    }

    public void setSoundex(String soundex) {
        this.soundex = soundex;
    }

    @JsonProperty
    @Column(name = "Code", length = 15)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Embedded
    @JsonProperty
    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<AdminEntity> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AdminEntity> children) {
        this.children = children;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "adminEntity")
    public Set<Target> getTargets() {
        return this.targets;
    }

    public void setTargets(Set<Target> targets) {
        this.targets = targets;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
  
    @Basic(fetch=FetchType.LAZY)
    @Type(type="org.hibernatespatial.GeometryUserType")
    public Geometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return getName() + "[id=" + getId() + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof AdminEntity)) {
            return false;
        }

        AdminEntity that = (AdminEntity) other;

        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
