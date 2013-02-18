/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;


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
