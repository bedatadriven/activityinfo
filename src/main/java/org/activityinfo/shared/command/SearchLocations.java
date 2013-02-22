package org.activityinfo.shared.command;

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

import java.util.Collection;

import org.activityinfo.shared.command.result.LocationResult;

public class SearchLocations implements Command<LocationResult> {
    private Collection<Integer> adminEntityIds;
    private String name;
    private int threshold = 300;
    private int locationTypeId = 0;

    public int getThreshold() {
        return threshold;
    }

    public Collection<Integer> getAdminEntityIds() {
        return adminEntityIds;
    }

    public SearchLocations setAdminEntityIds(Collection<Integer> adminEntityIds) {
        this.adminEntityIds = adminEntityIds;
        return this;
    }

    public String getName() {
        return name;
    }

    public SearchLocations setName(String name) {
        this.name = name;
        return this;
    }

    public int getLocationTypeId() {
        return locationTypeId;
    }

    public SearchLocations setLocationTypeId(int locationTypeId) {
        this.locationTypeId = locationTypeId;
        return this;
    }
}
