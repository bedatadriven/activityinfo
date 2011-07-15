/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public abstract class AbstractEditorGridView<ModelT extends ModelData, PresenterT extends GridPresenter<ModelT>>
        extends AbstractGridView<ModelT, PresenterT> {

    private ModelT lastSelection;

    @Override
    protected void initGridListeners(final Grid<ModelT> grid) {

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
        
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelT>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<ModelT> se) {
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
    	if(ge.getGrid().getSelectionModel() instanceof CellSelectionModel) {
	        CellSelectionModel sm = (CellSelectionModel) ge.getGrid().getSelectionModel();
	
	        ModelT selection = (ModelT) sm.getSelectCell().model;
	        if(lastSelection != selection) {
	            lastSelection = selection;
	            presenter.onSelectionChanged(selection); 
	        }
    	}
    }
 
    @Override
    public void setActionEnabled(String actionId, boolean enabled) {
        super.setActionEnabled(actionId, enabled);
    }
}
