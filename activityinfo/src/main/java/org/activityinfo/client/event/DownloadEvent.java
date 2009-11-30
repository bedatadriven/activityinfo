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

package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import org.activityinfo.client.page.DownloadManager;

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
        if(url == null)
            return null;
        int dot = url.lastIndexOf(".");
        if(dot == -1)
            return "";
        else
            return url.substring(dot+1);
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
