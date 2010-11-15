/*
 * All Sigmah code is released under the GNU General Public License v3 See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.element.FlexibleElementDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProjectModelDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8508466949743884046L;

    @Override
    public String getEntityName() {
        return "ProjectModel";
    }

    // Project model id
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

    // Root phase model DTO
    public PhaseModelDTO getRootPhaseModelDTO() {
        return get("rootPhaseModelDTO");
    }

    public void setRootPhaseModelDTO(PhaseModelDTO rootPhaseModelDTO) {
        set("rootPhaseModelDTO", rootPhaseModelDTO);
    }

    // Phase models list
    public List<PhaseModelDTO> getPhaseModelsDTO() {
        return get("phaseModelsDTO");
    }

    public void setPhaseModelsDTO(List<PhaseModelDTO> phaseModelsDTO) {
        set("phaseModelsDTO", phaseModelsDTO);
    }

    // Reference to the project banner
    public ProjectBannerDTO getProjectBannerDTO() {
        return get("projectBannerDTO");
    }

    public void setProjectBannerDTO(ProjectBannerDTO projectBannerDTO) {
        set("projectBannerDTO", projectBannerDTO);
    }

    // Reference to the project details
    public ProjectDetailsDTO getProjectDetailsDTO() {
        return get("projectDetailsDTO");
    }

    public void setProjectDetailsDTO(ProjectDetailsDTO projectDetailsDTO) {
        set("projectDetailsDTO", projectDetailsDTO);
    }

    // Flexible elements list
    public List<FlexibleElementDTO> getFlexibleElementsDTO() {
        return get("flexibleElementsDTO");
    }

    public void setFlexibleElementsDTO(List<FlexibleElementDTO> flexibleElementsDTO) {
        set("flexibleElementsDTO", flexibleElementsDTO);
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
     * @param organization
     *            The organization.
     * @return The type of this model for the given organization,
     *         <code>null</code> otherwise.
     */
    public ProjectModelType getVisibility(OrganizationDTO organization) {

        if (organization == null || getVisibilities() == null) {
            return null;
        }

        for (final ProjectModelVisibilityDTO visibility : getVisibilities()) {
            if (visibility.getOrganizationId() == organization.getId()) {
                return visibility.getType();
            }
        }

        return null;
    }
}
