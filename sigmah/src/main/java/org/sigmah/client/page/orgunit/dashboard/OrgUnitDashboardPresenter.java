/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.orgunit.OrgUnitPresenter;
import org.sigmah.client.page.orgunit.OrgUnitState;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

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

        public abstract TreePanel<OrgUnitDTOLight> getTree();
    }

    /**
     * This presenter view.
     */
    private View view;

    private final OrgUnitPresenter mainPresenter;
    private OrgUnitDTOLight currentOrgUnitDTO;
    private final EventBus eventBus;

    public OrgUnitDashboardPresenter(Dispatcher dispatcher, EventBus eventBus, OrgUnitPresenter mainPresenter) {
        this.eventBus = eventBus;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public Component getView() {

        if (view == null) {
            view = new OrgUnitDashboardView();
        }

        // If the current org unit has changed, clear the view
        if (!mainPresenter.getCurrentOrgUnitDTO().equals(currentOrgUnitDTO)) {
            currentOrgUnitDTO = mainPresenter.getCurrentOrgUnitDTO().light(null);

            view.getTreeStore().removeAll();
            view.getTreeStore().add(currentOrgUnitDTO, true);

            // Expand/collapse on click.
            view.getTree().addListener(Events.OnClick, new Listener<TreePanelEvent<OrgUnitDTOLight>>() {
                @Override
                public void handleEvent(TreePanelEvent<OrgUnitDTOLight> tpe) {
                    view.getTree().setExpanded(tpe.getItem(), !view.getTree().isExpanded(tpe.getItem()));
                }
            });

            // Go to the org unit page on double-click.
            view.getTree().addListener(Events.OnDoubleClick, new Listener<TreePanelEvent<OrgUnitDTOLight>>() {
                @Override
                public void handleEvent(TreePanelEvent<OrgUnitDTOLight> tpe) {
                    view.getTree().setExpanded(tpe.getItem(), view.getTree().isExpanded(tpe.getItem()));
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new OrgUnitState(tpe
                            .getItem().getId())));
                }
            });
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // nothing
    }
}
