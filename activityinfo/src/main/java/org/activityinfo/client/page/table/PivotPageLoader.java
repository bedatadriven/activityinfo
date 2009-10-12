package org.activityinfo.client.page.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPageLoader implements PageLoader {

    private AppInjector injector;

    @Inject
    public PivotPageLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Pages.Pivot, this);
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
