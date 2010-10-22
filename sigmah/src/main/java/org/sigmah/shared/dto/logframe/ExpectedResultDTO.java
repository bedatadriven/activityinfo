package org.sigmah.shared.dto.logframe;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.sigmah.client.page.project.logframe.grid.Row.Positionable;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.ExpectedResult.
 * 
 * @author tmi
 * 
 */
public class ExpectedResultDTO extends BaseModelData implements EntityDTO, Positionable {

    private static final long serialVersionUID = 2394670766294049525L;

    @Override
    public String getEntityName() {
        return "logframe.ExpectedResult";
    }

    // Result id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Result code.
    public Integer getCode() {
        return get("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    // Result position in its group.
    public Integer getPosition() {
        return get("position");
    }

    @Override
    public void setPosition(Integer position) {
        set("position", position);
    }

    // Result intervention logic.
    public String getInterventionLogic() {
        return get("interventionLogic");
    }

    public void setInterventionLogic(String interventionLogic) {
        set("interventionLogic", interventionLogic);
    }

    // Result risks.
    public String getRisks() {
        return get("risks");
    }

    public void setRisks(String risks) {
        set("risks", risks);
    }

    // Result assumptions.
    public String getAssumptions() {
        return get("assumptions");
    }

    public void setAssumptions(String assumptions) {
        set("assumptions", assumptions);
    }

    // Result activities.
    public List<LogFrameActivityDTO> getActivitiesDTO() {
        return get("activitiesDTO");
    }

    public void setActivitiesDTO(List<LogFrameActivityDTO> activitiesDTO) {
        set("activitiesDTO", activitiesDTO);
    }

    /**
     * Gets the list of activities which aren't deleted.
     * 
     * @return The list of activities which aren't deleted.
     */
    public List<LogFrameActivityDTO> getActivitiesDTONotDeleted() {

        final List<LogFrameActivityDTO> activities = get("activitiesDTO");

        if (activities == null) {
            return null;
        }

        // Filters deleted activities.
        // This action is needed because after saving the log frame, the
        // hibernate filter to hide deleted entities isn't re-applied.
        for (final Iterator<LogFrameActivityDTO> iterator = activities.iterator(); iterator.hasNext();) {

            final LogFrameActivityDTO logFrameActivityDTO = iterator.next();
            if (logFrameActivityDTO.isDeleted()) {
                iterator.remove();
            }
        }

        return activities;
    }

    // Result parent objective.
    public SpecificObjectiveDTO getParentSpecificObjectiveDTO() {
        return get("parentSpecificObjectiveDTO");
    }

    public void setParentSpecificObjectiveDTO(SpecificObjectiveDTO parentSpecificObjectiveDTO) {
        set("parentSpecificObjectiveDTO", parentSpecificObjectiveDTO);
    }

    // Result group.
    public LogFrameGroupDTO getLogFrameGroupDTO() {
        return get("logFrameGroupDTO");
    }

    public void setLogFrameGroupDTO(LogFrameGroupDTO logFrameGroupDTO) {
        set("logFrameGroupDTO", logFrameGroupDTO);
    }

    // Display label.
    /**
     * Sets the attribute <code>label</code> to display this element in a
     * selection window.
     */
    public void setLabel(String label) {
        set("label", label);
    }

    public String getLabel() {
        return get("label");
    }

    // Result deleted date.
    public Date getDateDeleted() {
        return get("dateDeleted");
    }

    public void setDateDeleted(Date dateDeleted) {
        set("dateDeleted", dateDeleted);
    }

    /**
     * Deletes this result.
     */
    public void delete() {
        setDateDeleted(new Date());
    }

    /**
     * Returns if this result is deleted.
     * 
     * @return If this result is deleted.
     */
    public boolean isDeleted() {
        return getDateDeleted() != null;
    }

    /**
     * Gets the client-side id for this entity. If this entity has a server-id
     * id, it's returned. Otherwise, a temporary id is generated and returned.
     * 
     * @return The client-side id.
     */
    public int getClientSideId() {

        // Server-side id.
        Integer id = (Integer) get("id");

        if (id == null) {

            // Client-side id.
            id = (Integer) get("tmpid");

            // Generates the client-side id once.
            if (id == null) {
                id = generateClientSideId();
            }
        }

        return id;
    }

    /**
     * Generate a client-side unique id for this entity and stores it in the
     * <code>temporaryId</code> attribute.
     */
    private int generateClientSideId() {
        final int id = (int) new Date().getTime();
        set("tmpid", id);
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ExpectedResultDTO [");
        sb.append("entity name = ");
        sb.append(getEntityName());
        sb.append(" ; id = ");
        sb.append(getId());
        sb.append(" ; group id = ");
        if (getLogFrameGroupDTO() != null) {
            sb.append(getLogFrameGroupDTO().getId() != -1 ? getLogFrameGroupDTO().getId() : getLogFrameGroupDTO()
                    .getClientSideId());
        }
        sb.append(" ; dlabel = ");
        sb.append(getLabel());
        sb.append(" ; code = ");
        sb.append(getCode());
        sb.append(" ; intervention logic = ");
        sb.append(getInterventionLogic());
        sb.append(" ; risks = ");
        sb.append(getRisks());
        sb.append(" ; assumptions = ");
        sb.append(getAssumptions());
        sb.append(" ; activities = (\n");
        if (getActivitiesDTO() != null) {
            for (final LogFrameActivityDTO a : getActivitiesDTO()) {
                sb.append(a);
                sb.append("\n");
            }
        }
        sb.append(")]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return getClientSideId();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ExpectedResultDTO)) {
            return false;
        }

        final ExpectedResultDTO other = (ExpectedResultDTO) obj;
        return getClientSideId() == other.getClientSideId();
    }

    /**
     * Adds a new activity to this log frame.
     * 
     * @return The new activity.
     */
    public LogFrameActivityDTO addActivity() {

        List<LogFrameActivityDTO> activities = getActivitiesDTO();

        // Retrieves the higher code.
        int max = 0;
        if (activities != null) {
            for (final LogFrameActivityDTO activity : activities) {
                max = activity.getCode() > max ? activity.getCode() : max;
            }
        }

        if (activities == null) {
            activities = new ArrayList<LogFrameActivityDTO>();
        }

        // Creates the activity.
        final LogFrameActivityDTO newActivity = new LogFrameActivityDTO();
        newActivity.setCode(max + 1);
        newActivity.setParentExpectedResultDTO(this);

        // Adds it to the local list.
        activities.add(newActivity);
        setActivitiesDTO(activities);

        return newActivity;
    }

    /**
     * Removes an activity from this result.
     * 
     * @param activity
     *            The activity to remove.
     * @return If the activity has been removed.
     */
    public boolean removeActivity(LogFrameActivityDTO activity) {

        // Gets the current activities list.
        final List<LogFrameActivityDTO> activities = getActivitiesDTO();

        // If the list is empty, do nothing.
        if (activities == null) {
            return false;
        }

        // Tries to remove the activity from the local list.
        if (activities.contains(activity)) {
            activity.delete();
            return true;
        }

        return false;
    }
}
