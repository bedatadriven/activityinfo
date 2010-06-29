/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;


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
