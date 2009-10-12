package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.widget.grid.Grid;
/*
 * @author Alex Bertram
 */

public class SiteGridDragSource extends DragSource {

    private final Grid grid;

    public SiteGridDragSource(Grid grid) {
        super(grid);
        this.grid = grid;
    }

    @Override
    protected void onDragStart(DNDEvent e) {
        int rowIndex = grid.getView().findRowIndex(e.getTarget());
        if (rowIndex == -1) {
            e.setCancelled(true);
            return;
        }


        ModelData site = grid.getStore().getAt(rowIndex);

        e.setCancelled(false);
        e.setData(grid.getStore().getRecord(site));
        e.getStatus().update("");

        e.getStatus().update("Déposer sur le carte");

    }
}

