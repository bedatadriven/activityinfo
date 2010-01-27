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
        this.view.setHeading("Bienvenue dans l'ActivityInfo");
        this.view.setIntro("Selectionner une rubrique à partir des onglets ci-dessus, ou bien parcourir  " +
                "tout les fonctionalities ci-dessous.");

        this.view.add(Application.CONSTANTS.dataEntry(),
                "Saisir les résultants de vos interventions avec l'aide des formulaires, fiches des calcul et " +
                        "cartes interactive.", "form.png", new SiteGridPlace());

        this.view.add("Listes des interventions",
                "Parcourir des listes des sites d'intervention avec une presentation sembable à l'Excel. Trier," +
                        "filtrer, chercher, et cartographier",
                "grid.png", new SiteGridPlace());

        this.view.add("Tableaux croisser dynamique",
                "Croisser tous les dimensions de vos résultats -- activité, periode de temps, partenaires, " +
                        "géographie",
                "pivot.png", new PivotPlace());

        this.view.add("Graphiques",
                "Sortir rapidement des graphiques variés qui synthesent vos resultats",
                "charts/time.png", new ChartPlace());

        this.view.add("Cartes",
                "Sortir rapidement de cartographies des indicateurs",
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
