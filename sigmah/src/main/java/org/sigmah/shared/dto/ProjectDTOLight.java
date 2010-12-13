package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sigmah.client.util.DateUtils;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.category.CategoryTypeDTO;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Light DTO mapping class for entity Project.
 * 
 * @author tmi
 * 
 */
public class ProjectDTOLight extends BaseTreeModel implements EntityDTO {

    private static final long serialVersionUID = -4898072895587927460L;

    @Override
    public String getEntityName() {
        return "Project";
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
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Project full name
    public String getFullName() {
        return get("fullName");
    }

    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    // Complete name
    public String getCompleteName() {
        return get("completeName");
    }

    public void setCompleteName(String completeName) {
        set("completeName", completeName);
    }

    public void generateCompleteName() {
        setCompleteName(getName() + " - " + getFullName());
    }

    // Reference to the current phase
    public PhaseDTO getCurrentPhaseDTO() {
        return get("currentPhaseDTO");
    }

    public void setCurrentPhaseDTO(PhaseDTO currentPhaseDTO) {
        set("currentPhaseDTO", currentPhaseDTO);
    }

    // Project visibilities
    public List<ProjectModelVisibilityDTO> getVisibilities() {
        return get("visibilities");
    }

    public void setVisibilities(List<ProjectModelVisibilityDTO> visibilities) {
        set("visibilities", visibilities);
    }

    /**
     * Gets the type of this model for the given organization. If this model
     * isn't visible for this organization, <code>null</code> is returned.
     * 
     * @param organizationId
     *            The organization.
     * @return The type of this model for the given organization,
     *         <code>null</code> otherwise.
     */
    public ProjectModelType getVisibility(int organizationId) {

        if (getVisibilities() == null) {
            return null;
        }

        for (final ProjectModelVisibilityDTO visibility : getVisibilities()) {
            if (visibility.getOrganizationId() == organizationId) {
                return visibility.getType();
            }
        }

        return null;
    }

    // Budget
    public Double getPlannedBudget() {
        final Double b = (Double) get("plannedBudget");
        return b != null ? b : 0.0;
    }

    public void setPlannedBudget(Double plannedBudget) {
        set("plannedBudget", plannedBudget);
    }

    public Double getSpendBudget() {
        final Double b = (Double) get("spendBudget");
        return b != null ? b : 0.0;
    }

    public void setSpendBudget(Double spendBudget) {
        set("spendBudget", spendBudget);
    }

    public Double getReceivedBudget() {
        final Double b = (Double) get("receivedBudget");
        return b != null ? b : 0.0;
    }

    public void setReceivedBudget(Double receivedBudget) {
        set("receivedBudget", receivedBudget);
    }

    // Org Unit
    public String getOrgUnitName() {
        return get("orgUnitName");
    }

    public void setOrgUnitName(String orgUnitName) {
        set("orgUnitName", orgUnitName);
    }

    // Project start date
    public Date getStartDate() {
        return get("startDate");
    }

    public void setStartDate(Date startDate) {
        set("startDate", startDate);
    }

    // Project end date
    public Date getEndDate() {
        return get("endDate");
    }

    public void setEndDate(Date endDate) {
        set("endDate", endDate);
    }

    // Starred project.
    public void setStarred(Boolean starred) {
        set("starred", starred);
    }

    public Boolean getStarred() {
        final Boolean b = (Boolean) get("starred");
        return b == null ? false : b;
    }

    // Closed date.
    public void setCloseDate(Date closeDate) {
        set("closeDate", closeDate);
    }

    public Date getCloseDate() {
        return get("closeDate");
    }

    public boolean isClosed() {
        return getCloseDate() != null;
    }

    // Categories.
    public List<CategoryTypeDTO> getCategories() {
        return get("categories");
    }

    public void setCategories(List<CategoryTypeDTO> categories) {
        set("categories", categories);
    }

    // Children (projects funded by this project)
    public List<ProjectDTOLight> getChildrenProjects() {
        return get("childrenProjects");
    }

    public void setChildrenProjects(List<ProjectDTOLight> childrenProjects) {

        // Base tree model.
        final ArrayList<ModelData> children = new ArrayList<ModelData>(childrenProjects);
        setChildren(children);

        set("childrenProjects", childrenProjects);
    }

    /**
     * Gets the type of this model for the given organization. If this model
     * isn't visible for this organization, <code>null</code> is returned.
     * 
     * @param organizationId
     *            The organization id.
     * @return The type of this model for the given organization,
     *         <code>null</code> otherwise.
     */
    public ProjectModelType getProjectModelType(int organizationId) {

        if (getVisibilities() == null) {
            return null;
        }

        for (final ProjectModelVisibilityDTO visibility : getVisibilities()) {
            if (visibility.getOrganizationId() == organizationId) {
                return visibility.getType();
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ProjectDTOLight)) {
            return false;
        }

        final ProjectDTOLight other = (ProjectDTOLight) obj;

        return getId() == other.getId();
    }

    /**
     * Gets the percentage of the elapsed time for the given project.
     * 
     * @param model
     *            The project.
     * @return The percentage of the elapsed time.
     */
    @SuppressWarnings("deprecation")
    public double getElapsedTime() {

        final double ratio;
        final Date start = getStartDate();
        final Date end = getEndDate();
        final Date close = getCloseDate();
        final Date today = new Date();
        final Date comparison;

        if (isClosed()) {
            comparison = new Date(close.getYear(), close.getMonth(), close.getDate());
        } else {
            comparison = new Date(today.getYear(), today.getMonth(), today.getDate());
        }

        // No end date
        if (end == null) {
            ratio = 0d;
        }
        // No start date but with a end date.
        else if (start == null) {

            if (DateUtils.DAY_COMPARATOR.compare(comparison, end) < 0) {
                ratio = 0d;
            } else {
                ratio = 100d;
            }
        }
        // Start date and end date.
        else {

            // The start date is after the end date -> 100%.
            if (DateUtils.DAY_COMPARATOR.compare(start, end) >= 0) {
                ratio = 100d;
            }
            // The start date is after today -> 0%.
            else if (DateUtils.DAY_COMPARATOR.compare(comparison, start) <= 0) {
                ratio = 0d;
            }
            // The start date is before the end date -> x%.
            else {
                final Date sd = new Date(start.getYear(), start.getMonth(), start.getDate());
                final Date ed = new Date(end.getYear(), end.getMonth(), end.getDate());
                final double elapsedTime = comparison.getTime() - sd.getTime();
                final double estimatedTime = ed.getTime() - sd.getTime();
                ratio = NumberUtils.ratio(elapsedTime, estimatedTime);
            }
        }

        return NumberUtils.adjustRatio(ratio);
    }

    /**
     * Gets all the categories type of this project.
     * 
     * @return The categories type as string.
     */
    public String getCategoriesString() {

        final List<CategoryTypeDTO> categories = getCategories();

        final StringBuilder sb = new StringBuilder();

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                sb.append(categories.get(i).getLabel());
                if (i + 1 != categories.size()) {
                    sb.append(" / ");
                }
            }
        }

        return sb.toString();
    }
}
