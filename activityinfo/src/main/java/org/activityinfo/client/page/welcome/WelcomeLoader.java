package org.activityinfo.client.page.welcome;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
/*
 * @author Alex Bertram
 */

public class WelcomeLoader implements PageLoader {


    private final AppInjector injector;

    @Inject
    public WelcomeLoader(AppInjector injector, PageManager pageManager, PlaceSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Pages.Welcome, this);
        placeSerializer.registerStatelessPlace(Pages.Welcome, new WelcomePlace());
    }

    public void load(PageId pageId, Place place, AsyncCallback<PagePresenter> callback) {
        callback.onSuccess(injector.getWelcomePage());
    }
}
