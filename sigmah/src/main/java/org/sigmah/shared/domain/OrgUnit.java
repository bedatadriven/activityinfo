/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

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
    private Organization organization;
    private Double plannedBudget;
    private Double spendBudget;
    private Double receivedBudget;
    private OrgUnitModel orgUnitModel;
    private Integer calendarId;

    public OrgUnit() {
    }

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "id_org_unit_model")
    public OrgUnitModel getOrgUnitModel() {
        return orgUnitModel;
    }

    public void setOrgUnitModel(OrgUnitModel orgUnitModel) {
        this.orgUnitModel = orgUnitModel;
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

    @ManyToOne
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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
}
