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

public class DbConfigPresenter implements PagePresenter {

    private final GalleryView view;

    @Inject
    public DbConfigPresenter(GalleryView view) {
        this.view = view;
    }

    public void go(UserDatabaseDTO db) {
        view.setHeading(db.getFullName() == null ? db.getName() : db.getFullName());

        if (db.isDesignAllowed()) {
            view.add(Application.CONSTANTS.design(), Application.CONSTANTS.designDescription(),
                    "db-design.png", new DbPlace(Pages.Design, db.getId()));
        }
        if (db.isManageAllUsersAllowed()) {
            view.add(Application.CONSTANTS.partner(), Application.CONSTANTS.partnerEditorDescription(),
                    "db-partners.png", new DbPlace(Pages.DatabasePartners, db.getId()));
        }
        if (db.isManageUsersAllowed()) {
            view.add(Application.CONSTANTS.users(), Application.CONSTANTS.userManagerDescription(),
                    "db-users.png", new DbPlace(Pages.DatabaseUsers, db.getId()));
        }

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
