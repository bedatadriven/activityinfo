package org.activityinfo.client.page.welcome;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
/*
 * @author Alex Bertram
 */

public class WelcomeLoader implements PageLoader {


    private final AppInjector injector;

    @Inject
    public WelcomeLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Welcome.Welcome, this);
        placeSerializer.registerStatelessPlace(Welcome.Welcome, new WelcomePageState());
    }

    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {
        callback.onSuccess(injector.getWelcomePage());
    }
}
