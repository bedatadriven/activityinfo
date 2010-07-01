/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

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


public class ProjectListPresenter implements Page {
    public static final PageId PAGE_ID = new PageId("projects");

    private final Dispatcher dispatcher;
    private final View view;

    @Inject
    public ProjectListPresenter(Dispatcher dispatcher, View view) {
        this.dispatcher = dispatcher;
        this.view = view;
    }

    public interface View {
        Observable getGrid();
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
        dispatcher.execute(new GetProjects(), null, new AsyncCallback<ProjectList>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ProjectList projectList) {
                view.getStore().add(projectList.getList());
            }
        });


        return true;
    }

    @Override
    public void shutdown() {

    }
}
