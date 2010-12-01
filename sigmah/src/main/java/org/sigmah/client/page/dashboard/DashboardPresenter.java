/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
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

        public ListStore<CountryDTO> getCountriesStore();

        public TreeStore<ProjectDTOLight> getProjectsStore();

        public TreeStore<OrgUnitDTOLight> getOrgUnitsStore();

        public TreeGrid<OrgUnitDTOLight> getOrgUnitsTree();

        public Button getLoadProjectsButton();

        public ContentPanel getProjectsPanel();

        public ContentPanel getOrgUnitsPanel();
    }

    /**
     * The service.
     */
    private final Dispatcher dispatcher;

    /**
     * The view.
     */
    private final View view;

    /**
     * The user's info.
     */
    private final UserInfo info;

    @Inject
    public DashboardPresenter(final Dispatcher dispatcher, final View view, final UserInfo info) {

        this.view = view;
        this.dispatcher = dispatcher;
        this.info = info;

        // Default sort order.
        view.getProjectsStore().setSortInfo(new SortInfo("name", SortDir.ASC));

        // Adds the load projects action.
        view.getLoadProjectsButton().addListener(Events.Select, new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {

                // Retreives all selected org units.
                final ArrayList<Integer> orgUnitsIds = new ArrayList<Integer>();
                final List<OrgUnitDTOLight> orgUnits = view.getOrgUnitsTree().getSelectionModel().getSelectedItems();

                // Gets the selected org units ids.
                if (orgUnits != null) {
                    for (final OrgUnitDTOLight orgUnit : orgUnits) {
                        orgUnitsIds.add(orgUnit.getId());
                    }
                }

                // Retrieves the projects for these org units.
                DashboardPresenter.this.dispatcher.execute(new GetProjects(orgUnitsIds),
                        new MaskingAsyncMonitor(view.getProjectsPanel(), I18N.CONSTANTS.loading()),
                        new AsyncCallback<ProjectListResult>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                Log.error("[GetProjects command] Error while getting projects.");
                                // nothing
                            }

                            @Override
                            public void onSuccess(ProjectListResult result) {
                                int count = 0;
                                view.getProjectsStore().removeAll();
                                if (result != null) {
                                    final List<ProjectDTOLight> resultList = result.getList();
                                    view.getProjectsStore().add(resultList, false);
                                    count = resultList.size();
                                }

                                view.getProjectsPanel().setHeading(I18N.CONSTANTS.projects() + " (" + count + ')');
                            }
                        });
            }
        });
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
                }
            }
        });

        return true;
    }

    @Override
    public void shutdown() {
    }
}
