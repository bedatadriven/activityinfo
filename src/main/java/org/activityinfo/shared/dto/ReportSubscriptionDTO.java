

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
 * Projection DTO for the {@link org.activityinfo.server.database.hibernate.entity.ReportSubscription ReportSubscription}
 * domain class.
 * 
 * A row in a list of users who can be invited to view a report.
 * Models a projection of <code>UserPermission</code>, <code>ReportTemplate</code>,
 * and <code>ReportProjection</code>.
 *
 * @author Alex Bertram
 */
public final class ReportSubscriptionDTO extends BaseModelData {

    public ReportSubscriptionDTO() {
    }

    public void setUserId(int userId) {
        set("userId", userId);
    }

    public int getUserId() {
        return (Integer) get("userId");
    }

    public String getUserEmail() {
        return get("userEmail");
    }

    public void setUserEmail(String email) {
        set("userEmail", email);
    }

    public void setUserName(String name) {
        set("userName", name);
    }

    public String getUserName() {
        return get("userName");
    }

    public Boolean isSubscribed() {
        return get("subscribed");
    }

    public void setSubscribed(Boolean subscribed) {
        set("subscribed", subscribed);
    }
    
}
