package org.sigmah.shared.dto;


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
}
