package org.activityinfo.client.page.welcome;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.common.place.SimplePlaceParser;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.base.GalleryPage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class WelcomeLoader implements PageLoader {


    private final AppInjector injector;

    @Inject
    public WelcomeLoader(AppInjector injector) {
        this.injector = injector;
        injector.getPlaceSerializer().registerParser(Pages.Welcome,
                new SimplePlaceParser(new WelcomePlace()));
        injector.getPageManager().registerPageLoader(Pages.Welcome, this);
    }

    public void load(PageId pageId, Place place, AsyncCallback<PagePresenter> callback) {
        callback.onSuccess(injector.getWelcomePage());
    }
}
