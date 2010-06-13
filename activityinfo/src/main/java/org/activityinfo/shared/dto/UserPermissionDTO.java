package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * Projection DTO of the {@link org.activityinfo.server.domain.UserPermission UserPermission}
 * domain object
 *
 * @author Alex Bertram
 */
public final class UserPermissionDTO extends BaseModel implements DTO {

    public UserPermissionDTO() {
        setAllowView(true);
        setAllowViewAll(false);
        setAllowEdit(false);
        setAllowEditAll(false);
        setAllowManageUsers(false);
        setAllowManageAllUsers(false);
        setAllowDesign(false);
    }

    public void setName(String value) {
        set("name", value);
    }

    /**
     * Returns the User's name.
     *
     * @return the user's name
     */
    public String getName() {
        return get("name");
    }

    public void setEmail(String value) {
        set("email", value);
    }

    /**
     * Returns the User's email
     *
     * @return the User's email
     */
    public String getEmail() {
        return get("email");
    }

    public void setAllowView(boolean value) {
        set("allowView", value);
    }

    public void setAllowDesign(boolean value) {
        set("allowDesign", value);
    }

    public boolean getAllowDesign() {
        return (Boolean) get("allowDesign");
    }

    public boolean getAllowView() {
        return (Boolean) get("allowView");
    }

    public void setAllowViewAll(boolean value) {
        set("allowViewAll", value);
    }

    public boolean getAllowViewAll() {
        return (Boolean) get("allowViewAll");
    }

    public void setAllowEdit(boolean value) {
        set("allowEdit", value);
    }

    public boolean getAllowEdit() {
        return (Boolean) get("allowEdit");
    }

    public void setAllowEditAll(boolean value) {
        set("allowEditAll", value);
    }

    public boolean getAllowEditAll() {
        return (Boolean) get("allowEditAll");
    }

    public boolean getAllowManageUsers() {
        return (Boolean) get("allowManageUsers");
    }

    public void setAllowManageUsers(boolean allowManageUsers) {
        set("allowManageUsers", allowManageUsers);
    }

    public boolean getAllowManageAllUsers() {
        return (Boolean) get("allowManageAllUsers");
    }

    public void setAllowManageAllUsers(boolean allowManageAll) {
        set("allowManageAllUsers", allowManageAll);
    }

    public PartnerDTO getPartner() {
        return get("partner");
    }

    public void setPartner(PartnerDTO value) {
        set("partner", value);
    }
}
