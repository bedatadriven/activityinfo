package org.activityinfo.client.page.config;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.common.nav.NavigationPanel;
import org.activityinfo.client.page.common.widget.VSplitFrameSet;
import org.activityinfo.client.page.config.design.Designer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.Schema;

public class ConfigLoader implements PageLoader {

    private final AppInjector injector;
    private final CommandService service;

    @Inject
    public ConfigLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;
        this.service = injector.getService();

        pageManager.registerPageLoader(Pages.ConfigFrameSet, this);
        pageManager.registerPageLoader(Pages.Account, this);
        pageManager.registerPageLoader(Pages.DatabaseList, this);
        pageManager.registerPageLoader(Pages.DatabaseUsers, this);
        pageManager.registerPageLoader(Pages.DatabasePartners, this);
        pageManager.registerPageLoader(Pages.Design, this);

        placeSerializer.registerStatelessPlace(Pages.Account, new AccountPlace());
        placeSerializer.registerStatelessPlace(Pages.DatabaseList, new DbListPlace());
        placeSerializer.registerParser(Pages.DatabaseUsers, new DbPlace.Parser(Pages.DatabaseUsers));
        placeSerializer.registerParser(Pages.DatabasePartners, new DbPlace.Parser(Pages.DatabasePartners));
        placeSerializer.registerParser(Pages.Design, new DbPlace.Parser(Pages.Design));
    }

    @Override
    public void load(final PageId pageId, final Place place, final AsyncCallback<PagePresenter> callback) {


        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {

                if(Pages.ConfigFrameSet.equals(pageId)) {

                    NavigationPanel navPanel = new NavigationPanel(injector.getEventBus(),
                           injector.getConfigNavigator());
                    VSplitFrameSet frameSet = new VSplitFrameSet(pageId, navPanel);

                    callback.onSuccess(frameSet);

                } else if(Pages.Account.equals(pageId)) {
                    callback.onSuccess(injector.getAccountEditor());

                } else if(Pages.DatabaseList.equals(pageId)) {
                    callback.onSuccess(injector.getDbListPresenter());

                } else if(Pages.DatabaseUsers.equals(pageId)) {
                    service.execute(new GetSchema(), null, new Got<Schema>() {
                        @Override
                        public void got(Schema schema) {

                            DbPlace userPlace = (DbPlace) place;
                            DbUserEditor editor = injector.getDbUserEditor();

                            editor.go(schema.getDatabaseById(userPlace.getDatabaseId()), userPlace);

                            callback.onSuccess(editor);
                        }
                    });
                } else if(Pages.DatabasePartners.equals(pageId)) {
                    service.execute(new GetSchema(), null, new Got<Schema>() {
                        @Override
                        public void got(Schema result) {
                            DbPartnerEditor presenter = injector.getDbPartnerEditor();

                            DbPlace partnerPlace = (DbPlace) place;
                            presenter.go(result.getDatabaseById(partnerPlace.getDatabaseId()));

                            callback.onSuccess(presenter);
                        }
                    });
                } else if(Pages.Design.equals(pageId)) {
                      service.execute(new GetSchema(), null, new Got<Schema>() {

                        @Override
                        public void got(Schema schema) {
                            Designer presenter = injector.getDesigner();

                            DbPlace dPlace = (DbPlace) place;
                            presenter.go(schema.getDatabaseById(dPlace.getDatabaseId()));

                            callback.onSuccess(presenter);
                        }
                    });

                } else {

                    callback.onFailure(new Exception("ConfigLoader didn't know how to handle " + place.toString()));

                }
            }

        });


    }



}
