package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.DownloadEvent;

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
        eventBus.fireEvent(new DownloadEvent("../download?"+ result.getUrl()));
    }
}
