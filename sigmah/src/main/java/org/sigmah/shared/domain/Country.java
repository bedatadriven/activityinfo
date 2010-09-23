/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name="queryAllCountriesAlphabetically",
        query="select c from Country c order by c.name")
public class Country implements Serializable, SchemaElement {

	private int id;
	private String name;
	private Bounds bounds;
	
	private Set<AdminLevel> adminLevels = new HashSet<AdminLevel>(0);
	private Set<LocationType> locationTypes = new HashSet<LocationType>(0);
	
	private String codeISO;
	
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
	
	@Column(name = "ISO2", length = 2)
	public String getCodeISO() {
	    return this.codeISO;
	}

	public void setCodeISO(String codeISO) {
	    this.codeISO = codeISO;
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
	
	@Override
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Country");
        sb.append("\nname:");
        sb.append(this.getName());
        sb.append("\niso2:");
        sb.append(this.getCodeISO());
        sb.append("\nbounds:");
        sb.append(this.getBounds());
        return sb.toString();
	}
}
