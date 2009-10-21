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

package org.activityinfo.client.page;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.event.DownloadEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.inject.Singleton;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
@Singleton
public class DownloadManager {

    public static final EventType DownloadRequested = new EventBus.NamedEventType("DownloadRequested");


    private final EventBus eventBus;
    private final Authentication auth;

    @Inject
    public DownloadManager(EventBus eventBus, Authentication auth) {
        this.eventBus = eventBus;
        this.auth = auth;


        eventBus.addListener(DownloadRequested, new Listener<DownloadEvent>() {

            @Override
            public void handleEvent(DownloadEvent be) {
                initiateDownload(be.getUrl());
            }
        });

        attachIFrameListener(DOM.getElementById("_downloadFrame"), this);


    }

    public static native void attachIFrameListener(Element iframe, DownloadManager self) /*-{
        iframe.onload = function() { self.@org.activityinfo.client.page.DownloadManager::onLoad()(); };
        iframe.onerror = function() { self.@org.activityinfo.client.page.DownloadManager::onError()(); };
        iframe.onreadystatechange = function() {  self.@org.activityinfo.client.page.DownloadManager::onReadyStateChanged()(); };
    }-*/;

    private void initiateDownload(String url) {

        // perform substitutions
        url = url.replace("#AUTH#", auth.getAuthToken());

        // launch downlaod in IFRAME (so our app window is not disturbed
        // if the connection fails before the client receives the Content-disposition header
        Window.open(url, "_downloadFrame", null);

        // todo: feedback to user and monitor transfer, if possible.
    }

    public static void fireIFrameEvent(DownloadManager mgr) {
        mgr.onLoad();
    }

    private void onLoad() {
        GWT.log("DownloadManager: iframe onLoad()", null);
    }

    private void onError() {
        GWT.log("DownloadManager: iframe onError()", null);
    }

    private void onReadyStateChanged() {
        GWT.log("DownloadManager: iframe onReadyStateChange()", null);
    }


}

