package org.activityinfo.client.page.welcome;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import org.activityinfo.client.Place;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
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
