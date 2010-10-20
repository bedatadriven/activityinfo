package org.sigmah.shared.dto.logframe;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.ExpectedResult.
 * 
 * @author tmi
 * 
 */
public class ExpectedResultDTO extends BaseModelData implements EntityDTO {

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
}
