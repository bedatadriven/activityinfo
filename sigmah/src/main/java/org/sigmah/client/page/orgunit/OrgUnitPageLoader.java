package org.sigmah.client.page.orgunit;

import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class OrgUnitPageLoader implements PageLoader {

    private final SigmahInjector injector;

    @Inject
    public OrgUnitPageLoader(SigmahInjector injector, NavigationHandler navigationHandler,
            PageStateSerializer placeSerializer) {

        this.injector = injector;

        navigationHandler.registerPageLoader(OrgUnitPresenter.PAGE_ID, this);
        placeSerializer.registerParser(OrgUnitPresenter.PAGE_ID, new OrgUnitState.Parser());
    }

    @Override
    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {

        if (pageId.equals(OrgUnitPresenter.PAGE_ID)) {

            final OrgUnitPresenter orgUnitPresenter = injector.getOrgUnitPresenter();
            orgUnitPresenter.navigate(pageState);

            callback.onSuccess(orgUnitPresenter);
        }
    }
}
