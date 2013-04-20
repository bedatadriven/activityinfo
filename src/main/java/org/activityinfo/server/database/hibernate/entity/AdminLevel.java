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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@JsonAutoDetect(JsonMethod.NONE)
public class AdminLevel implements java.io.Serializable {

    private int id;
    private Country country;
    private AdminLevel parent;
    private String name;
    private boolean polygons;
    private Set<AdminEntity> entities = new HashSet<AdminEntity>(0);
    private Set<AdminLevel> childLevels = new HashSet<AdminLevel>(0);
    private Set<LocationType> boundLocationTypes = new HashSet<LocationType>(0);
    private int version;
    private boolean deleted;

    public AdminLevel() {
    }

    public AdminLevel(int adminLevelId, Country country, String name) {
        this.id = adminLevelId;
        this.country = country;
        this.name = name;
    }

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AdminLevelId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CountryId", nullable = false)
    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    public AdminLevel getParent() {
        return this.parent;
    }
    
    @Transient
    @JsonProperty
    public Integer getParentId() {
        if (getParent() == null) {
            return null;
        } else {
            return getParent().getId();
        }
    }

    public void setParent(AdminLevel adminLevel) {
        this.parent = adminLevel;
    }

    @JsonProperty
    @Column(name = "Name", nullable = false, length = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public boolean isPolygons() {
        return polygons;
    }

    public void setPolygons(boolean polygons) {
        this.polygons = polygons;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "level")
    public Set<AdminEntity> getEntities() {
        return this.entities;
    }
    

    public void setEntities(Set<AdminEntity> entities) {
        this.entities = entities;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "boundAdminLevel")
    public Set<LocationType> getBoundLocationTypes() {
        return this.boundLocationTypes;
    }
    
    public void setBoundLocationTypes(Set<LocationType> locationTypes) {
        this.boundLocationTypes = locationTypes;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<AdminLevel> getChildLevels() {
        return this.childLevels;
    }

    public void setChildLevels(Set<AdminLevel> childLevels) {
        this.childLevels = childLevels;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
