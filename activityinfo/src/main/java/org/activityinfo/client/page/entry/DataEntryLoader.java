package org.activityinfo.client.page.entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.common.nav.NavigationPanel;
import org.activityinfo.client.page.common.widget.VSplitFrameSet;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DataEntryLoader implements PageLoader {

    private final AppInjector injector;

    @Inject
    public DataEntryLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Pages.DataEntryFrameSet, this);
        pageManager.registerPageLoader(Pages.SiteGrid, this);
        placeSerializer.registerParser(Pages.SiteGrid, new SiteGridPlace.Parser());
    }

    @Override
    public void load(final PageId pageId, final Place place, final AsyncCallback<PagePresenter> callback) {

        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {

                if(Pages.DataEntryFrameSet.equals(pageId)) {
                    loadFrame(place, callback);
                } else if(Pages.SiteGrid.equals(pageId)) {
                    loadSiteGrid(place, callback);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                callback.onFailure(throwable);
            }
        });

    }

    private void loadFrame(Place place, AsyncCallback<PagePresenter> callback) {

        NavigationPanel navPanel = new NavigationPanel(injector.getEventBus(),
                injector.getDataEntryNavigator());

        VSplitFrameSet frameSet = new VSplitFrameSet(Pages.DataEntryFrameSet, navPanel);

        callback.onSuccess(frameSet);
    }

    protected void loadSiteGrid(final Place place, final AsyncCallback<PagePresenter> callback) {
        injector.getService().execute(new GetSchema(), null, new Got<Schema>() {
            @Override
            public void got(Schema schema) {

                SiteGridPlace sgPlace = (SiteGridPlace)place;
                if(sgPlace.getActivityId() == 0) {
                    sgPlace.setActivityId(schema.getFirstActivity().getId());
                }

                ActivityModel activity = schema.getActivityById(sgPlace.getActivityId());

                SiteGridPage grid = new SiteGridPage(true);
                SiteEditor editor = new SiteEditor(injector.getEventBus(), injector.getService(),
                        injector.getStateManager(), grid);

                if(activity.getReportingFrequency() == ActivityModel.REPORT_MONTHLY) {
                    MonthlyGrid monthlyGrid = new MonthlyGrid(activity);
                    MonthlyTab monthlyTab = new MonthlyTab(monthlyGrid);
                    MonthlyPresenter monthlyPresenter = new MonthlyPresenter(
                            injector.getEventBus(),
                            injector.getService(),
                            injector.getStateManager(),
                            activity, monthlyGrid);
                    editor.addSubComponent(monthlyPresenter);
                    grid.addSouthTab(monthlyTab);
                } else {

                    DetailsTab detailsTab = new DetailsTab();
                    DetailsPresenter detailsPresenter = new DetailsPresenter(
                            injector.getEventBus(),
                            activity,
                            injector.getMessages(),
                            detailsTab);
                    grid.addSouthTab(detailsTab);
                    editor.addSubComponent(detailsPresenter);
                }

                if(Maps.isLoaded()) {
                    SiteMap map = new SiteMap(injector.getEventBus(), injector.getService(),
                            activity);

                    editor.addSubComponent(map);
                    grid.addSidePanel(Application.CONSTANTS.map(), Application.ICONS.map(), map);

                }
                editor.go((SiteGridPlace) place, activity);

                callback.onSuccess(editor);

            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

}
