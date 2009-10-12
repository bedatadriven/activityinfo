package org.activityinfo.client.page.table;

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
    protected void renderChildren(PivotGridPanel.PivotTableRow parent) {
        super.renderChildren(parent);

        if(parent == null) {
            for(PivotGridPanel.PivotTableRow row : getTreeStore().getRootItems()) {

                setExpanded(row, true, true);
            }
        }
    }



}
