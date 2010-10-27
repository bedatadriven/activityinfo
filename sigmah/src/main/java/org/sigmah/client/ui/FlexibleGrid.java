package org.sigmah.client.ui;

import java.util.Arrays;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;

/**
 * A basic GXT grid with some extra features.
 * 
 * @author tmi
 * 
 * @param <M>
 *            Type of data stored in this grid.
 */
public class FlexibleGrid<M extends ModelData> extends EditorGrid<M> {

    /**
     * The number of elements to display (before adding a scrollbar).<br/>
     * A negative or <code>null</code> value displays all elements by default.
     */
    private int visibleElementsCount = -1;

    /**
     * Creates a new grid.
     * 
     * @param store
     *            The data store.
     * @param selection
     *            If the grid must implements a default checkbox selection
     *            model.
     * @param columns
     *            The columns model configurations.
     */
    public FlexibleGrid(ListStore<M> store, GridSelectionModel<M> selectionModel, ColumnConfig... columns) {
        this(store, selectionModel, -1, columns);
    }

    /**
     * Creates a new grid.
     * 
     * @param store
     *            The data store.
     * @param selectionModel
     *            If the grid must implements a default checkbox selection
     *            model.
     * @param visibleElementsCount
     *            The number of elements displayed.
     * @param columns
     *            The columns model configurations.
     */
    public FlexibleGrid(ListStore<M> store, GridSelectionModel<M> selectionModel, int visibleElementsCount,
            ColumnConfig... columns) {
        super(store, new ColumnModel(Arrays.asList(columns)));

        this.visibleElementsCount = visibleElementsCount;

        // Some default values.
        this.getView().setForceFit(true);
        setBorders(false);

        if (selectionModel != null) {
            setSelectionModel(selectionModel);
            if (selectionModel instanceof CheckBoxSelectionModel) {
                addPlugin((CheckBoxSelectionModel<M>) selectionModel);
            }
        }

        // Manages the grid's height adjustments.
        this.addListener(Events.ViewReady, new Listener<ComponentEvent>() {
            @Override
            public void handleEvent(ComponentEvent be) {
                getStore().addListener(Store.Add, new Listener<StoreEvent<M>>() {
                    @Override
                    public void handleEvent(StoreEvent<M> be) {
                        doAutoHeight();
                    }
                });
                getStore().addListener(Store.Remove, new Listener<StoreEvent<M>>() {
                    @Override
                    public void handleEvent(StoreEvent<M> be) {
                        doAutoHeight();
                    }
                });
                getStore().addListener(Store.Clear, new Listener<StoreEvent<M>>() {
                    @Override
                    public void handleEvent(StoreEvent<M> be) {
                        doAutoHeight();
                    }
                });
                doAutoHeight();
            }
        });
    }

    /**
     * Gets the number of elements displayed.
     * 
     * @return The number of elements displayed.
     */
    public int getVisibleElementsCount() {
        return visibleElementsCount;
    }

    /**
     * Sets the number of elements to display (before adding a scrollbar).<br/>
     * A negative or <code>null</code> value displays all elements by default.
     * 
     * @param visibleElementsCount
     *            The number of elements to display.
     */
    public void setVisibleElementsCount(int visibleElementsCount) {
        this.visibleElementsCount = visibleElementsCount;
    }

    /**
     * Computes and applies the grid height depending on the number of desired
     * elements to display.
     */
    private void doAutoHeight() {

        final int elementsHeight;
        if (visibleElementsCount <= 0) {
            // Shows all elements.
            elementsHeight = this.getView().getBody().firstChild().getHeight();
        } else {
            // Shows only desired elements.
            elementsHeight = this.getView().getBody().firstChild().firstChild().getHeight()
                    * (getStore().getCount() > visibleElementsCount ? visibleElementsCount : getStore().getCount());
        }

        this.setHeight((this.getView().getBody().isScrollableX() ? 20 : 0) + this.getView().getHeader().getHeight()
                + this.el().getFrameWidth("tb") + elementsHeight);
    }
}
