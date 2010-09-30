package org.sigmah.shared.dto.category;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class CategoryElementDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 7879245182808843730L;

    @Override
    public String getEntityName() {
        return "category.CategoryElement";
    }

    // Element id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Element label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Reference to the parent category type
    public CategoryTypeDTO getParentCategoryDTO() {
        return get("parentCategoryDTO");
    }

    public void setParentCategoryDTO(CategoryTypeDTO parentCategoryDTO) {
        set("parentCategoryDTO", parentCategoryDTO);
    }
}
