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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public final class DatabaseDTO extends BaseModelData implements DTO {

    private UserDatabaseDTO database = new UserDatabaseDTO();

    public DatabaseDTO() {
    }

    public UserDatabaseDTO getDatabase() {
        return database;
    }

    public void setDatabase(UserDatabaseDTO database) {
        this.database = database;
    }

    public List<ActivityDTO> getActivities() {

        List<ActivityDTO> publicActivity = new ArrayList<ActivityDTO>();

        List<ActivityDTO> activities = database.getActivities();
        for (ActivityDTO e : activities) {
            if (e != null) {
                publicActivity.add(e);
            }
        }

        return publicActivity;
    }

}
