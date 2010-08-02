/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

/**
 * Page loader for the dashboard screen of Sigmah.
 * @author rca
 */
public class DashboardPageLoader implements PageLoader {
    private final SigmahInjector injector;
    
    @Inject
    public DashboardPageLoader(SigmahInjector injector, NavigationHandler navigationHandler, PageStateSerializer placeSerializer) {
        this.injector = injector;

        navigationHandler.registerPageLoader(DashboardPresenter.PAGE_ID, this);
        placeSerializer.registerParser(DashboardPresenter.PAGE_ID, new DashboardPageState.Parser());
    }

    @Override
    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {
        if(pageId.equals(DashboardPresenter.PAGE_ID)) {
            final DashboardPresenter presenter = injector.getDashboardPresenter();
            presenter.navigate(pageState);
            callback.onSuccess(presenter);
        }
    }

}
