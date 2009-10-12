package org.activityinfo.client.page.charts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.PagePresenter;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartLoader implements PageLoader {

    private final AppInjector injector;

    @Inject
    public ChartLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Charts.Charts, this);
        placeSerializer.registerStatelessPlace(Charts.Charts, new ChartPlace());
    }

    @Override
    public void load(final PageId pageId, final Place initialPlaceHint, final AsyncCallback<PagePresenter> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {

                if(initialPlaceHint instanceof ChartPlace) {
                    callback.onSuccess(injector.getCharter());
                }
            }
        });


    }
}
