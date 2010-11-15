package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.domain.ProjectModelType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Light DTO mapping class for entity Project.
 * 
 * @author tmi
 * 
 */
public class ProjectDTOLight extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -4898072895587927460L;

    public ProjectDTOLight() {
        // TODO remove this.
        setFavorite(false);
    }

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

    // Favorite
    public boolean isFavorite() {
        return get("favorite");
    }

    public void setFavorite(boolean favorite) {
        set("favorite", favorite);
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
}
