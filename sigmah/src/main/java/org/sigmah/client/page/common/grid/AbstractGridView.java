package org.sigmah.client.page.common.grid;

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
import org.sigmah.client.Application;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.page.common.toolbar.ActionToolBar;

/*
 * @author Alex Bertram
 */

public abstract class AbstractGridView<ModelT extends ModelData, PresenterT extends GridPresenter<ModelT>>
        extends ContentPanel
        implements GridView<PresenterT, ModelT> {

    protected ActionToolBar toolBar;
    protected PresenterT presenter;
    protected PagingToolBar pagingBar;
    private Grid<ModelT> grid;


    public void init(final PresenterT presenter, Store store) {
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

        /*
         *  In some cases, there is async call before the user inerface can be
         * loaded. So we have to make sure our new components are rendered
         */
        if (this.isRendered()) {
            this.layout();
        }

    }

    protected void initGridListeners(Grid<ModelT> grid) {

        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelT>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<ModelT> se) {
                presenter.onSelectionChanged(se.getSelectedItem());
            }
        });
    }

    protected abstract Grid<ModelT> createGridAndAddToContainer(Store store);

    protected void createToolBar() {
        toolBar = new ActionToolBar(presenter);
        setTopComponent(toolBar);

        initToolBar();

        toolBar.setDirty(false);
    }

    protected abstract void initToolBar();

    public void setActionEnabled(String actionId, boolean enabled) {
        toolBar.setActionEnabled(actionId, enabled);
    }

    public void confirmDeleteSelected(ConfirmCallback callback) {
        callback.confirmed();
    }


    public ModelT getSelection() {
        GridSelectionModel<ModelT> sm = grid.getSelectionModel();
        if (sm instanceof CellSelectionModel) {
            CellSelectionModel<ModelT>.CellSelection cell = ((CellSelectionModel<ModelT>) sm).getSelectCell();
            return cell == null ? null : cell.model;
        } else {
            return sm.getSelectedItem();
        }
    }

    public AsyncMonitor getDeletingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.deleting());
    }

    public AsyncMonitor getSavingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.saving());
    }
}
