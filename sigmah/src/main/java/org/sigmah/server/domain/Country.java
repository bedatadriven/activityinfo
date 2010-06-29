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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name="queryAllCountriesAlphabetically",
        query="select c from Country c order by c.name")
public class Country implements Serializable, SchemaElement {

	private int id;
	private String name;
	private Bounds bounds;
	
	private Set<AdminLevel> adminLevels = new HashSet<AdminLevel>(0);
	private Set<LocationType> locationTypes = new HashSet<LocationType>(0);
	
	public Country() {
	}


    /**
     * Gets the country's id
     *
     * @return the country's id
     */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "CountryId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

    /**
     * Sets the country's id
     *
     * @param id the country's id
     */
	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

    /**
     * A short, human-readable name of the Country
     *
     * @param name A short, human-readable name of the Country
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * The geographic bounds of this Country. Bounds for the Country cannot be null.
     *
     * @return tbe geogaphics bounds of this Country
     */
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="x1", column=@Column(nullable = false)),
            @AttributeOverride(name="y1", column=@Column(nullable = false)),
            @AttributeOverride(name="x2", column=@Column(nullable = false)),
            @AttributeOverride(name="y2", column=@Column(nullable = false))
    })
	public Bounds getBounds() {
		return this.bounds;
	}

    /**
     * Sets the country's geographic bounds. Bounds for the Country c
     * @param bounds
     */
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

    /**
     * Gets all the administrative levels for this Country
     *
     * @return a list of all the administrative levels in this Country
     */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
	@org.hibernate.annotations.OrderBy(clause="AdminLevelId")
	public Set<AdminLevel> getAdminLevels() {
		return this.adminLevels;
	}

    /**
     * Sets the administrative levels for this Country
     *
     * @param adminLevels
     */
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
