package org.sigmah.client.page.admin;

import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AdminPageLoader implements PageLoader {

	
	private final SigmahInjector injector;

    @Inject
    public AdminPageLoader(SigmahInjector injector, NavigationHandler navigationHandler,
            PageStateSerializer placeSerializer) {

        this.injector = injector;

        navigationHandler.registerPageLoader(AdminPresenter.PAGE_ID, this);
        placeSerializer.registerParser(AdminPresenter.PAGE_ID, new AdminPageState.Parser());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {    
        if (pageId.equals(AdminPresenter.PAGE_ID)) {
            final AdminPresenter adminPresenter = injector.getAdminPresenter();
            adminPresenter.navigate(pageState);
            callback.onSuccess(adminPresenter);
        }
    }

}
