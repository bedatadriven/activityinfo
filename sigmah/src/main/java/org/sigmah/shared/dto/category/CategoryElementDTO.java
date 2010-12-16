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

    // Color
    public String getColor() {
        return get("color");
    }

    public void setColor(String color) {
        set("color", color);
    }

    public void setIconHtml(String iconHtml) {
        set("iconHtml", iconHtml);
    }

    public String getIconHtml() {
        return get("iconHtml");
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof CategoryElementDTO)) {
            return false;
        }

        final CategoryElementDTO other = (CategoryElementDTO) obj;

        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
