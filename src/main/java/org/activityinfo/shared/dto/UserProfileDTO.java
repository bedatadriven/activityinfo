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

public final class UserProfileDTO extends BaseModelData implements DTO {
    private static final long serialVersionUID = -1308866678724041573L;

    public UserProfileDTO() {
        super();
    }

    public UserProfileDTO(int userId) {
        super();
        this.setUserId(userId);
    }

    public void setUserId(int userId) {
        set("userId", userId);
    }

    public int getUserId() {
        return (Integer) get("userId");
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public String getEmail() {
        return (String) get("email");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setOrganization(String organization) {
        set("organization", organization);
    }

    public String getOrganization() {
        return (String) get("organization");
    }

    public void setJobtitle(String jobtitle) {
        set("jobtitle", jobtitle);
    }

    public String getJobtitle() {
        return (String) get("jobtitle");
    }

    public void setLocale(String locale) {
        set("locale", locale);
    }

    public String getLocale() {
        return (String) get("locale");
    }

    public void setEmailNotification(boolean emailNotification) {
        set("emailNotification", emailNotification);
    }

    public boolean isEmailNotification() {
        Object b = get("emailNotification");
        return b != null ? (Boolean) b : false;
    }
}
