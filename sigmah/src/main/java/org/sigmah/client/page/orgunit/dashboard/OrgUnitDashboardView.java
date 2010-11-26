/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.dashboard.OrgUnitTreeGrid;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 * @author tmi
 */
public class OrgUnitDashboardView extends OrgUnitDashboardPresenter.View {

    private final OrgUnitTreeGrid tree;

    public OrgUnitDashboardView(EventBus eventBus) {

        tree = new OrgUnitTreeGrid(eventBus, false);

        setHeading(I18N.CONSTANTS.orgunitTree());

        setLayout(new FitLayout());
        setTopComponent(tree.getToolbar());
        add(tree.getTreeGrid());
    }

    @Override
    public TreeStore<OrgUnitDTOLight> getTreeStore() {
        return tree.getStore();
    }
}
