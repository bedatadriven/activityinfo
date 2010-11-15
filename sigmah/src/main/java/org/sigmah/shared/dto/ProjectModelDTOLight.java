package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.domain.ProjectModel;
import org.sigmah.shared.domain.ProjectModelType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Light mapping class for {@link ProjectModel} entity. Only the id and the name
 * of the model are mapped.
 * 
 * @author tmi
 * 
 */
public class ProjectModelDTOLight extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -7198337856191082952L;

    @Override
    public String getEntityName() {
        return "ProjectModel";
    }

    // Project model id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Project model name
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
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
}
