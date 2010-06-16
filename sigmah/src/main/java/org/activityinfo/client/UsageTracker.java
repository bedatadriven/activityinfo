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
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.gears.client.Factory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.remote.cache.DefaultCommandListener;
import org.activityinfo.client.event.DownloadEvent;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.DownloadManager;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;

/**
 * Tracks usage of the application and reports to Google Analytics
 *
 * @author Alex Bertram
 */
@Singleton
public class UsageTracker {

    private static final int VISITOR_SCOPE = 1;
    private static final int SESSION_SCOPE = 2;
    private static final int PAGE_SCOPE = 3;

    @Inject
    public UsageTracker(EventBus eventBus, DispatchEventSource commandEventSource) {

        // Note whether this user has gears installed
        setCustomVar(1, "gears", isGearsInstalled() ? "installed" : "not installed", VISITOR_SCOPE);

        // Track internal page movements
        eventBus.addListener(NavigationHandler.NavigationAgreed, new Listener<NavigationEvent>() {
            public void handleEvent(NavigationEvent event) {
                trackPageView(event.getPlace().getPageId().toString());
            }
        });
        // Track downloads
        eventBus.addListener(DownloadManager.DownloadRequested, new Listener<DownloadEvent>() {
            public void handleEvent(DownloadEvent event) {
                trackDownload(event);
            }
        });

        // Track successful creates by user
        commandEventSource.registerListener(CreateEntity.class, new DefaultCommandListener<CreateEntity>() {
            @Override
            public void onSuccess(CreateEntity command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/create");
            }
        });
        // Track successful updates by user
        commandEventSource.registerListener(UpdateEntity.class, new DefaultCommandListener<UpdateEntity>() {
            @Override
            public void onSuccess(UpdateEntity command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/update");
            }
        });
        // Track successful deletes by user
        commandEventSource.registerListener(Delete.class, new DefaultCommandListener<Delete>() {
            @Override
            public void onSuccess(Delete command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/delete");
            }
        });
    }

    private boolean isGearsInstalled() {
        return Factory.getInstance() != null;
    }

    private void trackDownload(DownloadEvent event) {
        StringBuilder pageName = new StringBuilder("download/")
                .append(event.getName() == null ? "unnamed" : event.getName());
        setCustomVar(1, "filetype", event.getUrlExtension(), PAGE_SCOPE);
        trackPageView(pageName.toString());
    }

    private void trackPageView(String pageName) {
        Log.trace("Pageview tracked: " + pageName);
        try {
            doTrackPageView(pageName);
        } catch (JavaScriptException e) {
            Log.error("pageTracker.trackPageview() threw exception", e);
        }
    }

    private native void doTrackPageView(String pageName) /*-{
        $wnd.pageTracker._trackPageview(pageName);
    }-*/;


    private void setCustomVar(int slot, String variableName, String value, int scope) {
        try {
            doSetCustomVar(slot, variableName, value, scope);
        } catch (JavaScriptException e) {
            Log.error("pageTracker.setCustomVar() threw exception", e);
        }
    }

    private native void doSetCustomVar(int slot, String variableName, String value, int scope) /*-{
        $wnd.pageTracker._setCustomVar(slot, variableName, value, scope);
    }-*/;
}
