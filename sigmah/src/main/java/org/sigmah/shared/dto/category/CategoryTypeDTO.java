package org.sigmah.shared.dto.category;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class CategoryTypeDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 4190439829705158136L;

    @Override
    public String getEntityName() {
        return "category.CategoryType";
    }

    // Type id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Type label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Category elements list
    public List<CategoryElementDTO> getCategoryElementsDTO() {
        return get("categoryElementsDTO");
    }

    public void setCategoryElementsDTO(List<CategoryElementDTO> categoryElementsDTO) {
        set("categoryElementsDTO", categoryElementsDTO);
    }
}
