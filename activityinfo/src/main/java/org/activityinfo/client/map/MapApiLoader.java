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

package org.activityinfo.client.map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.AsyncMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience wrapper for loading the Google Maps API asynchronously
 *
 * @author Alex Bertram
 */
public class MapApiLoader {
    private static final int TIMEOUT = 10 * 1000;
    private static final String API_KEY = "ABQIAAAAHxDe2DM8fTqR3KT6JA3uSxQowpcadhvcZvF-AZHbBConS0qRQRSyDoOLQrw76pJeJNUXt2g-yC8FAg";
    private static final String API_VERSION = "2";
    private static final boolean USING_SENSOR = false;

    private static boolean loadInProgress = false;
    private static List<AsyncMonitor> waitingMonitors;
    private static List<AsyncCallback> waitingCallbacks;

    public static void load(final AsyncMonitor monitor, final AsyncCallback<Void> callback) {
        if(Maps.isLoaded()) {
            if(monitor != null) {
                monitor.onCompleted();
            }
            if(callback != null) {
                callback.onSuccess(null);
            }
        } else {
            if(!loadInProgress) {
                startLoad();
            }
            addListeners(monitor, callback);
        }
        load();
    }

    public static void load() {
        Log.debug("MapApiLoader: load()");
        if(!Maps.isLoaded() && !loadInProgress) {
            startLoad();
        }
    }

    private static void startLoad() {
        loadInProgress = true;
        waitingMonitors = new ArrayList<AsyncMonitor>();
        waitingCallbacks = new ArrayList<AsyncCallback>();

        AjaxLoader.AjaxLoaderOptions options = AjaxLoader.AjaxLoaderOptions.newInstance();
        options.setLanguage(LocaleInfo.getCurrentLocale().getLocaleName());

        Maps.loadMapsApi(API_KEY, API_VERSION, USING_SENSOR, options, new Runnable() {
            @Override
            public void run() {
                onApiLoaded();
            }
        });
        startFailureTimer();
    }

    private static void startFailureTimer() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                if (!Maps.isLoaded()) {
                    onApiLoadFailure();
                }
            }
        };
        timer.schedule(TIMEOUT);
    }

    private static void onApiLoaded() {
        loadInProgress = false;
        for(AsyncMonitor monitor : waitingMonitors) {
            monitor.onCompleted();
        }
        for(AsyncCallback callback : waitingCallbacks) {
            callback.onSuccess(null);
        }
    }

    private static void onApiLoadFailure() {
        loadInProgress = false;
        for(AsyncMonitor monitor : waitingMonitors) {
            monitor.onCompleted();
        }
        for(AsyncCallback callback : waitingCallbacks) {
            callback.onSuccess(null);
        }
    }

    private static void addListeners(AsyncMonitor monitor, AsyncCallback<Void> callback) {
        if(monitor != null) {
            waitingMonitors.add(monitor);
        }
        if(callback != null) {
            waitingCallbacks.add(callback);
        }
    }
}
