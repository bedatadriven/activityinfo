/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alex Bertram
 * 
 */
@Entity
@Table(name = "Partner")
public class OrgUnit implements java.io.Serializable, SchemaElement {

    private static final long serialVersionUID = -5985734789552797994L;

    private int id;
    private String name;
    private String fullName;
    private Set<UserDatabase> databases = new HashSet<UserDatabase>(0);
    private Location location;
    private OrgUnit parent;
    private Set<OrgUnit> children = new HashSet<OrgUnit>(0);
    private Double plannedBudget;
    private Double spendBudget;
    private Double receivedBudget;
    private Integer calendarId;
    private Country officeLocationCountry;

    public OrgUnit() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @JoinTable(name = "PartnerInDatabase", joinColumns = { @JoinColumn(name = "PartnerId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "DatabaseId", nullable = false, updatable = false) })
    public Set<UserDatabase> getDatabases() {
        return this.databases;
    }

    public void setDatabases(Set<UserDatabase> databases) {
        this.databases = databases;
    }


    /**
     * The point location of the OrgUnit, generally the city of its head office.
     * 
     */
    @ManyToOne
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
    @OrderBy("name asc")
    public Set<OrgUnit> getChildren() {
        return children;
    }

    public void setChildren(Set<OrgUnit> children) {
        this.children = children;
    }

    @Column(name = "planned_budget", nullable = true)
    public Double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }

    @Column(name = "spend_budget", nullable = true)
    public Double getSpendBudget() {
        return spendBudget;
    }

    public void setSpendBudget(Double spendBudget) {
        this.spendBudget = spendBudget;
    }

    @Column(name = "received_budget", nullable = true)
    public Double getReceivedBudget() {
        return receivedBudget;
    }

    public void setReceivedBudget(Double receivedBudget) {
        this.receivedBudget = receivedBudget;
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "office_country_id", nullable = true)
    public Country getOfficeLocationCountry() {
        return officeLocationCountry;
    }

    public void setOfficeLocationCountry(Country officeLocationCountry) {
        this.officeLocationCountry = officeLocationCountry;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof OrgUnit)) {
            return false;
        }

        final OrgUnit other = (OrgUnit) obj;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
