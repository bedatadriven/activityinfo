/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.DownloadRequestEvent;

/**
 *
 * @author Alex Bertram
 */
@Singleton
public class DownloadManager {

    public static final EventType DownloadRequested = new EventBus.NamedEventType("DownloadRequested");
    private final Authentication auth;

    @Inject
    public DownloadManager(EventBus eventBus, Authentication auth) {
        this.auth = auth;

        eventBus.addListener(DownloadRequested, new Listener<DownloadRequestEvent>() {

            @Override
            public void handleEvent(DownloadRequestEvent be) {
                initiateDownload(be.getUrl());
            }
        });
    }

    private void initiateDownload(String url) {
        // perform substitutions
        url = url.replace("#AUTH#", auth.getAuthToken());

        // launch download in IFRAME (so our app window is not disturbed
        // if the connection fails before the client receives the Content-disposition header
        Window.open(url, "_downloadFrame", null);

        // todo: feedback to user and monitor transfer, if possible.
    }
}

