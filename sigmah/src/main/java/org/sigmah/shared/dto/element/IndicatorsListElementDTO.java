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
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.value.IndicatorsListValueDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class IndicatorsListElementDTO extends FlexibleElementDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.IndicatorsListElement";
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

        // Creates actions menu to manage the files list.
        final Button addButton = new Button(I18N.CONSTANTS.flexibleElementIndicatorsListAdd());

        final Button removeButton = new Button(I18N.CONSTANTS.remove());
        removeButton.setEnabled(false);

        final ToolBar actionsToolBar = new ToolBar();
        actionsToolBar.add(addButton);
        actionsToolBar.add(removeButton);

        // Fills the grid store with the files list.
        final ListStore<IndicatorDTO> store = new ListStore<IndicatorDTO>();

        if (valueResult != null && valueResult.isValueDefined()) {
            for (Serializable s : valueResult.getValuesObject()) {
                store.add(((IndicatorsListValueDTO) s).getIndicatorDTO());
            }
        }

        // Grid plugins.
        final CheckBoxSelectionModel<IndicatorDTO> selectionModel = new CheckBoxSelectionModel<IndicatorDTO>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        // Creates the grid which contains the files list.

        final EditorGrid<IndicatorDTO> grid = new EditorGrid<IndicatorDTO>(store, getColumnModel(selectionModel));
        grid.setSelectionModel(selectionModel);
        grid.setAutoExpandColumn("name");
        grid.setBorders(false);
        grid.getView().setForceFit(true);
        grid.addPlugin(selectionModel);

        // Creates the main panel.
        final ContentPanel panel = new ContentPanel();
        panel.setHeading(getLabel());
        panel.setLayout(new FitLayout());

        panel.setTopComponent(actionsToolBar);
        panel.add(grid);

        // Detects additions and deletions in the store and adjusts the grid
        // height accordingly.
        grid.addListener(Events.ViewReady, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                grid.getStore().addListener(Store.Add, new Listener<StoreEvent<IndicatorDTO>>() {
                    public void handleEvent(StoreEvent<IndicatorDTO> be) {
                        doAutoHeight(grid, panel);
                    }
                });
                grid.getStore().addListener(Store.Remove, new Listener<StoreEvent<IndicatorDTO>>() {
                    public void handleEvent(StoreEvent<IndicatorDTO> be) {
                        doAutoHeight(grid, panel);
                    }
                });
                doAutoHeight(grid, panel);
            }
        });

        // Manages action buttons activations.
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<IndicatorDTO>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<IndicatorDTO> se) {
                final List<IndicatorDTO> selection = se.getSelection();
                final boolean enabledState = selection != null && !selection.isEmpty();
                removeButton.setEnabled(enabledState);
            }
        });

        // Buttons listeners.
        addButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                // TODO implements
                MessageBox.info("Unsupported operation", "Method not yet implemented.", null);
            }
        });

        removeButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {

                final IndicatorDTO indicator = selectionModel.getSelectedItem();

                // Asks the client to confirm the indicator removal.
                MessageBox.confirm(I18N.CONSTANTS.flexibleElementIndicatorsListRemoval(),
                        I18N.MESSAGES.flexibleElementIndicatorsListConfirmRemove(indicator.getName()),
                        new Listener<MessageBoxEvent>() {
                            public void handleEvent(MessageBoxEvent ce) {

                                if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {

                                    // Removes it.
                                    // TODO implements
                                    MessageBox.info("Unsupported operation", "Method not yet implemented.", null);
                                }
                            }
                        });
            }
        });

        return panel;
    }

    /**
     * Defines the column model for the files list grid.
     * 
     * @param selectionModel
     *            The grid selection model.
     * @return The column model.
     */
    private ColumnModel getColumnModel(CheckBoxSelectionModel<IndicatorDTO> selectionModel) {

        final List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        columnConfigs.add(selectionModel.getColumn());

        // Incator's code.
        ColumnConfig column = new ColumnConfig();
        column.setId("code");
        column.setHeader(I18N.CONSTANTS.flexibleElementIndicatorsListCode());
        column.setEditor(null);
        column.setWidth(30);
        columnConfigs.add(column);

        // Indicator's name.
        column = new ColumnConfig();
        column.setId("name");
        column.setHeader(I18N.CONSTANTS.flexibleElementIndicatorsListName());
        column.setEditor(null);
        column.setWidth(100);
        columnConfigs.add(column);

        // Indicator's unit.
        column = new ColumnConfig();
        column.setId("units");
        column.setHeader(I18N.CONSTANTS.flexibleElementIndicatorsListUnits());
        column.setEditor(null);
        column.setWidth(80);
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
    private void doAutoHeight(Grid<IndicatorDTO> grid, ContentPanel cp) {
        if (grid.isViewReady()) {
            cp.setHeight((grid.getView().getBody().isScrollableX() ? 20 : 0) + grid.el().getFrameWidth("tb")
                    + grid.getView().getHeader().getHeight() + cp.getFrameHeight()
                    + grid.getView().getBody().firstChild().getHeight());
        }
    }

}
