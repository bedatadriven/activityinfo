package org.activityinfo.client.page.welcome;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import org.activityinfo.client.page.*;
/*
 * @author Alex Bertram
 */

public class StaticPage extends ContentPanel implements Page {

    public StaticPage() {
        this.setHeaderVisible(false);
    }

    public void navigate(StaticPageState place) {
        this.setUrl("static/" + place.getKeyword() + ".html");
    }

    public PageId getPageId() {
        return Frames.Static;
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
