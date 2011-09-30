/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.toolbar.ActionToolBar;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

public abstract class AbstractGridView<M extends ModelData, P extends GridPresenter<M>>
        extends 
        	ContentPanel
        implements 
        	GridView<P, M> {

    protected ActionToolBar toolBar;
    protected P presenter;
    protected PagingToolBar pagingBar;
    private Grid<M> grid;
    
    protected abstract Grid<M> createGridAndAddToContainer(Store store);
    protected abstract void initToolBar();

    public void init(final P presenter, Store store) {
        this.presenter = presenter;

        createToolBar();

        grid = createGridAndAddToContainer(store);

        initGridListeners(grid);

        if (store instanceof ListStore) {
            Loader loader = ((ListStore) store).getLoader();
            if (loader instanceof PagingLoader) {
                pagingBar = new PagingToolBar(presenter.getPageSize());
                setBottomComponent(pagingBar);
                pagingBar.bind((PagingLoader<?>) loader);
            }
        }

        /**  In some cases, there is async call before the user inerface can be
         * loaded. So we have to make sure our new components are rendered */
        if (this.isRendered()) {
            this.layout();
        }
    }

    protected void initGridListeners(Grid<M> grid) {
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<M>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<M> se) {
                presenter.onSelectionChanged(se.getSelectedItem());
            }
        });
    }

    protected void createToolBar() {
        toolBar = new ActionToolBar(presenter);
        setTopComponent(toolBar);

        initToolBar();

        toolBar.setDirty(false);
    }

    public void setActionEnabled(String actionId, boolean enabled) {
        toolBar.setActionEnabled(actionId, enabled);
    }

    public void confirmDeleteSelected(ConfirmCallback callback) {
        callback.confirmed();
    }

    public M getSelection() {
        GridSelectionModel<M> sm = grid.getSelectionModel();
        if (sm instanceof CellSelectionModel) {
            CellSelectionModel<M>.CellSelection cell = ((CellSelectionModel<M>) sm).getSelectCell();
            return cell == null ? null : cell.model;
        } else {
            return sm.getSelectedItem();
        }
    }

    public AsyncMonitor getDeletingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.deleting());
    }

    public AsyncMonitor getSavingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.saving());
    }
}
