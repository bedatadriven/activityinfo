package org.sigmah.shared.dto;

import org.sigmah.shared.dto.layout.LayoutDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class OrgUnitDetailsDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 4611350969297280470L;

    @Override
    public String getEntityName() {
        return "OrgUnitDetails";
    }

    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Layout
    public LayoutDTO getLayout() {
        return get("layout");
    }

    public void setLayout(LayoutDTO layout) {
        set("layout", layout);
    }

    // Model
    public OrgUnitModelDTO getOrgUnitModel() {
        return get("oum");
    }

    public void setOrgUnitModel(OrgUnitModelDTO oum) {
        set("oum", oum);
    }
}
