/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import org.sigmah.client.page.DownloadManager;

/**
 * @author Alex Bertram
 */
public class DownloadEvent extends BaseEvent {

    private String name;
    private String url;

    public DownloadEvent(String name, String url) {
        super(DownloadManager.DownloadRequested);
        this.name = name;
        this.url = url;
    }

    public DownloadEvent(String url) {
        super(DownloadManager.DownloadRequested);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DownloadEvent{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
