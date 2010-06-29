/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.GetSyncRegions;
import org.sigmah.shared.command.result.SyncRegion;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.command.result.SyncRegions;

import java.sql.*;
import java.util.Iterator;

public class Synchronizer {

    private final Dispatcher dispatch;
    private final Connection conn;
    private final BulkUpdaterAsync updater;
    private final Authentication auth;

    private Iterator<SyncRegion> regionIt;

    private AsyncCallback<Void> callback;

    @Inject
    public Synchronizer(Dispatcher dispatch, Connection conn, BulkUpdaterAsync updater, Authentication auth) {
        this.conn = conn;
        this.dispatch = dispatch;
        this.updater = updater;
        this.auth = auth;
    }

    private void createTables() throws SQLException {
        Statement ddl = conn.createStatement();
        ddl.executeUpdate("create table if not exists sync_regions" +
                " (id TEXT, localVersion INTEGER) ");
    }

    public void start(AsyncCallback<Void> callback) {
        this.callback = callback;
        start();
    }

    public void start() {
        Log.info("Synchronizer: starting.");
        try {
            createTables();
            dispatch.execute(new GetSyncRegions(), null, new AsyncCallback<SyncRegions>() {
                @Override
                public void onFailure(Throwable throwable) {
                    handleException("Error getting sync regions", throwable);
                }

                @Override
                public void onSuccess(SyncRegions syncRegions) {
                    Synchronizer.this.regionIt = syncRegions.iterator();
                    doNextUpdate();
                }
            });
        } catch (SQLException e) {
            handleException("Exception thrown whilst creating tables", e);
        }
    }

    private void doNextUpdate() {
        if(regionIt.hasNext()) {
            final SyncRegion region = regionIt.next();
            String localVersion = queryLocalVersion(region.getId());

            Log.info("Synchronizer: Region " + region.getId() + ": localVersion=" + localVersion);

            dispatch.execute(new GetSyncRegionUpdates(region.getId(), localVersion), null, new AsyncCallback<SyncRegionUpdate>() {
                @Override
                public void onFailure(Throwable throwable) {
                    handleException("GetSyncRegionUpdates for region id " + region.getId() + " failed.", throwable);
                }

                @Override
                public void onSuccess(SyncRegionUpdate update) {
                    persistUpdates(region, update);
                    doNextUpdate();
                }
            });
        } else {
            if(callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    private void persistUpdates(final SyncRegion region, final SyncRegionUpdate update) {
        Log.debug("Synchronizer: got updates for region " + region.getId());
        if(update.getSql() != null) {
            executeUpdates(region, update);
        } else {
            updateLocalVersion(region.getId(), update.getVersion());
            doNextUpdate();
        }
    }

    private void executeUpdates(final SyncRegion region, final SyncRegionUpdate update) {
        updater.executeUpdates(auth.getLocalDbName(), update.getSql(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleException("Async execution of region updates failed. \nSQL=" + update.getSql() +
                        "\nMessage: " + throwable.getMessage(), throwable);
            }

            @Override
            public void onSuccess(Integer rows) {
                Log.debug("Synchronizer: updates succeeded, " + rows + " row(s) affected");
                updateLocalVersion(region.getId(), update.getVersion());
                doNextUpdate();
            }
        });
    }

    private String queryLocalVersion(String id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select localVersion from sync_regions where id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleException("Exception thrown while querying local version", e);
            throw new RuntimeException(e);
        }
    }

    private void updateLocalVersion(String id, String version) {
        try {
            PreparedStatement update = conn.prepareStatement(
                    "update sync_regions set localVersion = ? where id = ?");
            update.setString(1, version);
            update.setString(2, id);
            if(update.executeUpdate() == 0) {
                PreparedStatement insert = conn.prepareStatement(
                        "insert into sync_regions (id, localVersion) values (?, ?)");
                insert.setString(1, id);
                insert.setString(2, version);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            handleException("SQL exception thrown while updating local version number", e);
        }
    }

    private void handleException(String message, Throwable throwable) {
        Log.error("Synchronizer: " + message, throwable);
        if(callback != null) {
            callback.onFailure(throwable);
        }
    }
}
