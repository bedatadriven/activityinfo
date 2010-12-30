/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.dashboard.OrgUnitTreeGrid;
import org.sigmah.client.page.dashboard.ProjectsListPanel;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

/**
 * 
 * @author tmi
 */
public class OrgUnitDashboardView extends OrgUnitDashboardPresenter.View {

    private final OrgUnitTreeGrid tree;

    private ProjectsListPanel projectsListPanel;

    public OrgUnitDashboardView(EventBus eventBus, Dispatcher dispatcher, Authentication authentication) {

        tree = new OrgUnitTreeGrid(eventBus, false);
        projectsListPanel = new ProjectsListPanel(dispatcher, authentication);

        setHeading(I18N.CONSTANTS.orgunitTree());

        final VBoxLayout layout = new VBoxLayout() {
            @Override
            protected void onLayout(Container<?> container, El target) {
                super.onLayout(container, target);
                innerCt.addStyleName("main-background");
            }
        };
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);

        setLayout(layout);
        setTopComponent(tree.getToolbar());

        VBoxLayoutData flex = new VBoxLayoutData(new Margins(0, 0, 5, 0));
        flex.setFlex(1);
        add(tree.getTreeGrid(), flex);

        flex = new VBoxLayoutData(new Margins(0, 0, 0, 0));
        flex.setFlex(1);
        add(projectsListPanel.getProjectsPanel(), flex);
    }

    @Override
    public TreeStore<OrgUnitDTOLight> getTreeStore() {
        return tree.getStore();
    }

    @Override
    public ProjectsListPanel getProjectsListPanel() {
        return projectsListPanel;
    }
}
