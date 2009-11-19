package org.activityinfo.client.command.callback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.DownloadEvent;
import org.activityinfo.shared.command.result.RenderResult;

/**
 *
 * Handles <code>RenderResult</code> command calls.
 *
 * Upon success, starts the download of the renderended element
 * using the hidden iframe with name "_downloadFrame"
 * (should be defined in the hostpage) 
 *
 * @author Alex Bertram
 */
public class DownloadCallback implements AsyncCallback<RenderResult> {

    private final EventBus eventBus;

    public DownloadCallback(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void onFailure(Throwable caught) {

    }

    public void onSuccess(RenderResult result) {
        eventBus.fireEvent(new DownloadEvent(GWT.getModuleBaseURL() +  "download?"+ result.getUrl()));
    }
}
