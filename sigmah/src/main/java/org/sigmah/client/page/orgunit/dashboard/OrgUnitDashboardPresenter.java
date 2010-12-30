/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.page.dashboard.ProjectsListPanel;
import org.sigmah.client.page.orgunit.OrgUnitPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;

/**
 * 
 * @author tmi
 */
public class OrgUnitDashboardPresenter implements SubPresenter {

    /**
     * Description of the view managed by this presenter.
     */
    public static abstract class View extends ContentPanel {

        public abstract TreeStore<OrgUnitDTOLight> getTreeStore();

        public abstract ProjectsListPanel getProjectsListPanel();
    }

    /**
     * This presenter view.
     */
    private View view;

    private final OrgUnitPresenter mainPresenter;
    private OrgUnitDTOLight currentOrgUnitDTO;
    private final EventBus eventBus;
    private final Dispatcher dispatcher;
    private final Authentication authentication;

    public OrgUnitDashboardPresenter(Dispatcher dispatcher, EventBus eventBus, Authentication authentication,
            OrgUnitPresenter mainPresenter) {
        this.eventBus = eventBus;
        this.mainPresenter = mainPresenter;
        this.dispatcher = dispatcher;
        this.authentication = authentication;
    }

    @Override
    public Component getView() {

        if (view == null) {
            view = new OrgUnitDashboardView(eventBus, dispatcher, authentication);
        }

        // If the current org unit has changed, clear the view
        if (!mainPresenter.getCurrentOrgUnitDTO().equals(currentOrgUnitDTO)) {

            currentOrgUnitDTO = mainPresenter.getCurrentOrgUnitDTO().light();

            view.getTreeStore().removeAll();
            for (final OrgUnitDTOLight child : currentOrgUnitDTO.getChildrenDTO()) {
                view.getTreeStore().add(child, true);
            }

            view.getProjectsListPanel().refresh(currentOrgUnitDTO.getId());
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // nothing
    }
}
