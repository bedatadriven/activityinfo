/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.dashboard;

import org.activityinfo.client.page.Frames;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.widget.ContentPanel;

public class StaticPage extends ContentPanel implements Page {

    public StaticPage() {
        this.setHeaderVisible(false);
    }

    public void navigate(StaticPageState place) {
        this.setUrl("static/" + place.getKeyword() + ".html");
    }

    public PageId getPageId() {
        return Frames.STATIC;
    }

    public Object getWidget() {
        return this;
    }

    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(PageState place) {
        return false;
    }
}
