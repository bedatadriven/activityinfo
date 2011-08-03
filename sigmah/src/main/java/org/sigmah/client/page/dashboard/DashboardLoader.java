package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.config.AccountPageState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DashboardLoader  implements PageLoader  {
    private final AppInjector injector;
    private final Dispatcher service;
    
    @Inject
    public DashboardLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;
        this.service = injector.getService();
        
        pageManager.registerPageLoader(DashboardPresenter.Dashboard, this);
        placeSerializer.registerStatelessPlace(DashboardPresenter.Dashboard, new AccountPageState());
    }
	
	@Override
	public void load(final PageId pageId, PageState pageState,
			final AsyncCallback<Page> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
            @Override
            public void onSuccess() {
                if (DashboardPresenter.Dashboard.equals(pageId)) {
                    callback.onSuccess(injector.getDashboard());

                } else {
                    callback.onFailure(new Exception("ConfigLoader didn't know how to handle " + pageId.toString()));                
                }
            }
        });
	}

}
