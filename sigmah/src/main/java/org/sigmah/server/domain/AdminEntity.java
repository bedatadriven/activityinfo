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

package org.sigmah.server.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Bertram
 */
@Entity
@Table(name = "AdminEntity")
public class AdminEntity implements java.io.Serializable {

    private int id;
    private AdminLevel level;
    private AdminEntity parent;
    private String name;
    private String soundex;
    private String code;
    private Bounds bounds;

    private Set<Location> locations = new HashSet<Location>(0);
    private Set<AdminEntity> children = new HashSet<AdminEntity>(0);

    public AdminEntity() {
    }

    public AdminEntity(int id, AdminLevel adminLevel, String name) {
        this.id = id;
        this.level = adminLevel;
        this.name = name;
    }


    @Id
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
                    @JoinColumn(name = "AdminEntityId", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "LocationId", nullable = false, updatable = false)}
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

    @Column(name = "Code", length = 15)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Embedded
    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }


//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "adminEntities")
//	public Set<Location> getLocations() {
//		return this.locations;
//	}
//
//	public void setLocations(Set<Location> locations) {
//		this.locations = locations;
//	}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<AdminEntity> getChildren() {
        return this.children;
    }

    public void setChildren(Set<AdminEntity> children) {
        this.children = children;
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
