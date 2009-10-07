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
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class Country implements Serializable, SchemaElement {

	private int id;
	private String name;
	private Bounds bounds;
	
	private Set<AdminLevel> adminLevels = new HashSet<AdminLevel>(0);
	private Set<LocationType> locationTypes = new HashSet<LocationType>(0);
	
	public Country() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "CountryId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Embedded
	public Bounds getBounds() {
		return this.bounds;
	}
	
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
	@org.hibernate.annotations.OrderBy(clause="AdminLevelId")
	public Set<AdminLevel> getAdminLevels() {
		return this.adminLevels;
	}

	public void setAdminLevels(Set<AdminLevel> adminLevels) {
		this.adminLevels = adminLevels;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
	public Set<LocationType> getLocationTypes() {
		return locationTypes;
	}
	
	public void setLocationTypes(Set<LocationType> types) {
		this.locationTypes = types;
	}
}
