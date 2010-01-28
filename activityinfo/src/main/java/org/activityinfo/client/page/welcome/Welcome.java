package org.activityinfo.client.page.welcome;

import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.Place;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.charts.ChartPlace;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.client.page.entry.SiteGridPlace;
import org.activityinfo.client.page.map.MapPlace;
import org.activityinfo.client.page.table.PivotPlace;
/*
 * @author Alex Bertram
 */

public class Welcome implements PagePresenter {


    private GalleryView view;

    @Inject
    public Welcome(GalleryView view) {

        this.view = view;
        this.view.setHeading(Application.CONSTANTS.welcomeMessage());
        this.view.setIntro(Application.CONSTANTS.selectCategory());

        this.view.add(Application.CONSTANTS.dataEntry(), Application.CONSTANTS.dataEntryDescription(), "form.png", new SiteGridPlace());

        this.view.add(Application.CONSTANTS.siteLists(), Application.CONSTANTS.siteListsDescriptions(),
                "grid.png", new SiteGridPlace());

        this.view.add(Application.CONSTANTS.pivotTables(), Application.CONSTANTS.pivotTableDescription(),
                "pivot.png", new PivotPlace());

        this.view.add(Application.CONSTANTS.charts(), Application.CONSTANTS.chartsDescription(),
                "charts/time.png", new ChartPlace());

        this.view.add(Application.CONSTANTS.maps(), Application.CONSTANTS.mapsDescription(),
                "map.png", new MapPlace());


//        this.view.add("Exporter des Données Brutes",
//                "Sortir tous les données saisies pour des analyses au profondeur",
//                    "exporter.png", new ChartHomePlace());
//
//        this.view.add("Google Earth",
//                "Acceder au données à partir de Google Earth", "kml.png",
//                new StaticPlace("kml"));
//

    }

    public PageId getPageId() {
        return Pages.Welcome;
    }

    public Object getWidget() {
        return view;
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
        return true;
    }
}
