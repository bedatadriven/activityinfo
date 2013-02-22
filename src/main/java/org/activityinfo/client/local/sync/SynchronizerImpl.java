package org.activityinfo.client.local.sync;

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

import java.util.Date;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Log;
import org.activityinfo.client.local.command.LocalDispatcher;
import org.activityinfo.client.local.sync.pipeline.InstallPipeline;
import org.activityinfo.client.local.sync.pipeline.SyncPipeline;
import org.activityinfo.shared.command.Command;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SynchronizerImpl implements Synchronizer {

    private static final int AUTO_SYNC_INTERVAL_MS = 5 * 60 * 1000;

    private final LocalDispatcher localDispatcher;
    private final SchemaMigration migrator;
    private final InstallPipeline installPipeline;
    private final SyncPipeline syncPipeline;
    private SyncHistoryTable historyTable;

    @Inject
    public SynchronizerImpl(
        EventBus eventBus,
        LocalDispatcher localDispatcher,
        InstallPipeline installPipeline,
        SyncPipeline syncPipeline,
        SyncHistoryTable historyTable,
        SchemaMigration migrator) {
        this.localDispatcher = localDispatcher;
        this.migrator = migrator;
        this.installPipeline = installPipeline;
        this.syncPipeline = syncPipeline;
        this.historyTable = historyTable;

        eventBus.addListener(SyncRequestEvent.TYPE, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                SynchronizerImpl.this.syncPipeline.start();
            }
        });

    }

    @Override
    public void install(final AsyncCallback<Void> callback) {
        Log.trace("SynchronizerImpl.install() starting...");
        installPipeline.start(callback);
    }

    @Override
    public void getLastSyncTime(AsyncCallback<java.util.Date> callback) {
        historyTable.get(callback);
    }

    @Override
    public void validateOfflineInstalled(final AsyncCallback<Void> callback) {
        historyTable.get(new AsyncCallback<Date>() {

            @Override
            public void onSuccess(Date result) {
                if (result == null) {
                    callback.onFailure(new RuntimeException(
                        "Never synchronized"));
                } else {
                    // apply any changes made to the schema
                    migrator.migrate(callback);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    @Override
    public void synchronize() {
        syncPipeline.start(new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                scheduleNext();
            }

            @Override
            public void onFailure(Throwable caught) {
                scheduleNext();
            }
        });
    }

    private void scheduleNext() {
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            @Override
            public boolean execute() {
                synchronize();
                return false;
            }
        }, AUTO_SYNC_INTERVAL_MS);
    }

    @Override
    public void execute(Command command, AsyncCallback callback) {
        localDispatcher.execute(command, callback);
    }
}
