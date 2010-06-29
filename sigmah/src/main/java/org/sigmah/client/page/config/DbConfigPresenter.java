package org.sigmah.client.page.config;

import com.google.inject.Inject;
import org.sigmah.client.Application;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.GalleryView;
import org.sigmah.client.page.config.design.Designer;
import org.sigmah.shared.dto.UserDatabaseDTO;

public class DbConfigPresenter implements Page {

    private final GalleryView view;
    public static final PageId DatabaseConfig =  new PageId("db");

    @Inject
    public DbConfigPresenter(GalleryView view) {
        this.view = view;
    }

    public void go(UserDatabaseDTO db) {
        view.setHeading(db.getFullName() == null ? db.getName() : db.getFullName());

        if (db.isDesignAllowed()) {
            view.add(Application.CONSTANTS.design(), Application.CONSTANTS.designDescription(),
                    "db-design.png", new DbPageState(Designer.Design, db.getId()));
        }
        if (db.isManageAllUsersAllowed()) {
            view.add(Application.CONSTANTS.partner(), Application.CONSTANTS.partnerEditorDescription(),
                    "db-partners.png", new DbPageState(DbPartnerEditor.DatabasePartners, db.getId()));
        }
        if (db.isManageUsersAllowed()) {
            view.add(Application.CONSTANTS.users(), Application.CONSTANTS.userManagerDescription(),
                    "db-users.png", new DbPageState(DbUserEditor.DatabaseUsers, db.getId()));
        }

//        view.add("Cibles", "Définer les cibles pour les indicateurs.", "db-targets",
//                new DbPageState(Pages.DatabaseTargets, db.getId()));
    }

    @Override
    public PageId getPageId() {
        return DatabaseConfig;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
