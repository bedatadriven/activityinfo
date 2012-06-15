/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.callback;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.page.Downloader;
import org.activityinfo.shared.command.result.RenderResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
        Downloader.initiateDownload(GWT.getModuleBaseURL() + "download?" + result.getUrl());
    }
}
