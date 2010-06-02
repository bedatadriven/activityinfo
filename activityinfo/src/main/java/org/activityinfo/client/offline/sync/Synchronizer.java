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

package org.activityinfo.client.offline.sync;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.GetSyncRegions;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.command.result.SyncRegions;
import org.activityinfo.shared.sync.SyncRegion;

import java.sql.*;

public class Synchronizer {

    private final Dispatcher dispatch;
    private final Connection conn;
    private final BulkUpdater updater;

    private SyncRegions regions;

    @Inject
    public Synchronizer(Dispatcher dispatch, Connection conn, BulkUpdater updater) {
        this.conn = conn;
        this.dispatch = dispatch;
        this.updater = updater;
    }

    private void createTables() throws SQLException {
        Statement ddl = conn.createStatement();
        ddl.executeUpdate("create table if not exists sync_regions" +
                " (id TEXT, localVersion INTEGER) ");
    }

    public void start() throws SQLException {
        Log.info("Synchronizer: starting.");
        createTables();
        dispatch.execute(new GetSyncRegions(), null, new AsyncCallback<SyncRegions>() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Synchronizer: error getting sync regions", throwable);
            }

            @Override
            public void onSuccess(SyncRegions syncRegions) {
                Synchronizer.this.regions = syncRegions;
                doNextUpdate();
            }
        });
    }

    private void doNextUpdate() {
        for(final SyncRegion region : regions) {
            long localVersion = queryLocalVersion(region.getId());
            if(region.getCurrentVersion() > localVersion) {
                dispatch.execute(new GetSyncRegionUpdates(region.getId(), localVersion), null, new AsyncCallback<SyncRegionUpdate>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.error("GetSyncRegionUpdates for region id " + region.getId() + " failed.", throwable);
                    }

                    @Override
                    public void onSuccess(SyncRegionUpdate syncRegionUpdate) {
                        persistUpdates(syncRegionUpdate);
                    }
                });
            }
        }
    }

    private void persistUpdates(final SyncRegionUpdate update) {
        Log.debug("Synchronizer: got updates for region " + update.getRegionId() + ":\n " + update.getSql());
        updater.executeUpdates(update.getSql(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Synchronizer: async execution of region updates failed", throwable);
            }

            @Override
            public void onSuccess(Integer integer) {
                updateLocalVersion(update.getRegionId(), update.getVersion());
                doNextUpdate();
            }
        });
    }

    private long queryLocalVersion(String id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select localVersion from sync_regions where id = ?");
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Log.error("Synchronizer: exception thrown while querying local version", e);
            return 0;
        }
    }

    private void updateLocalVersion(String id, long version) {
        try {
            PreparedStatement update = conn.prepareStatement(
                    "update sync_region set localVersion = ? where id = ?");
            update.setLong(1, version);
            update.setString(2, id);
            if(update.executeUpdate() == 0) {
                PreparedStatement insert = conn.prepareStatement(
                        "insert into sync_region (id, localVersion) values (?, ?)");
                insert.setString(1, id);
                insert.setLong(2, version);
            }
        } catch (SQLException e) {
            Log.error("Syncrhonizer: SQL exeception thrown while updating local version number", e);
        }
    }

}
