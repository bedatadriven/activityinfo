package org.sigmah.shared.dto.logframe;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.LogFrameModel.
 * 
 * @author tmi
 * 
 */
public class LogFrameModelDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -7816999376877639326L;

    /**
     * The default visibility policy for groups if the corresponding attribute
     * is missing.
     */
    private static final boolean DEFAULT_VISIBILITY_GROUP_POLICY = false;

    @Override
    public String getEntityName() {
        return "logframe.LogFrameModel";
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

    // Log frame name.
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // SO parameters.
    public Boolean getEnableSpecificObjectivesGroups() {
        final Boolean b = get("enableSpecificObjectivesGroups");
        return b != null ? b : DEFAULT_VISIBILITY_GROUP_POLICY;
    }

    public void setEnableSpecificObjectivesGroups(Boolean enableSpecificObjectivesGroups) {
        set("enableSpecificObjectivesGroups", enableSpecificObjectivesGroups);
    }

    public Integer getSpecificObjectivesMax() {
        return get("specificObjectivesMax");
    }

    public void setSpecificObjectivesMax(Integer specificObjectivesMax) {
        set("specificObjectivesMax", specificObjectivesMax);
    }

    public Integer getSpecificObjectivesGroupsMax() {
        return get("specificObjectivesGroupsMax");
    }

    public void setSpecificObjectivesGroupsMax(Integer specificObjectivesGroupsMax) {
        set("specificObjectivesGroupsMax", specificObjectivesGroupsMax);
    }

    public Integer getSpecificObjectivesPerGroupMax() {
        return get("specificObjectivesPerGroupMax");
    }

    public void setSpecificObjectivesPerGroupMax(Integer specificObjectivesPerGroupMax) {
        set("specificObjectivesPerGroupMax", specificObjectivesPerGroupMax);
    }

    // ER parameters.
    public Boolean getEnableExpectedResultsGroups() {
        final Boolean b = get("enableExpectedResultsGroups");
        return b != null ? b : DEFAULT_VISIBILITY_GROUP_POLICY;
    }

    public void setEnableExpectedResultsGroups(Boolean enableExpectedResultsGroups) {
        set("enableExpectedResultsGroups", enableExpectedResultsGroups);
    }

    public Integer getExpectedResultsMax() {
        return get("expectedResultsMax");
    }

    public void setExpectedResultsMax(Integer expectedResultsMax) {
        set("expectedResultsMax", expectedResultsMax);
    }

    public Integer getExpectedResultsGroupsMax() {
        return get("expectedResultsGroupsMax");
    }

    public void setExpectedResultsGroupsMax(Integer expectedResultsGroupsMax) {
        set("expectedResultsGroupsMax", expectedResultsGroupsMax);
    }

    public Integer getExpectedResultsPerGroupMax() {
        return get("expectedResultsPerGroupMax");
    }

    public void setExpectedResultsPerGroupMax(Integer expectedResultsPerGroupMax) {
        set("expectedResultsPerGroupMax", expectedResultsPerGroupMax);
    }

    public Integer getExpectedResultsPerSpecificObjectiveMax() {
        return get("expectedResultsPerSpecificObjectiveMax");
    }

    public void setExpectedResultsPerSpecificObjectiveMax(Integer expectedResultsPerSpecificObjectiveMax) {
        set("expectedResultsPerSpecificObjectiveMax", expectedResultsPerSpecificObjectiveMax);
    }

    // Activities parameters.
    public Boolean getEnableActivitiesGroups() {
        final Boolean b = get("enableActivitiesGroups");
        return b != null ? b : DEFAULT_VISIBILITY_GROUP_POLICY;
    }

    public void setEnableActivitiesGroups(Boolean enableActivitiesGroups) {
        set("enableActivitiesGroups", enableActivitiesGroups);
    }

    public Integer getActivitiesMax() {
        return get("activitiesMax");
    }

    public void setActivitiesMax(Integer activitiesMax) {
        set("activitiesMax", activitiesMax);
    }

    public Integer getActivitiesGroupsMax() {
        return get("activitiesGroupsMax");
    }

    public void setActivitiesGroupsMax(Integer activitiesGroupsMax) {
        set("activitiesGroupsMax", activitiesGroupsMax);
    }

    public Integer getActivitiesPerGroupMax() {
        return get("activitiesPerGroupMax");
    }

    public void setActivitiesPerGroupMax(Integer activitiesPerGroupMax) {
        set("activitiesPerGroupMax", activitiesPerGroupMax);
    }

    public Integer getActivitiesPerExpectedResultMax() {
        return get("activitiesPerExpectedResultMax");
    }

    public void setActivitiesPerExpectedResultMax(Integer activitiesPerExpectedResultMax) {
        set("activitiesPerExpectedResultMax", activitiesPerExpectedResultMax);
    }

    // Prerequisites parameters.
    public Boolean getEnablePrerequisitesGroups() {
        final Boolean b = get("enablePrerequisitesGroups");
        return b != null ? b : DEFAULT_VISIBILITY_GROUP_POLICY;
    }

    public void setEnablePrerequisitesGroups(Boolean enablePrerequisitesGroups) {
        set("enablePrerequisitesGroups", enablePrerequisitesGroups);
    }

    public Integer getPrerequisitesMax() {
        return get("prerequisitesMax");
    }

    public void setPrerequisitesMax(Integer prerequisitesMax) {
        set("prerequisitesMax", prerequisitesMax);
    }

    public Integer getPrerequisitesGroupsMax() {
        return get("prerequisitesGroupsMax");
    }

    public void setPrerequisitesGroupsMax(Integer prerequisitesGroupsMax) {
        set("prerequisitesGroupsMax", prerequisitesGroupsMax);
    }

    public Integer getPrerequisitesPerGroupMax() {
        return get("prerequisitesPerGroupMax");
    }

    public void setPrerequisitesPerGroupMax(Integer prerequisitesPerGroupMax) {
        set("prerequisitesPerGroupMax", prerequisitesPerGroupMax);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LogFrameModelDTO [");
        sb.append("entity name = ");
        sb.append(getEntityName());
        sb.append(" ; id = ");
        sb.append(getId());
        sb.append(" ; name = ");
        sb.append(getName());
        return sb.toString();
    }
}
