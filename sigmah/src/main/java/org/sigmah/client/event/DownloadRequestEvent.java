/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import org.sigmah.client.page.DownloadManager;

/**
 * An event which requests the download of a file.
 *
 * @author Alex Bertram
 */
public class DownloadRequestEvent extends BaseEvent {
    private final String name;
    private final String url;

    /**
     * @param name the name of the download type, used only for usage tracking
     * @param url  the url from which to download the file
     */
    public DownloadRequestEvent(String name, String url) {
        super(DownloadManager.DownloadRequested);
        this.name = name;
        this.url = url;
    }

    /**
     * Creates an unnamed DownloadRequestEvent.
     *
     * @param url the url from which to download the file
     */
    public DownloadRequestEvent(String url) {
        super(DownloadManager.DownloadRequested);
        this.url = url;
        this.name = null;
    }

    /**
     *
     * @return the url from which to download the file
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the extension of the URL (the text following the final
     * ".") or an empty string if there is no dot. 
     *
     * @return the file extension of the URL
     */
    public String getUrlExtension() {
        if(url == null) {
            return null;
        }
        int dot = url.lastIndexOf(".");
        if(dot == -1) {
            return "";
        } else {
            return url.substring(dot + 1);
        }
    }

    /**
     * @return the name of the download, for usage-tracking purposes
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DownloadEvent{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
