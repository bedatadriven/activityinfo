/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
@Table(name="Partner")
public class OrgUnit implements java.io.Serializable, SchemaElement {
	private int id;
	private String name;
	private String fullName;
    private Set<UserDatabase> databases = new HashSet<UserDatabase>(0);
    private Location location;
    private OrgUnit parent;
    private Set<OrgUnit> children = new HashSet<OrgUnit>(0);
    private Organization organization;

	public OrgUnit() {
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


    @ManyToOne
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * The point location of the OrgUnit, generally
     * the city of its head office.
     *
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * The parent OrgUnit that manages this OrgUnit
     */
    @ManyToOne
    public OrgUnit getParent() {
        return parent;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }

    /**
     * The children OrgUnits that are managed by this OrgUnit
     */
    @OneToMany(mappedBy = "parent")
    public Set<OrgUnit> getChildren() {
        return children;
    }

    public void setChildren(Set<OrgUnit> children) {
        this.children = children;
    }



    
}
