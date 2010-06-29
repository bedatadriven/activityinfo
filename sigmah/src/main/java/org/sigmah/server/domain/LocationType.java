/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;


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
public class LocationType implements Serializable, SchemaElement {

	private int id;
	private boolean reuse;
	private String name;
	private Country country;
	private Set<Location> locations = new HashSet<Location>(0);
	private Set<Activity> activities = new HashSet<Activity>(0);
	
	private AdminLevel boundAdminLevel;

	public LocationType() {
	}


	@Id
	@Column(name = "LocationTypeId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Reuse", nullable = false)
	public boolean isReuse() {
		return this.reuse;
	}

	public void setReuse(boolean reuse) {
		this.reuse = reuse;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CountryId", nullable = false)
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "locationType")
	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "locationType")
	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BoundAdminLevelId", nullable = true)
	public AdminLevel getBoundAdminLevel() {
		return boundAdminLevel;
	}

	public void setBoundAdminLevel(AdminLevel boundAdminLevel) {
		this.boundAdminLevel = boundAdminLevel;
	}

}
