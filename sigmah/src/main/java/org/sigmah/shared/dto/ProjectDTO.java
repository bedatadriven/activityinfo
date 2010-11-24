/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sigmah.shared.dto.element.DefaultFlexibleElementContainer;
import org.sigmah.shared.dto.logframe.LogFrameDTO;
import org.sigmah.shared.dto.value.ValueDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity Project.
 * 
 * @author tmi
 * 
 */
public final class ProjectDTO extends BaseModelData implements EntityDTO, DefaultFlexibleElementContainer {

    private static final long serialVersionUID = -8604264278832531036L;

    @Override
    public String getEntityName() {
        return "Project";
    }

    @Override
    public String toString() {
        return "ProjectDTO id:" + getId() + ", name:" + getName() + ", projectModelDTO: " + getProjectModelDTO()
                + ", owner:" + getOwnerName() + ", phaseDTO:" + getPhasesDTO() + ", valueDTO:" + getValuesDTO()
                + ", currentPhaseDTO:" + getCurrentPhaseDTO();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectDTO other = (ProjectDTO) obj;
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.getId();
        return hash;
    }

    // Project id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Project name
    @Override
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Project full name
    @Override
    public String getFullName() {
        return get("fullName");
    }

    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    // Project start date
    @Override
    public Date getStartDate() {
        return get("startDate");
    }

    public void setStartDate(Date startDate) {
        set("startDate", startDate);
    }

    // Project end date
    @Override
    public Date getEndDate() {
        return get("endDate");
    }

    public void setEndDate(Date endDate) {
        set("endDate", endDate);
    }

    // Reference to the Project Model
    public ProjectModelDTO getProjectModelDTO() {
        return get("projectModelDTO");
    }

    public void setProjectModelDTO(ProjectModelDTO projectModelDTO) {
        set("projectModelDTO", projectModelDTO);
    }

    // Owner project name
    @Override
    public String getOwnerName() {
        return get("ownerName");
    }

    public void setOwnerName(String ownerName) {
        set("ownerName", ownerName);
    }

    // Owner project first name
    @Override
    public String getOwnerFirstName() {
        return get("ownerFirstName");
    }

    public void setOwnerFirstName(String ownerFirstName) {
        set("ownerFirstName", ownerFirstName);
    }

    // Owner project email
    public String getOwnerEmail() {
        return get("email");
    }

    public void setOwnerEmail(String email) {
        set("email", email);
    }

    // Reference to the project phases list
    public List<PhaseDTO> getPhasesDTO() {
        return get("phasesDTO");
    }

    public void setPhasesDTO(List<PhaseDTO> phasesDTO) {
        set("phasesDTO", phasesDTO);
    }

    // Reference to the project values list
    public List<ValueDTO> getValuesDTO() {
        return get("valuesDTO");
    }

    public void setValuesDTO(List<ValueDTO> valuesDTO) {
        set("valuesDTO", valuesDTO);
    }

    // Reference to the current phase
    public PhaseDTO getCurrentPhaseDTO() {
        return get("currentPhaseDTO");
    }

    public void setCurrentPhaseDTO(PhaseDTO currentPhaseDTO) {
        set("currentPhaseDTO", currentPhaseDTO);
    }

    public Integer getCalendarId() {
        return (Integer) get("calendarId");
    }

    public void setCalendarId(Integer calendarId) {
        set("calendarId", calendarId);
    }

    public LogFrameDTO getLogFrameDTO() {
        return get("logFrameDTO");
    }

    public void setLogFrameDTO(LogFrameDTO logFrameDTO) {
        set("logFrameDTO", logFrameDTO);
    }

    @Override
    public Double getPlannedBudget() {
        final Double b = (Double) get("plannedBudget");
        return b != null ? b : 0.0;
    }

    public void setPlannedBudget(Double plannedBudget) {
        set("plannedBudget", plannedBudget);
    }

    @Override
    public Double getSpendBudget() {
        final Double b = (Double) get("spendBudget");
        return b != null ? b : 0.0;
    }

    public void setSpendBudget(Double spendBudget) {
        set("spendBudget", spendBudget);
    }

    @Override
    public Double getReceivedBudget() {
        final Double b = (Double) get("receivedBudget");
        return b != null ? b : 0.0;
    }

    public void setReceivedBudget(Double receivedBudget) {
        set("receivedBudget", receivedBudget);
    }

    public List<ProjectFundingDTO> getFunding() {
        return get("funding");
    }

    public void setFunding(List<ProjectFundingDTO> funding) {
        set("funding", funding);
    }

    public void addFunding(ProjectFundingDTO funding) {

        if (funding == null) {
            return;
        }

        List<ProjectFundingDTO> fundings = getFunding();

        if (fundings == null) {
            fundings = new ArrayList<ProjectFundingDTO>();
        }

        fundings.remove(funding);
        fundings.add(funding);

        setFunding(fundings);
    }

    public List<ProjectFundingDTO> getFunded() {
        return get("funded");
    }

    public void setFunded(List<ProjectFundingDTO> funded) {
        set("funded", funded);
    }

    public void addFunded(ProjectFundingDTO funded) {

        if (funded == null) {
            return;
        }

        List<ProjectFundingDTO> fundeds = getFunding();

        if (fundeds == null) {
            fundeds = new ArrayList<ProjectFundingDTO>();
        }

        fundeds.remove(funded);
        fundeds.add(funded);

        setFunded(fundeds);
    }

    @Override
    public CountryDTO getCountry() {
        return get("country");
    }

    public void setCountry(CountryDTO country) {
        set("country", country);
    }

    /**
     * Gets the following phases of the given phase.
     * 
     * @param phase
     *            The phase.
     * @return The following phases.
     */
    public List<PhaseDTO> getSuccessors(PhaseDTO phase) {

        if (phase == null || phase.getPhaseModelDTO() == null) {
            return null;
        }

        final ArrayList<PhaseDTO> successors = new ArrayList<PhaseDTO>();

        // For each successor.
        for (final PhaseModelDTO successorModel : phase.getPhaseModelDTO().getSuccessorsDTO()) {

            // Retrieves the equivalent phase in this project.
            for (final PhaseDTO p : getPhasesDTO()) {

                if (p.getId() != phase.getId()) {
                    if (successorModel.equals(p.getPhaseModelDTO())) {
                        successors.add(p);
                    }
                }
            }
        }

        return successors;
    }

    /**
     * Map this project entity in a lightweight project.
     * 
     * @return The lightweight project.
     */
    public ProjectDTOLight light() {

        final ProjectDTOLight light = new ProjectDTOLight();
        light.setId(getId());
        light.setName(getName());
        light.setFullName(getFullName());
        light.generateCompleteName();
        light.setFavorite(false);
        light.setCurrentPhaseDTO(getCurrentPhaseDTO());
        light.setVisibilities(getProjectModelDTO().getVisibilities());
        light.setPlannedBudget(getPlannedBudget());

        return light;
    }
}
