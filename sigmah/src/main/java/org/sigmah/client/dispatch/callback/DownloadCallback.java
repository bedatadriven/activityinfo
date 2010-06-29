/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.callback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.DownloadEvent;
import org.sigmah.shared.command.result.RenderResult;

/**
 * Handles <code>RenderResult</code> command calls.
 * <p/>
 * Upon success, fires a DownloadRequestedEvent for the
 * resulting URL
 *
 * @author Alex Bertram
 */
public class DownloadCallback implements AsyncCallback<RenderResult> {

    private final EventBus eventBus;
    private String downloadName;

    public DownloadCallback(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public DownloadCallback(EventBus eventBus, String downloadName) {
        this.eventBus = eventBus;
        this.downloadName = downloadName;
    }

    public void onFailure(Throwable caught) {

    }

    public void onSuccess(RenderResult result) {
        eventBus.fireEvent(new DownloadEvent(downloadName, GWT.getModuleBaseURL() + "download?" + result.getUrl()));
    }
}
