/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

import org.sigmah.client.EventBus;
import org.sigmah.client.event.DownloadRequestEvent;
import org.sigmah.shared.auth.AuthenticatedUser;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author Alex Bertram
 */
@Singleton
public class DownloadManager {

    public static final EventType DownloadRequested = new EventBus.NamedEventType("DownloadRequested");
    private final AuthenticatedUser auth;

    @Inject
    public DownloadManager(EventBus eventBus, AuthenticatedUser auth) {
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
        Frame frame = new Frame(url);
        
        El el = El.fly(frame.getElement());
		el.setStyleAttribute("width", 0);
		el.setStyleAttribute("height", 0);
		el.setStyleAttribute("position", "absolute");
		el.setStyleAttribute("border", 0);
		
		DOM.sinkEvents(frame.getElement(), Event.ONLOAD);
		DOM.setEventListener(frame.getElement(), new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				GWT.log("Finished!");
			}
		});
		
		RootPanel.get().add(frame);
        
         // todo: feedback to user and monitor transfer, if possible.
    }
}

