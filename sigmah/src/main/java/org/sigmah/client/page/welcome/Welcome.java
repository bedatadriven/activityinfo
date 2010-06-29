/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import com.google.inject.Inject;
import org.sigmah.client.Application;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.common.GalleryView;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.table.PivotPageState;
/*
 * @author Alex Bertram
 */

public class Welcome implements Page {


    private GalleryView view;
    public static final PageId Welcome = new PageId("welcome");

    @Inject
    public Welcome(GalleryView view) {

        this.view = view;
        this.view.setHeading(Application.CONSTANTS.welcomeMessage());
        this.view.setIntro(Application.CONSTANTS.selectCategory());

        this.view.add(Application.CONSTANTS.dataEntry(), Application.CONSTANTS.dataEntryDescription(), "form.png", new SiteGridPageState());

        this.view.add(Application.CONSTANTS.siteLists(), Application.CONSTANTS.siteListsDescriptions(),
                "grid.png", new SiteGridPageState());

        this.view.add(Application.CONSTANTS.pivotTables(), Application.CONSTANTS.pivotTableDescription(),
                "pivot.png", new PivotPageState());

        this.view.add(Application.CONSTANTS.charts(), Application.CONSTANTS.chartsDescription(),
                "charts/time.png", new ChartPageState());

        this.view.add(Application.CONSTANTS.maps(), Application.CONSTANTS.mapsDescription(),
                "map.png", new MapPageState());


//        this.view.add("Exporter des Données Brutes",
//                "Sortir tous les données saisies pour des analyses au profondeur",
//                    "exporter.png", new ChartHomePlace());
//
//        this.view.add("Google Earth",
//                "Acceder au données à partir de Google Earth", "kml.png",
//                new StaticPageState("kml"));
//

    }

    public PageId getPageId() {
        return Welcome;
    }

    public Object getWidget() {
        return view;
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
        return true;
    }
}
