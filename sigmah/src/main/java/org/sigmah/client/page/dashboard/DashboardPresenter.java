/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.ProjectDTOLight;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Home screen of sigmah. Displays the main menu and a reminder of urgent tasks.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardPresenter implements Page {
    public static final PageId PAGE_ID = new PageId("welcome");

    private Dispatcher dispatcher;
    private Widget widget;

    /**
     * Model containing the displayed projects
     */
    private TreeStore<ProjectDTOLight> projectStore;

    /**
     * Model containing the countries available to the user.
     */
    private ListStore<CountryDTO> countryStore;

    @Inject
    public DashboardPresenter(final EventBus eventBus, final Dispatcher dispatcher, final Authentication authentication) {
        this.dispatcher = dispatcher;

        // Initialization of the models
        projectStore = new TreeStore<ProjectDTOLight>();
        projectStore.setMonitorChanges(true);

        countryStore = new ListStore<CountryDTO>();

        // Creation of the view
        final DashboardView view = new DashboardView(eventBus, dispatcher, authentication, projectStore, countryStore);

        this.widget = view;
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return widget;
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
        dispatcher.execute(new GetCountries(true), null, new AsyncCallback<CountryResult>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CountryResult countryResult) {
                countryStore.add(countryResult.getData());
            }
        });

        return true;
    }

    @Override
    public void shutdown() {
    }
}
