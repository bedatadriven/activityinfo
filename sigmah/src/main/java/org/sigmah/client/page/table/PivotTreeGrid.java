/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
/*
 * @author Alex Bertram
 */

public class PivotTreeGrid extends TreeGrid<PivotGridPanel.PivotTableRow> {

    public PivotTreeGrid(TreeStore store, ColumnModel cm) {
        super(store, cm);

        setAutoExpandColumn("header");

        addListener(Events.ContextMenu, new Listener<GridEvent>() {


            public void handleEvent(GridEvent be) {

               // beforeContextMenu(be);

            }
        });
    }


    @Override
    protected void renderChildren(PivotGridPanel.PivotTableRow parent, boolean auto) {
        super.renderChildren(parent, auto);

        if(parent == null) {
            for(PivotGridPanel.PivotTableRow row : getTreeStore().getRootItems()) {

                setExpanded(row, true, true);
            }
        }
    }



}
