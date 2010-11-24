package org.sigmah.shared.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.sigmah.shared.dto.element.DefaultFlexibleElementContainer;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class OrgUnitDTO extends BaseModelData implements EntityDTO, DefaultFlexibleElementContainer {

    private static final long serialVersionUID = -8812034670573721384L;

    @Override
    public String getEntityName() {
        return "OrgUnit";
    }

    // Id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Name
    @Override
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Full name
    @Override
    public String getFullName() {
        return get("fullName");
    }

    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    // Model
    public OrgUnitModelDTO getOrgUnitModel() {
        return get("oum");
    }

    public void setOrgUnitModel(OrgUnitModelDTO oum) {
        set("oum", oum);
    }

    // Planned budget
    @Override
    public Double getPlannedBudget() {
        final Double b = (Double) get("plannedBudget");
        return b != null ? b : 0.0;
    }

    public void setPlannedBudget(Double plannedBudget) {
        set("plannedBudget", plannedBudget);
    }

    // Spent budget
    @Override
    public Double getSpendBudget() {
        final Double b = (Double) get("spendBudget");
        return b != null ? b : 0.0;
    }

    public void setSpendBudget(Double spendBudget) {
        set("spendBudget", spendBudget);
    }

    // Received budget
    @Override
    public Double getReceivedBudget() {
        final Double b = (Double) get("receivedBudget");
        return b != null ? b : 0.0;
    }

    public void setReceivedBudget(Double receivedBudget) {
        set("receivedBudget", receivedBudget);
    }

    // Organization
    public OrganizationDTO getOrganization() {
        return get("organization");
    }

    public void setOrganization(OrganizationDTO organization) {
        set("organization", organization);
    }

    // Parent
    public OrgUnitDTO getParent() {
        return get("parent");
    }

    public void setParent(OrgUnitDTO parent) {
        set("parent", parent);
    }

    // Children
    public Set<OrgUnitDTO> getChildren() {
        return get("children");
    }

    public void setChildren(Set<OrgUnitDTO> children) {
        set("children", children);
    }

    // Calendar id
    public Integer getCalendarId() {
        return (Integer) get("calendarId");
    }

    public void setCalendarId(Integer calendarId) {
        set("calendarId", calendarId);
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public Date getEndDate() {
        return null;
    }

    @Override
    public CountryDTO getCountry() {
        return null;
    }

    @Override
    public String getOwnerFirstName() {
        return null;
    }

    @Override
    public String getOwnerName() {
        return null;
    }

    /**
     * Transforms this entity into a {@link OrgUnitDTOLight} entity.
     * 
     * @return The {@link OrgUnitDTOLight} entity.
     */
    public OrgUnitDTOLight light(OrgUnitDTOLight parent) {

        final OrgUnitDTOLight light = new OrgUnitDTOLight();
        light.setId(getId());
        light.setName(getName());
        light.setFullName(getFullName());
        light.generateCompleteName();
        light.setParentDTO(parent);
        final HashSet<OrgUnitDTOLight> children = new HashSet<OrgUnitDTOLight>();
        for (final OrgUnitDTO c : getChildren()) {
            children.add(c.light(light));
        }
        light.setChildrenDTO(children);

        return light;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof OrgUnitDTO)) {
            return false;
        }

        final OrgUnitDTO other = (OrgUnitDTO) obj;
        return getId() == other.getId();
    }
}
