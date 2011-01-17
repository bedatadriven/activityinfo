/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.sigmah.shared.dto.element.DefaultFlexibleElementContainer;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.logframe.LogFrameDTO;
import org.sigmah.shared.dto.reminder.MonitoredPointDTO;
import org.sigmah.shared.dto.reminder.MonitoredPointListDTO;
import org.sigmah.shared.dto.reminder.ReminderDTO;
import org.sigmah.shared.dto.reminder.ReminderListDTO;
import org.sigmah.shared.dto.value.ValueDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.shared.domain.Amendment;

/**
 * DTO mapping class for entity Project.
 * 
 * @author tmi
 * 
 */
public final class ProjectDTO extends BaseModelData implements EntityDTO, DefaultFlexibleElementContainer {

    private static final long serialVersionUID = -8604264278832531036L;

    /**
     * Localizes an flexible element in the project model.
     * 
     * @author tmi
     * 
     */
    public static final class LocalizedElement extends ProjectModelDTO.LocalizedElement {

        private final PhaseDTO phase;

        private LocalizedElement(ProjectModelDTO.LocalizedElement localized, PhaseDTO phase) {
            super(localized.getPhaseModel(), localized.getElement());
            this.phase = phase;
        }

        /**
         * Get the phase model in which the element is displayed, or
         * <code>null</code> if the element is in the details page.
         * 
         * @return The phase model of the element or <code>null</code>.
         */
        public PhaseDTO getPhase() {
            return phase;
        }
    }

    public static interface MonitoredPointListener {

        public void pointAdded(MonitoredPointDTO point);
    }

    public static interface ReminderListener {

        public void reminderAdded(ReminderDTO reminder);
    }

    private transient HashMap<PhaseModelDTO, PhaseDTO> mappedPhases;

    private transient ArrayList<MonitoredPointListener> listeners;

    private transient ArrayList<ReminderListener> listenersReminders;

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

    public MonitoredPointListDTO getPointsList() {
        return get("pointsList");
    }

    public void setPointsList(MonitoredPointListDTO pointsList) {
        set("pointsList", pointsList);
    }

    public ReminderListDTO getRemindersList() {
        return get("remindersList");
    }

    public void setRemindersList(ReminderListDTO remindersList) {
        set("remindersList", remindersList);
    }

    public void setStarred(Boolean starred) {
        set("starred", starred);
    }

    public Boolean getStarred() {
        final Boolean b = (Boolean) get("starred");
        return b == null ? false : b;
    }

    public void setCloseDate(Date closeDate) {
        set("closeDate", closeDate);
    }

    public Date getCloseDate() {
        return get("closeDate");
    }

    public boolean isClosed() {
        return getCloseDate() != null;
    }

    public Amendment.State getAmendmentState() {
        return get("amendmentState");
    }

    public void setAmendmentState(Amendment.State amendmentState) {
        set("amendmentState", amendmentState);
    }

    public Integer getAmendmentVersion() {
        return get("amendmentVersion");
    }

    public void setAmendmentVersion(Integer amendmentVersion) {
        set("amendmentVersion", amendmentVersion);
    }

    public Integer getAmendmentRevision() {
        return get("amendmentRevision");
    }

    public void setAmendmentRevision(Integer amendmentRevision) {
        set("amendmentRevision", amendmentRevision);
    }

    public List<AmendmentDTO> getAmendments() {
        return get("amendments");
    }

    public void setAmendments(List<AmendmentDTO> amendments) {
        set("amendments", amendments);
    }

    public void addListener(MonitoredPointListener l) {

        if (listeners == null) {
            listeners = new ArrayList<MonitoredPointListener>();
        }

        listeners.add(l);
    }

    public void addListener(ReminderListener l) {

        if (listenersReminders == null) {
            listenersReminders = new ArrayList<ReminderListener>();
        }

        listenersReminders.add(l);
    }

    public void removeAllListeners() {
        listeners = null;
        listenersReminders = null;
    }

    public void addMonitoredPoint(MonitoredPointDTO point) {

        final MonitoredPointListDTO list = getPointsList();

        // Must not happened.
        if (list == null) {
            return;
        }

        list.getPoints().add(point);

        if (listeners != null) {
            for (final MonitoredPointListener l : listeners) {
                l.pointAdded(point);
            }
        }
    }

    public void addReminder(ReminderDTO reminder) {

        final ReminderListDTO list = getRemindersList();

        // Must not happened.
        if (list == null) {
            return;
        }

        list.getReminders().add(reminder);

        if (listenersReminders != null) {
            for (final ReminderListener l : listenersReminders) {
                l.reminderAdded(reminder);
            }
        }
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
        light.setStarred(getStarred());
        light.setStartDate(getStartDate());
        light.setEndDate(getEndDate());
        light.setName(getName());
        light.setFullName(getFullName());
        light.generateCompleteName();
        light.setCurrentPhaseDTO(getCurrentPhaseDTO());
        light.setVisibilities(getProjectModelDTO().getVisibilities());
        light.setPlannedBudget(getPlannedBudget());
        light.setSpendBudget(getSpendBudget());
        light.setReceivedBudget(getReceivedBudget());
        light.setCloseDate(getCloseDate());

        return light;
    }

    /**
     * Gets all the flexible elements instances of the given class in this
     * project (phases and details page). The banner is ignored cause the
     * elements in it are read-only.
     * 
     * @param clazz
     *            The class of the searched flexible elements.
     * @return The elements localized for the given class, or <code>null</code>
     *         if there is no element of this class.
     */
    public List<LocalizedElement> getLocalizedElements(Class<? extends FlexibleElementDTO> clazz) {

        final List<ProjectModelDTO.LocalizedElement> localizedElements = getProjectModelDTO().getLocalizedElements(
                clazz);

        if (localizedElements == null) {
            return null;
        }

        final ArrayList<LocalizedElement> elements = new ArrayList<LocalizedElement>();
        for (final ProjectModelDTO.LocalizedElement localized : localizedElements) {
            elements.add(new LocalizedElement(localized, getPhaseFromModel(localized.getPhaseModel())));
        }

        return elements;
    }

    /**
     * Gets the phase which implements the given model for the current project.
     * 
     * @param model
     *            The phase model.
     * @return The corresponding phase.
     */
    public PhaseDTO getPhaseFromModel(PhaseModelDTO model) {

        if (mappedPhases == null) {
            mappedPhases = new HashMap<PhaseModelDTO, PhaseDTO>();
            for (final PhaseDTO phase : getPhasesDTO()) {
                mappedPhases.put(phase.getPhaseModelDTO(), phase);
            }
        }

        return mappedPhases.get(model);
    }
}
