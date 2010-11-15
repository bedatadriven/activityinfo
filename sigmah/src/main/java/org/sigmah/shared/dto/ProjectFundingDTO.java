package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Light DTO mapping class for entity ProjectFunding.
 * 
 * @author tmi
 * 
 */
public class ProjectFundingDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -191315535238325514L;

    @Override
    public String getEntityName() {
        return "ProjectFunding";
    }

    // Funding id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Funding projects
    public ProjectDTOLight getFunding() {
        return get("funding");
    }

    public void setFunding(ProjectDTOLight funding) {
        set("funding", funding);
    }

    // Funded projects.
    public ProjectDTOLight getFunded() {
        return get("funded");
    }

    public void setFunded(ProjectDTOLight funded) {
        set("funded", funded);
    }

    // Funding percentage.
    public Double getPercentage() {
        return (Double) get("percentage");
    }

    public void setPercentage(Double percentage) {
        set("percentage", percentage);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ProjectFundingDTO)) {
            return false;
        }

        final ProjectFundingDTO other = (ProjectFundingDTO) obj;

        return getId() == other.getId();
    }
}
