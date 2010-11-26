package org.sigmah.client.util;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.user.client.Event;

/**
 * This selection model can be only used with a {@link TreeGrid}. When an
 * element is selected, all its children elements will be selected too.
 * 
 * @author tmi
 * 
 * @param <M>
 */
public class TreeGridCheckboxSelectionModel<M extends BaseTreeModel> extends CheckBoxSelectionModel<M> {

    @Override
    protected void handleMouseDown(GridEvent<M> e) {
        if (e.getEvent().getButton() == Event.BUTTON_LEFT && e.getTarget().getClassName().equals("x-grid3-row-checker")) {
            M m = listStore.getAt(e.getRowIndex());
            if (m != null) {
                if (isSelected(m)) {
                    deselect(m);
                    deselectChildren(m);
                } else {
                    select(m, true);
                    selectChildren(m, true);
                }
            }
        } else {
            super.handleMouseDown(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void deselectChildren(M m) {
        for (ModelData md : m.getChildren()) {

            if (md instanceof BaseTreeModel) {
                final M child = (M) md;
                deselect(child);
                deselectChildren(child);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void selectChildren(M m, boolean keepExisting) {
        for (ModelData md : m.getChildren()) {

            if (md instanceof BaseTreeModel) {
                final M child = (M) md;
                select(child, keepExisting);
                selectChildren(child, keepExisting);
            }
        }
    }
}
