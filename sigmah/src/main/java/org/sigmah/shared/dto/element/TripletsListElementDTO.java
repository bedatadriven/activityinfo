/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.value.TripletValueDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class TripletsListElementDTO extends FlexibleElementDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.TripletsListElement";
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {

        if (result == null || result.getValuesObject() == null) {
            return false;
        }

        return !result.getValuesObject().isEmpty();
    }

    @Override
    public Component getComponent(ValueResult valueResult) {

        // Creates actions toolbar to manage the triplets list.
        final Button addButton = new Button(I18N.CONSTANTS.addItem());
        final Button deleteButton = new Button(I18N.CONSTANTS.remove());
        deleteButton.setEnabled(false);

        final ToolBar actionsToolBar = new ToolBar();
        actionsToolBar.add(addButton);
        actionsToolBar.add(new SeparatorToolItem());
        actionsToolBar.add(deleteButton);

        // Creates the top panel of the grid.
        final ContentPanel topPanel = new ContentPanel();
        topPanel.setHeaderVisible(false);
        topPanel.setLayout(new FlowLayout());

        topPanel.add(actionsToolBar);

        // Fills the grid store with the triplets lists
        final ListStore<TripletValueDTO> store = new ListStore<TripletValueDTO>();

        if (valueResult != null && valueResult.isValueDefined()) {
            for (Serializable s : valueResult.getValuesObject()) {
                store.add((TripletValueDTO) s);
            }
        }

        // Creates the grid which contains the triplets list.
        final CheckBoxSelectionModel<TripletValueDTO> selectionModel = new CheckBoxSelectionModel<TripletValueDTO>();
        final EditorGrid<TripletValueDTO> grid = new EditorGrid<TripletValueDTO>(store, getColumnModel(selectionModel));
        grid.setSelectionModel(selectionModel);
        grid.setAutoExpandColumn("name");
        grid.setBorders(false);
        grid.getView().setForceFit(true);
        grid.addPlugin(selectionModel);

        // Creates the main panel.
        final ContentPanel panel = new ContentPanel();
        panel.setHeading(getLabel());
        panel.setLayout(new FitLayout());

        panel.setTopComponent(topPanel);
        panel.add(grid);

        // Detects additions and deletions in the store and adjusts the grid
        // height accordingly.
        grid.addListener(Events.ViewReady, new Listener<ComponentEvent>() {
            @Override
			public void handleEvent(ComponentEvent be) {
                grid.getStore().addListener(Store.Add, new Listener<StoreEvent<TripletValueDTO>>() {
                    @Override
					public void handleEvent(StoreEvent<TripletValueDTO> be) {
                        doAutoHeight(grid, panel);
                    }
                });
                grid.getStore().addListener(Store.Remove, new Listener<StoreEvent<TripletValueDTO>>() {
                    @Override
					public void handleEvent(StoreEvent<TripletValueDTO> be) {
                        doAutoHeight(grid, panel);
                    }
                });
                grid.getStore().addListener(Store.Clear, new Listener<StoreEvent<TripletValueDTO>>() {
                    @Override
					public void handleEvent(StoreEvent<TripletValueDTO> be) {
                        doAutoHeight(grid, panel);
                    }
                });
                doAutoHeight(grid, panel);
            }
        });

        grid.addListener(Events.AfterEdit, new Listener<GridEvent<TripletValueDTO>>() {

            @Override
            public void handleEvent(GridEvent<TripletValueDTO> be) {
                // Edit an existing triplet
                final TripletValueDTO valueDTO = grid.getStore().getAt(be.getRowIndex());
                valueDTO.setIndex(be.getRowIndex());

                handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, valueDTO, ValueEvent.ChangeType.EDIT));
            }

        });

        // Manages action buttons activations.
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<TripletValueDTO>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<TripletValueDTO> se) {
                final List<TripletValueDTO> selection = se.getSelection();

                final boolean enabledState = selection != null && !selection.isEmpty();
                deleteButton.setEnabled(enabledState);
            }
        });

        // Buttons listeners.
        addButton.addListener(Events.OnClick, new ActionListener(grid, true));
        deleteButton.addListener(Events.OnClick, new ActionListener(grid, false));

        return panel;
    }

    /**
     * Defines the column model for the triplets list grid.
     * 
     * @param selectionModel
     *            The grid selection model.
     * @return The column model.
     */
    private ColumnModel getColumnModel(CheckBoxSelectionModel<TripletValueDTO> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());

        ColumnConfig column = new ColumnConfig();
        column.setId("code");
        column.setHeader(I18N.CONSTANTS.flexibleElementTripletsListCode());
        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        column.setWidth(100);
        columnConfigs.add(column);

        column = new ColumnConfig();
        column.setId("name");
        column.setHeader(I18N.CONSTANTS.flexibleElementTripletsListName());
        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        column.setWidth(100);
        columnConfigs.add(column);

        column = new ColumnConfig();
        column.setId("period");
        column.setHeader(I18N.CONSTANTS.flexibleElementTripletsListPeriod());
        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        column.setWidth(60);
        columnConfigs.add(column);

        return new ColumnModel(columnConfigs);
    }

    /**
     * Adjusts the grid height for the current elements number.
     * 
     * @param grid
     *            The grid.
     * @param cp
     *            The grid's parent panel.
     */
    private void doAutoHeight(Grid<TripletValueDTO> grid, ContentPanel cp) {
        if (grid.isViewReady()) {
            cp.setHeight((grid.getView().getBody().isScrollableX() ? 20 : 0) + grid.el().getFrameWidth("tb")
                    + grid.getView().getHeader().getHeight() + cp.getFrameHeight()
                    + grid.getView().getBody().firstChild().getHeight());
        }
    }

    /**
     * Manages grid actions.
     * 
     * @author tmi
     * 
     */
    private class ActionListener implements Listener<ButtonEvent> {

        private EditorGrid<TripletValueDTO> grid;
        private boolean isAddAction;

        public ActionListener(EditorGrid<TripletValueDTO> grid, boolean isAddAction) {
            this.grid = grid;
            this.isAddAction = isAddAction;
        }

        @Override
        public void handleEvent(ButtonEvent be) {
            // Add a new triplet
            if (isAddAction) {
                TripletValueDTO addedValue = new TripletValueDTO();
                addedValue.setCode("-" + I18N.CONSTANTS.flexibleElementTripletsListCode() + "-");
                addedValue.setName("-" + I18N.CONSTANTS.flexibleElementTripletsListName() + "-");
                addedValue.setPeriod("-" + I18N.CONSTANTS.flexibleElementTripletsListPeriod() + "-");

                grid.getStore().add(addedValue);
                addedValue.setIndex(grid.getStore().indexOf(addedValue));
                handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, addedValue, ValueEvent.ChangeType.ADD));

                // Required element ?
                if (getValidates()) {
                    handlerManager.fireEvent(new RequiredValueEvent(true));
                }
            }
            // Remove some existing triplets
            else {
                for (TripletValueDTO removedValue : grid.getSelectionModel().getSelectedItems()) {
                    removedValue.setIndex(grid.getStore().indexOf(removedValue));
                    grid.getStore().remove(removedValue);
                    // TODO creates fire method for addition and deletion
                    handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, removedValue, ValueEvent.ChangeType.REMOVE));
                }

                if (grid.getStore().getCount() == 0) {
                    handlerManager.fireEvent(new RequiredValueEvent(false));
                }
            }

        }
    }

}
