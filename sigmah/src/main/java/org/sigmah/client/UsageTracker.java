/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.gears.client.Factory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.remote.cache.DefaultDispatchListener;
import org.sigmah.client.event.DownloadRequestEvent;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.DownloadManager;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CommandResult;

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
        eventBus.addListener(DownloadManager.DownloadRequested, new Listener<DownloadRequestEvent>() {
            public void handleEvent(DownloadRequestEvent event) {
                trackDownload(event);
            }
        });

        // Track successful creates by user
        commandEventSource.registerListener(CreateEntity.class, new DefaultDispatchListener<CreateEntity>() {
            @Override
            public void onSuccess(CreateEntity command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/create");
            }
        });
        // Track successful updates by user
        commandEventSource.registerListener(UpdateEntity.class, new DefaultDispatchListener<UpdateEntity>() {
            @Override
            public void onSuccess(UpdateEntity command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/update");
            }
        });
        // Track successful deletes by user
        commandEventSource.registerListener(Delete.class, new DefaultDispatchListener<Delete>() {
            @Override
            public void onSuccess(Delete command, CommandResult result) {
                trackPageView("/crud/" + command.getEntityName() + "/delete");
            }
        });
    }

    private boolean isGearsInstalled() {
        return Factory.getInstance() != null;
    }

    private void trackDownload(DownloadRequestEvent event) {
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
