package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity Organization.
 * 
 * @author tmi
 * 
 */
public class OrganizationDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8285349034203126628L;

    @Override
    public String getEntityName() {
        return "Organization";
    }

    // Organization id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Organization name.
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Organization logo path.
    public String getLogo() {
        return get("logo");
    }

    public void setLogo(String logo) {
        set("logo", logo);
    }
}
