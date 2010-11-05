package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity PhaseModelDefinition.
 * 
 * @author tmi
 * 
 */
public class PhaseModelDefinitionDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 1745325814814487880L;

    @Override
    public String getEntityName() {
        return "PhaseModelDefinition";
    }

    // Definition id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }
}
