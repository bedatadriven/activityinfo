/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.Frames;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.common.nav.NavigationPanel;
import org.sigmah.client.page.common.widget.VSplitFrameSet;
import org.sigmah.client.page.config.design.DesignPresenter;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class ConfigLoader implements PageLoader {

    private final AppInjector injector;
    private final Dispatcher service;

    @Inject
    public ConfigLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;
        this.service = injector.getService();

        pageManager.registerPageLoader(Frames.ConfigFrameSet, this);
        pageManager.registerPageLoader(AccountEditor.Account, this);
        pageManager.registerPageLoader(DbConfigPresenter.DatabaseConfig, this);
        pageManager.registerPageLoader(DbListPresenter.DatabaseList, this);
        pageManager.registerPageLoader(DbUserEditor.DatabaseUsers, this);
        pageManager.registerPageLoader(DbPartnerEditor.DatabasePartners, this);
        pageManager.registerPageLoader(DbProjectEditor.DatabaseProjects, this);
        pageManager.registerPageLoader(LockedPeriodsPresenter.LockedPeriod, this);
        pageManager.registerPageLoader(DesignPresenter.PAGE_ID, this);

        placeSerializer.registerStatelessPlace(AccountEditor.Account, new AccountPageState());
        placeSerializer.registerStatelessPlace(DbListPresenter.DatabaseList, new DbListPageState());
        placeSerializer.registerParser(DbConfigPresenter.DatabaseConfig, new DbPageState.Parser(DbConfigPresenter.DatabaseConfig));
        placeSerializer.registerParser(DbUserEditor.DatabaseUsers, new DbPageState.Parser(DbUserEditor.DatabaseUsers));
        placeSerializer.registerParser(DbPartnerEditor.DatabasePartners, new DbPageState.Parser(DbPartnerEditor.DatabasePartners));
        placeSerializer.registerParser(DbProjectEditor.DatabaseProjects, new DbPageState.Parser(DbProjectEditor.DatabaseProjects));
        placeSerializer.registerParser(LockedPeriodsPresenter.LockedPeriod, new DbPageState.Parser(LockedPeriodsPresenter.LockedPeriod));
        placeSerializer.registerParser(DesignPresenter.PAGE_ID, new DbPageState.Parser(DesignPresenter.PAGE_ID));
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
            @Override
            public void onSuccess() {
                if (Frames.ConfigFrameSet.equals(pageId)) {
                    NavigationPanel navPanel = new NavigationPanel(injector.getEventBus(),
                            injector.getConfigNavigator());
                    VSplitFrameSet frameSet = new VSplitFrameSet(pageId, navPanel);
                    callback.onSuccess(frameSet);

                } else if (AccountEditor.Account.equals(pageId)) {
                    callback.onSuccess(injector.getAccountEditor());

                } else if (DbListPresenter.DatabaseList.equals(pageId)) {
                    callback.onSuccess(injector.getDbListPage());
                    
                } else if (pageState instanceof DbPageState) {
                    final DbPageState dPlace = (DbPageState) pageState;

                    /// the schema needs to be loaded before we can continue
                    service.execute(new GetSchema(), null, new Got<SchemaDTO>() {

                        @Override
                        public void got(SchemaDTO schema) {

                            UserDatabaseDTO db = schema.getDatabaseById(dPlace.getDatabaseId());

                            if (DbConfigPresenter.DatabaseConfig.equals(pageId)) {
                                DbConfigPresenter presenter = injector.getDbConfigPresenter();
                                presenter.go(db);
                                callback.onSuccess(presenter);

                            } else if (DesignPresenter.PAGE_ID.equals(pageId)) {
                                DesignPresenter presenter = injector.getDesigner();
                                presenter.go(db);
                                callback.onSuccess(presenter);

                            } else if (DbUserEditor.DatabaseUsers.equals(pageId)) {
                                DbUserEditor editor = injector.getDbUserEditor();
                                editor.go(db, dPlace);
                                callback.onSuccess(editor);

                            } else if (DbPartnerEditor.DatabasePartners.equals(pageId)) {
                                DbPartnerEditor presenter = injector.getDbPartnerEditor();
                                presenter.go(db);
                                callback.onSuccess(presenter);

                            } else if (LockedPeriodsPresenter.LockedPeriod.equals(pageId)) {
                            	LockedPeriodsPresenter presenter = injector.getLockedPeriodsEditor();
                                presenter.initialize(db);
                                callback.onSuccess(presenter);
                                
                            } else if (DbProjectEditor.DatabaseProjects.equals(pageId)) {
                                DbProjectEditor presenter = injector.getDbProjectEditor();
                                presenter.go(db);
                                callback.onSuccess(presenter);
                            } else {
                                callback.onFailure(new Exception("ConfigLoader didn't know how to handle " + pageState.toString()));
                            }
                        }
                    });
                } else {
                    callback.onFailure(new Exception("ConfigLoader didn't know how to handle " + pageState.toString()));
                }
            }
        });

    }
}
