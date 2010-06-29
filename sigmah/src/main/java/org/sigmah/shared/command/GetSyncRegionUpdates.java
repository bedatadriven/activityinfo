/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SyncRegionUpdate;

public class GetSyncRegionUpdates implements Command<SyncRegionUpdate> {
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
