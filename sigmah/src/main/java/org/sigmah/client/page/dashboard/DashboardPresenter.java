/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Home screen of sigmah. Displays the main menu and a reminder of urgent tasks.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardPresenter implements Page {

    public static final PageId PAGE_ID = new PageId("welcome");

    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(DashboardView.class)
    public interface View {

        public ProjectsListPanel getProjectsListPanel();

        public TreeStore<OrgUnitDTOLight> getOrgUnitsStore();

        public TreeGrid<OrgUnitDTOLight> getOrgUnitsTree();

        public ContentPanel getOrgUnitsPanel();
    }

    /**
     * The view.
     */
    private final View view;

    /**
     * The user's info.
     */
    private final UserInfo info;

    @Inject
    public DashboardPresenter(final View view, final UserInfo info, final Authentication authentication) {

        this.view = view;
        this.info = info;
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

        // Reloads the list of projects each time the navigation is done to be
        // sure to show the last modifications.

        // Gets user's organization.
        info.getOrgUnit(new AsyncCallback<OrgUnitDTOLight>() {

            @Override
            public void onFailure(Throwable e) {
                // nothing
            }

            @Override
            public void onSuccess(OrgUnitDTOLight result) {

                if (result != null) {

                    view.getOrgUnitsStore().removeAll();
                    view.getOrgUnitsPanel().setHeading(
                            result.getName() + " (" + result.getFullName() + ") : " + I18N.CONSTANTS.orgunitTree());

                    for (final OrgUnitDTOLight child : result.getChildrenDTO()) {
                        view.getOrgUnitsStore().add(child, true);
                    }

                    view.getProjectsListPanel().refresh(result.getId());
                }
            }
        });

        return true;
    }

    @Override
    public void shutdown() {
    }
}
