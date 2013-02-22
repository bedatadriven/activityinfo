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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.appcache.client.events.ProgressEventHandler;
import com.bedatadriven.rebar.async.AsyncCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AppCacheSynchronizer implements ProgressEventHandler, AsyncCommand {

    private final EventBus eventBus;
    private final AppCache appCache = AppCacheFactory.get();

    private static final Logger LOGGER = Logger
        .getLogger(AppCacheSynchronizer.class.getName());

    @Inject
    public AppCacheSynchronizer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void execute(final AsyncCallback<Void> callback) {

        final HandlerRegistration progressRegistration = appCache
            .addProgressHandler(this);
        appCache.ensureUpToDate(new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                if (appCache.getStatus() == Status.UPDATE_READY) {
                    callback.onFailure(new AppOutOfDateException());
                } else {
                    progressRegistration.removeHandler();
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.log(Level.SEVERE, "Exception in AppCache Synchronizer",
                    caught);
                progressRegistration.removeHandler();
                callback.onFailure(new SynchronizerConnectionException(caught));
            }
        });
    }

    @Override
    public void onProgress(int filesComplete, int filesTotal) {
        eventBus.fireEvent(new SyncStatusEvent(I18N.CONSTANTS
            .appCacheProgress(),
            (double) filesComplete / (double) filesTotal * 100d));
    }
}
