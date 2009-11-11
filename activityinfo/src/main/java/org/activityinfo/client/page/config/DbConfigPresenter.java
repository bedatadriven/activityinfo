package org.activityinfo.client.page.config;

import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.Place;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.GalleryView;
import org.activityinfo.shared.dto.UserDatabaseDTO;

public class DbConfigPresenter implements PagePresenter
{

    private final GalleryView view;

    @Inject
    public DbConfigPresenter(GalleryView view) {
        this.view = view;
    }

    public void go(UserDatabaseDTO db) {
        UserDatabaseDTO db1 = db;
        view.setHeading(db.getFullName() == null ? db.getName() : db.getFullName());

        view.add(Application.CONSTANTS.design(), "Créer ou modifier des activités et leurs indicateurs qui fait parti de " +
                "cette base de données", "db-design.png", new DbPlace(Pages.Design, db.getId()));

        view.add(Application.CONSTANTS.partner(), "Definer les organisations partenaire qui participe dans cette " +
                "base de données.", "db-partners.png", new DbPlace(Pages.DatabasePartners, db.getId()) );

        view.add(Application.CONSTANTS.users(), "Ajouter des utilistateurs ou controler leur niveau d'accès.",
                "db-users.png", new DbPlace(Pages.DatabaseUsers, db.getId()));

//        view.add("Cibles", "Définer les cibles pour les indicateurs.", "db-targets",
//                new DbPlace(Pages.DatabaseTargets, db.getId()));
    }

    @Override
    public PageId getPageId() {
        return Pages.DatabaseConfig;
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

    @Override
    public boolean navigate(Place place) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
