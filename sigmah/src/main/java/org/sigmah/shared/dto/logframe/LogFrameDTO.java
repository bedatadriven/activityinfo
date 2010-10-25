package org.sigmah.shared.dto.logframe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.sigmah.shared.domain.logframe.LogFrameGroupType;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.LogFrame.
 * 
 * @author tmi
 * 
 */
public class LogFrameDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -2994539648384496954L;

    @Override
    public String getEntityName() {
        return "logframe.LogFrame";
    }

    // Log frame id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Log frame model.
    public LogFrameModelDTO getLogFrameModelDTO() {
        return get("model");
    }

    public void setLogFrameModelDTO(LogFrameModelDTO model) {
        set("model", model);
    }

    // Log frame risks.
    public String getTitle() {
        return get("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    // Log main objective.
    public String getMainObjective() {
        return get("mainObjective");
    }

    public void setMainObjective(String mainObjective) {
        set("mainObjective", mainObjective);
    }

    // Log frame specific objectives.
    public List<SpecificObjectiveDTO> getSpecificObjectivesDTO() {
        return get("specificObjectivesDTO");
    }

    public void setSpecificObjectivesDTO(List<SpecificObjectiveDTO> specificObjectivesDTO) {
        set("specificObjectivesDTO", specificObjectivesDTO);
    }

    /**
     * Gets the list of specific objectives which aren't deleted.
     * 
     * @return The list of specific objectives which aren't deleted.
     */
    public List<SpecificObjectiveDTO> getSpecificObjectivesDTONotDeleted() {

        final List<SpecificObjectiveDTO> objectives = get("specificObjectivesDTO");

        if (objectives == null) {
            return null;
        }

        // Filters deleted objectives.
        // This action is needed because after saving the log frame, the
        // hibernate filter to hide deleted entities isn't re-applied.
        for (final Iterator<SpecificObjectiveDTO> iterator = objectives.iterator(); iterator.hasNext();) {

            final SpecificObjectiveDTO specificObjectiveDTO = iterator.next();
            if (specificObjectiveDTO.isDeleted()) {
                iterator.remove();
            }
        }

        return objectives;
    }

    // Log frame prerequisites.
    public List<PrerequisiteDTO> getPrerequisitesDTO() {
        return get("prerequisitesDTO");
    }

    public void setPrerequisitesDTO(List<PrerequisiteDTO> prerequisitesDTO) {
        set("prerequisitesDTO", prerequisitesDTO);
    }

    /**
     * Gets the list of prerequisites which aren't deleted.
     * 
     * @return The list of prerequisites which aren't deleted.
     */
    public List<PrerequisiteDTO> getPrerequisitesDTONotDeleted() {

        final List<PrerequisiteDTO> prerequisites = get("prerequisitesDTO");

        if (prerequisites == null) {
            return null;
        }

        // Filters deleted prerequisites.
        // This action is needed because after saving the log frame, the
        // hibernate filter to hide deleted entities isn't re-applied.
        for (final Iterator<PrerequisiteDTO> iterator = prerequisites.iterator(); iterator.hasNext();) {

            final PrerequisiteDTO prerequisiteDTO = iterator.next();
            if (prerequisiteDTO.isDeleted()) {
                iterator.remove();
            }
        }

        return prerequisites;
    }

    // Log frame group.
    public List<LogFrameGroupDTO> getGroupsDTO() {
        return get("groupsDTO");
    }

    public void setGroupsDTO(List<LogFrameGroupDTO> groupsDTO) {
        set("groupsDTO", groupsDTO);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LogFrameDTO [");
        sb.append("entity name = ");
        sb.append(getEntityName());
        sb.append(" ; id = ");
        sb.append(getId());
        sb.append(" ; title = ");
        sb.append(getTitle());
        sb.append(" ; main objective = ");
        sb.append(getMainObjective());
        sb.append(" ; groups = (\n");
        if (getGroupsDTO() != null) {
            for (final LogFrameGroupDTO g : getGroupsDTO()) {
                sb.append(g);
                sb.append("\n");
            }
        }
        sb.append(" ; prerequisites = (\n");
        if (getPrerequisitesDTO() != null) {
            for (final PrerequisiteDTO p : getPrerequisitesDTO()) {
                sb.append(p);
                sb.append("\n");
            }
        }
        sb.append(")\n");
        sb.append("specific objectives = (\n");
        if (getSpecificObjectivesDTO() != null) {
            for (final SpecificObjectiveDTO o : getSpecificObjectivesDTO()) {
                sb.append(o);
                sb.append("\n");
            }
        }
        sb.append(")\n");
        sb.append(" ; model = (\n");
        sb.append(getLogFrameModelDTO());
        sb.append(")]\n");

        return sb.toString();
    }

    /**
     * Gets all the groups of the given type. If the type is <code>null</code>,
     * all groups will be returned.
     * 
     * @param type
     *            The type.
     * @return The groups with the given type.
     */
    public Collection<LogFrameGroupDTO> getAllGroups(LogFrameGroupType type) {

        // Lists of groups.
        final HashSet<LogFrameGroupDTO> returnedGroups = new HashSet<LogFrameGroupDTO>();
        final List<LogFrameGroupDTO> groups = getGroupsDTO();

        // Retrieves groups.
        if (groups != null) {
            for (final LogFrameGroupDTO g : groups) {

                // Adds the group if it has the correct type.
                if (type == null || type == g.getType()) {
                    returnedGroups.add(g);
                }
            }
        }

        return returnedGroups;
    }

    /**
     * Gets the only default group of this type. The default group is never
     * <code>null</code>. An empty project log frame contains always one group
     * of each type.
     * 
     * @param type
     *            The type.
     * @return The only default group for the given type.
     */
    public LogFrameGroupDTO getDefaultGroup(LogFrameGroupType type) {

        LogFrameGroupDTO group = null;

        // Lists of groups.
        final List<LogFrameGroupDTO> groups = getGroupsDTO();

        // Retrieves group.
        if (groups != null) {
            for (final LogFrameGroupDTO g : groups) {

                // Stops at the first group with the given type.
                if (type == null || type == g.getType()) {
                    group = g;
                    break;
                }
            }
        }

        return group;
    }

    /**
     * Creates and adds a new group.
     * 
     * @param label
     *            The group label.
     * @param type
     *            The group type.
     * @return The created group.
     */
    public LogFrameGroupDTO addGroup(String label, LogFrameGroupType type) {

        // Creates the groups.
        final LogFrameGroupDTO group = new LogFrameGroupDTO();
        group.setLabel(label);
        group.setType(type);
        group.setParentLogFrameDTO(this);

        // Adds it.
        List<LogFrameGroupDTO> groups = getGroupsDTO();

        if (groups == null) {
            groups = new ArrayList<LogFrameGroupDTO>();
        }

        groups.add(group);

        setGroupsDTO(groups);

        return group;
    }

    /**
     * Adds a new specific objective to this log frame.
     * 
     * @return The new specific objective.
     */
    public SpecificObjectiveDTO addSpecificObjective() {

        List<SpecificObjectiveDTO> specificObjectives = getSpecificObjectivesDTO();

        // Retrieves the higher code.
        int max = 0;
        if (specificObjectives != null) {
            for (final SpecificObjectiveDTO objective : specificObjectives) {
                max = objective.getCode() > max ? objective.getCode() : max;
            }
        }

        if (specificObjectives == null) {
            specificObjectives = new ArrayList<SpecificObjectiveDTO>();
        }

        // Creates the new objective.
        final SpecificObjectiveDTO newObjective = new SpecificObjectiveDTO();
        newObjective.setCode(max + 1);
        newObjective.setParentLogFrameDTO(this);

        // Adds it to the local list.
        specificObjectives.add(newObjective);
        setSpecificObjectivesDTO(specificObjectives);

        return newObjective;
    }

    /**
     * Removes a specific objective from this log frame.
     * 
     * @param objective
     *            The objective to remove.
     * @return If the objective has been removed.
     */
    public boolean removeSpecificObjective(SpecificObjectiveDTO objective) {

        // Gets the current objectives list.
        final List<SpecificObjectiveDTO> specificObjectives = getSpecificObjectivesDTO();

        // If the list is empty, do nothing.
        if (specificObjectives == null) {
            return false;
        }

        // Tries to remove the objective from the local list.
        if (specificObjectives.contains(objective)) {
            objective.delete();
            return true;
        }

        return false;
    }

    /**
     * Adds a new prerequisite to this log frame.
     * 
     * @return The new prerequisite.
     */
    public PrerequisiteDTO addPrerequisite() {

        List<PrerequisiteDTO> prerequisites = getPrerequisitesDTO();

        // Retrieves the higher code.
        int max = 0;
        if (prerequisites != null) {
            for (final PrerequisiteDTO prerequisite : prerequisites) {
                max = prerequisite.getCode() > max ? prerequisite.getCode() : max;
            }
        }

        if (prerequisites == null) {
            prerequisites = new ArrayList<PrerequisiteDTO>();
        }

        // Creates the new objective.
        final PrerequisiteDTO newPrerequisite = new PrerequisiteDTO();
        newPrerequisite.setCode(max + 1);
        newPrerequisite.setParentLogFrameDTO(this);

        // Adds it to the local list.
        prerequisites.add(newPrerequisite);
        setPrerequisitesDTO(prerequisites);

        return newPrerequisite;
    }

    /**
     * Removes a prerequisite from this log frame.
     * 
     * @param prerequisite
     *            The prerequisite to remove.
     * @return If the prerequisite has been removed.
     */
    public boolean removePrerequisite(PrerequisiteDTO prerequisite) {

        // Gets the current prerequisites list.
        final List<PrerequisiteDTO> prerequisites = getPrerequisitesDTO();

        // If the list is empty, do nothing.
        if (prerequisites == null) {
            return false;
        }

        // Tries to remove the prerequisite from the local list.
        if (prerequisites.contains(prerequisite)) {
            prerequisite.delete();
            return true;
        }

        return false;
    }

    /**
     * Returns all the expected results of this log frame.
     * 
     * @return All the expected results of this log frame.
     */
    public List<ExpectedResultDTO> getAllExpectedResultsDTO() {

        final ArrayList<ExpectedResultDTO> results = new ArrayList<ExpectedResultDTO>();

        // Retrieves the expected results for each objective.
        List<ExpectedResultDTO> list;
        for (final SpecificObjectiveDTO objective : getSpecificObjectivesDTONotDeleted()) {
            if ((list = objective.getExpectedResultsDTONotDeleted()) != null) {
                results.addAll(list);
            }
        }

        return results;
    }

    /**
     * Returns all the activities of this log frame.
     * 
     * @return All the activities of this log frame.
     */
    public List<LogFrameActivityDTO> getAllActivitiesDTO() {

        final ArrayList<LogFrameActivityDTO> activities = new ArrayList<LogFrameActivityDTO>();

        // Retrieves the activities for each expected result.
        List<LogFrameActivityDTO> list;
        for (final ExpectedResultDTO result : getAllExpectedResultsDTO()) {
            if ((list = result.getActivitiesDTONotDeleted()) != null) {
                activities.addAll(list);
            }
        }

        return activities;
    }
}
