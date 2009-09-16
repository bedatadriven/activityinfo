package org.activityinfo.client.page.table;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.date.DateUtil;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPageLoader implements PageLoader {

    private AppInjector injector;

    @Inject
    public PivotPageLoader(AppInjector injector) {
        this.injector = injector;

        PageManager pageManager = injector.getPageManager();
        pageManager.registerPageLoader(Pages.Pivot, this);

        PlaceSerializer placeSerializer = injector.getPlaceSerializer();
        placeSerializer.registerParser(Pages.Pivot, new PivotPlace.Parser());
    }

    @Override
    public void load(PageId pageId, Place initialPlaceHint, final AsyncCallback<PagePresenter> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(injector.getPivotPresenter());
            }
        });

    }
}
