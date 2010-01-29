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
import com.google.gwt.user.client.rpc.InvocationException;
import org.activityinfo.client.dispatch.AsyncMonitor;

/**
 * Convience wrapper for loading the Google Maps API
 *
 * @author Alex Bertram
 */
public class MapApiLoader {

    private static final int TIMEOUT = 30000;

    public static void load(final AsyncMonitor monitor, final AsyncCallback<Void> callback) {

        Log.debug("MapApiLoader: load()");

        if (monitor != null)
            monitor.beforeRequest();

        AjaxLoader.AjaxLoaderOptions opts = AjaxLoader.AjaxLoaderOptions.newInstance();
        opts.setLanguage(LocaleInfo.getCurrentLocale().getLocaleName());

        //     <script src="http://maps.google.com/maps?gwt=1&amp;file=api&amp;v=2&amp;key=" />

        String key = "ABQIAAAAHxDe2DM8fTqR3KT6JA3uSxQowpcadhvcZvF-AZHbBConS0qRQRSyDoOLQrw76pJeJNUXt2g-yC8FAg";

        Maps.loadMapsApi(key, "2", false, opts, new Runnable() {
            @Override
            public void run() {
                if (monitor != null) {
                    monitor.onCompleted();
                }
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
        // set up a timer to call onFailed() if things don't work out.
        if (callback != null || monitor != null) {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    if (!Maps.isLoaded()) {
                        if (monitor != null) {
                            monitor.onCompleted();
                        }
                        if (callback != null) {
                            //noinspection ThrowableInstanceNeverThrown
                            callback.onFailure(
                                    new InvocationException("API Loader timed out."));
                        }
                    }
                }
            };
            timer.schedule(TIMEOUT);
        }

    }

    public static void preload() {
        load(null, null);
    }
}
