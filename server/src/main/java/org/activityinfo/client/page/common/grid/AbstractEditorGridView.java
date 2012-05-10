/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public abstract class AbstractEditorGridView<M extends ModelData, P extends GridPresenter<M>>
        extends AbstractGridView<M, P> {

    private M lastSelection;

    @Override
    protected void initGridListeners(final Grid<M> grid) {
        grid.getStore().setMonitorChanges(true);

        grid.getStore().addListener(Store.Update, new Listener<StoreEvent>() {
            public void handleEvent(StoreEvent se) {
                toolBar.setDirty(true);
            }
        });

        grid.addListener(Events.CellClick, new Listener<GridEvent>() {
            public void handleEvent(GridEvent be) {
                onCellClick(be);
            }
        });
        
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<M>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<M> se) {
				if(se.getSelectedItem() != null) {
					presenter.onSelectionChanged(se.getSelectedItem());
				}
			}
        });

        grid.addListener(Events.BeforeEdit, new Listener<GridEvent>() {
            public void handleEvent(GridEvent be) {
                if(!presenter.beforeEdit(be.getRecord(), be.getProperty())) {
                    be.setCancelled(true);
                }
            }
        });
    }

    protected void onCellClick(GridEvent ge) {
    	M selection = (M) ge.getGrid().getSelectionModel().getSelectedItem();
        if(lastSelection != selection) {
            lastSelection = selection;
            presenter.onSelectionChanged(selection); 
        }
    }
 
    @Override
    public void setActionEnabled(String actionId, boolean enabled) {
        super.setActionEnabled(actionId, enabled);
    }
}
