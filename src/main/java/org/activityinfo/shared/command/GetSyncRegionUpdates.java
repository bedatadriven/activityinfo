

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

import org.activityinfo.shared.command.result.SyncRegionUpdate;

public class GetSyncRegionUpdates implements Command<SyncRegionUpdate>  {
    private String regionId;
    private String localVersion;

    public GetSyncRegionUpdates() {
    }

    public GetSyncRegionUpdates(String regionId, String localVersion) {
        this.regionId = regionId;
        this.localVersion = localVersion;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getLocalVersion() {
        return localVersion;
    }

    public void setLocalVersion(String localVersion) {
        this.localVersion = localVersion;
    }
}
