package org.activityinfo.client.page.welcome;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.*;

import com.extjs.gxt.ui.client.widget.ContentPanel;
/*
 * @author Alex Bertram
 */

public class StaticPage extends ContentPanel implements PagePresenter {

    public StaticPage() {
        this.setHeaderVisible(false);
    }

    public void navigate(StaticPlace place) {
        this.setUrl("static/" + place.getKeyword() + ".html");
    }

    public PageId getPageId() {
        return Pages.Static;
    }

    public Object getWidget() {
        return this;
    }

    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(Place place) {
        return false;
    }
}
