

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


@Entity
public class AdminLevel implements java.io.Serializable {

	private int id;
	private Country country;
	private AdminLevel parent;
	private String name;
	private boolean polygons;
	private Set<AdminEntity> entities = new HashSet<AdminEntity>(0);
	private Set<AdminLevel> childLevels = new HashSet<AdminLevel>(0);

	public AdminLevel() {
	}

	public AdminLevel(int adminLevelId, Country country, String name) {
		this.id = adminLevelId;
		this.country = country;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "AdminLevelId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int ID) {
		this.id = ID;
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

	public void setParent(AdminLevel adminLevel) {
		this.parent = adminLevel;
	}

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<AdminLevel> getChildLevels() {
		return this.childLevels;
	}

	public void setChildLevels(Set<AdminLevel> childLevels) {
		this.childLevels = childLevels;
	}
}
