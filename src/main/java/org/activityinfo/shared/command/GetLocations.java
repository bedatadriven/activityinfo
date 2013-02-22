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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.GetLocations.GetLocationsResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.LocationDTO;

public class GetLocations implements Command<GetLocationsResult> {
    private static final long serialVersionUID = 6998517531187983518L;

    private List<Integer> locationIds;

    public GetLocations() {
        locationIds = new ArrayList<Integer>();
    }

    public GetLocations(Integer id) {
        locationIds = new ArrayList<Integer>();
        if (id != null) {
            locationIds.add(id);
        }
    }

    public GetLocations(List<Integer> ids) {
        locationIds = ids;
    }

    public List<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Integer> ids) {
        locationIds = ids;
    }

    public boolean hasLocationIds() {
        return (locationIds != null && locationIds.size() > 0);
    }

    public static class GetLocationsResult implements CommandResult {
        private static final long serialVersionUID = 4418411241161619590L;

        private List<LocationDTO> locations = new ArrayList<LocationDTO>();

        public GetLocationsResult() {
        }

        public GetLocationsResult(List<LocationDTO> locations) {
            if (locations != null) {
                this.locations = locations;
            }
        }

        public List<LocationDTO> getLocations() {
            return locations;
        }

        public boolean hasLocations() {
            return (locations != null && locations.size() > 0);
        }

        public LocationDTO getLocation() {
            if (locations != null && locations.size() > 0) {
                return locations.get(0);
            } else {
                return null;
            }
        }
    }
}
