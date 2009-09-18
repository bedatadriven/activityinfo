package org.activityinfo.client.page.map;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.common.place.SimplePlaceParser;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.base.GalleryPage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapLoader implements PageLoader {

    private final AppInjector injector;

    @Inject
    public MapLoader(AppInjector injector) {
        this.injector = injector;

        PageManager pageManager = injector.getPageManager();
        pageManager.registerPageLoader(Maps.Home, this);
        pageManager.registerPageLoader(Maps.Single, this);

        PlaceSerializer placeSerializer = injector.getPlaceSerializer();
        placeSerializer.registerParser(Maps.Home, new SimplePlaceParser(new MapHomePlace()));
        placeSerializer.registerParser(Maps.Single, new SimplePlaceParser(new SingleMapPlace()));
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