package org.sigmah.shared.dto;

import org.sigmah.shared.domain.ProjectModelType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity ProjectModelVisibility.
 * 
 * @author tmi
 * 
 */
public class ProjectModelVisibilityDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -4517698536716727232L;

    @Override
    public String getEntityName() {
        return "ProjectModelVisibility";
    }

    // Visibility id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Visibility type.
    public ProjectModelType getType() {
        return get("type");
    }

    public void setType(ProjectModelType type) {
        set("type", type);
    }

    // Visibility organization id.
    public Integer getOrganizationId() {
        return get("organizationId");
    }

    public void setOrganizationId(Integer organizationId) {
        set("organizationId", organizationId);
    }
}
