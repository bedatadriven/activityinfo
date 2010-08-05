/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Page loader of a project details page.
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectPageLoader implements PageLoader {

    private final NavigationHandler navigationHandler;
    private final SigmahInjector injector;

    @Inject
    public ProjectPageLoader(SigmahInjector injector, NavigationHandler navigationHandler, PageStateSerializer placeSerializer) {
        this.injector = injector;
        this.navigationHandler = navigationHandler;

        navigationHandler.registerPageLoader(ProjectPresenter.PAGE_ID, this);
        placeSerializer.registerParser(ProjectPresenter.PAGE_ID, new ProjectState.Parser());
    }

    @Override
    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {
        if(pageId.equals(ProjectPresenter.PAGE_ID)) {
            final ProjectPresenter projectPresenter = injector.getProjectPresenter();
            projectPresenter.navigate(pageState);
            
            callback.onSuccess(projectPresenter);
        }
    }
}
