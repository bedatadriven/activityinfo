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
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class Partner implements java.io.Serializable, SchemaElement {

	private int id;
	private String name;
	private String fullName;
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>(0);
	private Set<UserDatabase> databases = new HashSet<UserDatabase>(0);
	
	public Partner() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	@JoinTable(name = "PartnerInDatabase", 
			joinColumns = { @JoinColumn(name = "PartnerId", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "DatabaseId", nullable = false, updatable = false) }) 
	public Set<UserDatabase> getDatabases() {
		return this.databases;
	}
	
	public void setDatabases(Set<UserDatabase> databases) {
		this.databases = databases;
	}
	
//	@Column(name = "Operational", nullable = false)
//	public boolean isOperational() {
//		return this.operational;
//	}
//
//	public void setOperational(boolean operational) {
//		this.operational = operational;
//	}

	

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "partner")
//	public Set<UserPermission> getUserPermissions() {
//		return this.userPermissions;
//	}
//
//	public void setUserPermissions(Set<UserPermission> userPermissions) {
//		this.userPermissions = userPermissions;
//	}

}
