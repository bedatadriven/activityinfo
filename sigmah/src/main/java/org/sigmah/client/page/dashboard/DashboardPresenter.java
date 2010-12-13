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
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Radio;
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

        public ProjectStore getProjectsStore();

        public TreeStore<OrgUnitDTOLight> getOrgUnitsStore();

        public TreeGrid<OrgUnitDTOLight> getOrgUnitsTree();

        public Button getLoadProjectsButton();

        public ContentPanel getProjectsPanel();

        public ContentPanel getOrgUnitsPanel();

        public Radio getRadioFilter(ProjectModelType type);
    }

    /**
     * A tree store with some useful dedicated methods.
     * 
     * @author tmi
     * 
     */
    public static class ProjectStore extends TreeStore<ProjectDTOLight> {
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

    // Current projects grid parameters.
    private ProjectModelType currentModelType;
    private final ArrayList<Integer> orgUnitsIds;

    @Inject
    public DashboardPresenter(final Dispatcher dispatcher, final View view, final UserInfo info,
            final Authentication authentication) {

        this.view = view;
        this.dispatcher = dispatcher;
        this.info = info;

        // Default sort order of the projects grid.
        view.getProjectsStore().setSortInfo(new SortInfo("name", SortDir.ASC));

        // Adds the refresh projects action.
        view.getLoadProjectsButton().addListener(Events.Select, new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {

                // Retreives all selected org units.
                orgUnitsIds.clear();
                final List<OrgUnitDTOLight> orgUnits = view.getOrgUnitsTree().getSelectionModel().getSelectedItems();

                // Gets the selected org units ids.
                if (orgUnits != null) {
                    for (final OrgUnitDTOLight orgUnit : orgUnits) {
                        orgUnitsIds.add(orgUnit.getId());
                    }
                }

                refreshProjectGrid();
            }
        });

        // Default filters parameters.
        orgUnitsIds = new ArrayList<Integer>();
        currentModelType = ProjectModelType.NGO;

        // Updates the projects grid heading when the store is filtered.
        view.getProjectsStore().addListener(Store.Filter, new Listener<StoreEvent<ProjectDTOLight>>() {

            @Override
            public void handleEvent(StoreEvent<ProjectDTOLight> be) {
                view.getProjectsPanel().setHeading(
                        I18N.CONSTANTS.projects() + " (" + view.getProjectsStore().getChildCount() + ')');
            }
        });

        // Adds actions on filter by model type.
        for (final ProjectModelType type : ProjectModelType.values()) {
            view.getRadioFilter(type).addListener(Events.Change, new Listener<FieldEvent>() {

                @Override
                public void handleEvent(FieldEvent be) {
                    if (Boolean.TRUE.equals(be.getValue())) {
                        currentModelType = type;
                        applyProjectFilters();
                    }
                }
            });
        }

        // The filter by model type.
        final StoreFilter<ProjectDTOLight> typeFilter = new StoreFilter<ProjectDTOLight>() {

            @Override
            public boolean select(Store<ProjectDTOLight> store, ProjectDTOLight parent, ProjectDTOLight item,
                    String property) {

                boolean selected = false;

                // Root item.
                if (item.getParent() == null) {
                    // A root item is filtered if its type doesn't match the
                    // current type.
                    selected = item.getVisibility(authentication.getOrganizationId()) == currentModelType;
                }
                // Child item
                else {
                    // A child item is filtered if its parent is filtered.
                    selected = ((ProjectDTOLight) item.getParent()).getVisibility(authentication.getOrganizationId()) == currentModelType;
                }

                return selected;
            }
        };

        view.getProjectsStore().addFilter(typeFilter);
    }

    /**
     * Refreshes the projects grid with the current parameters.
     */
    private void refreshProjectGrid() {

        // Retrieves all the projects in the org units. The filters on type,
        // etc. are applied locally.
        final GetProjects cmd = new GetProjects();
        cmd.setOrgUnitsIds(orgUnitsIds);

        dispatcher.execute(cmd, new MaskingAsyncMonitor(view.getProjectsPanel(), I18N.CONSTANTS.loading()),
                new AsyncCallback<ProjectListResult>() {

                    @Override
                    public void onFailure(Throwable e) {
                        Log.error("[GetProjects command] Error while getting projects.", e);
                        // nothing
                    }

                    @Override
                    public void onSuccess(ProjectListResult result) {

                        view.getProjectsStore().removeAll();
                        view.getProjectsStore().clearFilters();

                        if (result != null) {
                            final List<ProjectDTOLight> resultList = result.getList();
                            view.getProjectsStore().add(resultList, true);
                        }

                        applyProjectFilters();
                    }
                });
    }

    private void applyProjectFilters() {
        view.getProjectsStore().applyFilters(null);
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
