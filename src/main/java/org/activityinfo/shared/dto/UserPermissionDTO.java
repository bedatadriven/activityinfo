package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Projection DTO of the
 * {@link org.activityinfo.server.database.hibernate.entity.UserPermission
 * UserPermission} domain object
 * 
 * @author Alex Bertram
 */
public final class UserPermissionDTO extends BaseModelData implements DTO {

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

    public String getFirstName() {
        return get("firstName");
    }

    public void setFirstName(String firstName) {
        set("firstName", firstName);
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
