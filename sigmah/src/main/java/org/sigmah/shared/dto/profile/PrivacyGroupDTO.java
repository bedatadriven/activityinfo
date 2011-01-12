package org.sigmah.shared.dto.profile;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity profile.PrivacyGroup.
 * 
 * @author tmi
 * 
 */
public class PrivacyGroupDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -8951877538079370046L;

    @Override
    public String getEntityName() {
        return "profile.PrivacyGroup";
    }

    // Id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Code.
    public Integer getCode() {
        return get("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    // Title.
    public String getTitle() {
        return get("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PrivacyGroupDTO)) {
            return false;
        }

        final PrivacyGroupDTO other = (PrivacyGroupDTO) obj;
        return getId() == other.getId();
    }
}
