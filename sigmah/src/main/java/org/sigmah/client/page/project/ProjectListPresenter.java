/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.ProjectList;


/**
 * Manage the {@link ProjectListView}.
 * @author rca
 */
public class ProjectListPresenter implements Page {
    public static final PageId PAGE_ID = new PageId("projects");

    private final Dispatcher dispatcher;
    private final View view;

    @Inject
    public ProjectListPresenter(Dispatcher dispatcher, View view) {
        this.dispatcher = dispatcher;
        this.view = view;

        // TODO: navigate() should be called by the home screen but since we currently don't have one, it is called explicity here.
        navigate(null);
    }

    /**
     * Description of the view managed by this presenter.
     */
    public interface View {
        /**
         * Return the grid used to display the projects.
         * @return A grid.
         */
        Observable getGrid();
        
        /**
         * Return the store used by the grid.
         * @return A project store.
         */
        ListStore getStore();
    }


    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        Log.debug("Loading projects...");
        dispatcher.execute(new GetProjects(), null, new AsyncCallback<ProjectList>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ProjectList projectList) {
                Log.debug("Projects loaded : "+projectList.getList().size());
                view.getStore().add(projectList.getList());
            }
        });


        return true;
    }

    @Override
    public void shutdown() {

    }
}
