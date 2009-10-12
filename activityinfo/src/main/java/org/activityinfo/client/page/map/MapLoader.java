package org.activityinfo.client.page.map;

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
public class MapLoader implements PageLoader {

    private final AppInjector injector;

    @Inject
    public MapLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Maps.Home, this);
        pageManager.registerPageLoader(Maps.Single, this);

        placeSerializer.registerStatelessPlace(Maps.Home, new MapHomePlace());
        placeSerializer.registerStatelessPlace(Maps.Single, new SingleMapPlace());
    }

    @Override
    public void load(final PageId pageId, final Place place, final AsyncCallback<PagePresenter> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {


                if(Maps.Home.equals(pageId)) {
                    callback.onSuccess(injector.getMapHome());

                } else {

                    MapForm form;

                    if(pageId.equals(Maps.Single)) {
                        form = injector.getSingleMapForm();
                    } else {
                        callback.onFailure(new Exception("Unknown page type" + pageId.toString()));
                        return;
                    }

                    MapPage page = new MapPage(form);
                    MapPresenter presenter = new MapPresenter(pageId,
                            injector.getEventBus(),
                            injector.getService(),
                            page);

                    callback.onSuccess(presenter);
                }

            }
        });

    }
}