/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.callback;

import org.activityinfo.client.util.Downloader;
import org.activityinfo.shared.command.result.UrlResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles <code>UrlResult</code> command calls.
 * <p/>
 * Upon success, fires a DownloadRequestedEvent for the
 * resulting URL
 *
 * @author Alex Bertram
 */
public class DownloadCallback implements AsyncCallback<UrlResult> {

    private String downloadName;

    public DownloadCallback() {
    }

    public DownloadCallback(String downloadName) {
        this.downloadName = downloadName;
    }

    public void onFailure(Throwable caught) {

    }

    public void onSuccess(UrlResult result) {
    	Downloader downloader = GWT.create(Downloader.class);
        downloader.initiateDownload(GWT.getModuleBaseURL() + "download?" + result.getUrl());
    }
}
