package org.activityinfo.client.page.map;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.base.GalleryView;
import org.activityinfo.client.page.charts.Charts;

import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapHomePage implements PagePresenter {

    private GalleryView view;

    @Inject
    public MapHomePage(GalleryView view) {

        this.view = view;
        this.view.add("Cartographie d'une indicateur unique",
                "Cartographier un indicateur unique avec des symbols gradués", "map/single.png",
                new SingleMapPlace());

    }

    @Override
    public PageId getPageId() {
        return Maps.Home;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(Place place) {
        return true;
    }
}