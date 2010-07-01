/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.*;

public class ProjectPageLoader implements PageLoader {

    private final NavigationHandler navigationHandler;
    private final SigmahInjector injector;

    @Inject
    public ProjectPageLoader(SigmahInjector injector, NavigationHandler navigationHandler) {
        this.injector = injector;
        this.navigationHandler = navigationHandler;

        navigationHandler.registerPageLoader(ProjectListPresenter.PAGE_ID, this);
    }

    @Override
    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {
        if(pageId.equals(ProjectListPresenter.PAGE_ID)) {
            callback.onSuccess(injector.getProjectListPresenter());
        }
    }
}
