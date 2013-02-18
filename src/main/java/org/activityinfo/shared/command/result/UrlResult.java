/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

/**
 *
 * The result containing a URL to be subsequently downloaded
 */
public class UrlResult implements CommandResult {

    private String url;

    public UrlResult() {
    }

    public UrlResult(String url) {
        this.url = url;
    }

    /**
     * @return The URL from which the rendered file can be accessed.
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
