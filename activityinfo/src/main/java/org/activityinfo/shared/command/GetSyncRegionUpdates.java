/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.SyncRegionUpdate;

public class GetSyncRegionUpdates implements Command<SyncRegionUpdate> {
    private String regionId;
    private long localVersion;

    public GetSyncRegionUpdates() {
    }

    public GetSyncRegionUpdates(String regionId, long localVersion) {
        this.regionId = regionId;
        this.localVersion = localVersion;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public long getLocalVersion() {
        return localVersion;
    }

    public void setLocalVersion(long localVersion) {
        this.localVersion = localVersion;
    }
}
