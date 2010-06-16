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
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Bertram
 */
@Entity
public class AdminLevel implements java.io.Serializable {

	private int id;
	private Country country;
	private AdminLevel parent;
	private String name;
	private boolean allowAdd;
	private Set<AdminEntity> entities = new HashSet<AdminEntity>(0);
	private Set<AdminLevel> childLevels = new HashSet<AdminLevel>(0);

	public AdminLevel() {
	}

	public AdminLevel(int adminLevelId, Country country, String name,
			boolean allowAdd) {
		this.id = adminLevelId;
		this.country = country;
		this.name = name;
		this.allowAdd = allowAdd;
	}

	public AdminLevel(int adminLevelId, Country country, AdminLevel adminLevel,
			String name, boolean allowAdd, Set<AdminEntity> entities,
			Set<AdminLevel> childLevels) {
		this.id = adminLevelId;
		this.country = country;
		this.parent = adminLevel;
		this.name = name;
		this.allowAdd = allowAdd;
		this.entities = entities;
		this.childLevels = childLevels;
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

	@Column(name = "AllowAdd", nullable = false)
	public boolean isAllowAdd() {
		return this.allowAdd;
	}

	public void setAllowAdd(boolean allowAdd) {
		this.allowAdd = allowAdd;
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
