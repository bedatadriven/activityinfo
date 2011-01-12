package org.sigmah.shared.dto.profile;

import java.util.Map;
import java.util.Set;

import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.domain.profile.PrivacyGroupPermissionEnum;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity profile.Profile.
 * 
 * @author tmi
 * 
 */
public class ProfileDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 4319548689359747450L;

    @Override
    public String getEntityName() {
        return "profile.Profile";
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

    // Name.
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Global permissions.
    public Set<GlobalPermissionEnum> getGlobalPermissions() {
        return get("globalPermissions");
    }

    public void setGlobalPermissions(Set<GlobalPermissionEnum> globalPermissions) {
        set("globalPermissions", globalPermissions);
    }

    // Privacy groups.
    public Map<PrivacyGroupDTO, PrivacyGroupPermissionEnum> getPrivacyGroups() {
        return get("privacyGroups");
    }

    public void setPrivacyGroups(Map<PrivacyGroupDTO, PrivacyGroupPermissionEnum> privacyGroups) {
        set("privacyGroups", privacyGroups);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        sb.append("Profile: ");
        sb.append(getName());
        sb.append("\n\tGlobal permissions: ");
        for (final GlobalPermissionEnum perm : getGlobalPermissions()) {
            sb.append(perm.name());
            sb.append(" | ");
        }
        sb.append("\n\tPrivacy groups: ");
        for (final Map.Entry<PrivacyGroupDTO, PrivacyGroupPermissionEnum> perm : getPrivacyGroups().entrySet()) {
            sb.append(perm.getKey().getTitle());
            sb.append(" - ");
            sb.append(perm.getValue().name());
            sb.append(" | ");
        }

        return sb.toString();
    }
}
