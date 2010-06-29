/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import org.json.JSONException;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

public interface UpdateBuilder {

    SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException;

}
