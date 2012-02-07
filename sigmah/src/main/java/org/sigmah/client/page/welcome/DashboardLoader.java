/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DashboardLoader implements PageLoader {
    private final Provider<DashboardPage> welcomeProvider;

    @Inject
    public DashboardLoader(NavigationHandler pageManager, PageStateSerializer placeSerializer, Provider<DashboardPage> welcomeProvider) {
        this.welcomeProvider = welcomeProvider;

        pageManager.registerPageLoader(DashboardPage.PAGE_ID, this);
        placeSerializer.registerStatelessPlace(DashboardPage.PAGE_ID, new DashboardPlace());
    }

    @Override
	public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {
        callback.onSuccess(welcomeProvider.get());
    }
}