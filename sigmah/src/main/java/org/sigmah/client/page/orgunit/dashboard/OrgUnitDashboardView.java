/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.dashboard;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.orgunit.OrgUnitImageBundle;
import org.sigmah.shared.dto.OrgUnitDTOLight;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

/**
 * 
 * @author tmi
 */
public class OrgUnitDashboardView extends OrgUnitDashboardPresenter.View {

    private final Button expandButton;
    private final Button collapseButton;
    private final ToolBar toolbar;
    private final TreeStore<OrgUnitDTOLight> treeStore;
    private final TreePanel<OrgUnitDTOLight> tree;

    public OrgUnitDashboardView() {

        // Tree store
        treeStore = new TreeStore<OrgUnitDTOLight>();

        // Tree
        tree = new TreePanel<OrgUnitDTOLight>(treeStore);
        tree.setDisplayProperty("completeName");
        tree.setToolTip(I18N.CONSTANTS.orgunitTreeOpen());
        tree.getStyle().setLeafIcon(OrgUnitImageBundle.ICONS.orgUnitSmall());
        tree.getStyle().setNodeCloseIcon(OrgUnitImageBundle.ICONS.orgUnitSmall());
        tree.getStyle().setNodeOpenIcon(OrgUnitImageBundle.ICONS.orgUnitSmallTransparent());

        expandButton = new Button(I18N.CONSTANTS.expandAll(), IconImageBundle.ICONS.expand(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        tree.expandAll();
                    }
                });

        collapseButton = new Button(I18N.CONSTANTS.collapseAll(), IconImageBundle.ICONS.collapse(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        tree.collapseAll();
                    }
                });

        // Toolbar
        toolbar = new ToolBar();
        toolbar.setAlignment(HorizontalAlignment.LEFT);

        toolbar.add(expandButton);
        toolbar.add(collapseButton);

        setHeading(I18N.CONSTANTS.orgunitTree());
        setTopComponent(toolbar);
        add(tree);
    }

    @Override
    public TreeStore<OrgUnitDTOLight> getTreeStore() {
        return treeStore;
    }

    @Override
    public TreePanel<OrgUnitDTOLight> getTree() {
        return tree;
    }
}
