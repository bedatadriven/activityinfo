/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.ui.FlexibleGrid;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.history.HistoryTokenDTO;
import org.sigmah.shared.dto.history.HistoryTokenListDTO;
import org.sigmah.shared.dto.value.ListableValue;
import org.sigmah.shared.dto.value.TripletValueDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

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

        if (result == null || !result.isValueDefined()) {
            return false;
        }

        return !result.getValuesObject().isEmpty();
    }

    @Override
    protected Component getComponent(ValueResult valueResult, final boolean enabled) {

        // Creates actions toolbar to manage the triplets list.
        final Button addButton = new Button(I18N.CONSTANTS.addItem());
        addButton.setEnabled(enabled);

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
            for (ListableValue s : valueResult.getValuesObject()) {
                store.add((TripletValueDTO) s);
            }
        }

        // Creates the grid which contains the triplets list.
        final CheckBoxSelectionModel<TripletValueDTO> selectionModel = new CheckBoxSelectionModel<TripletValueDTO>();
        final FlexibleGrid<TripletValueDTO> grid = new FlexibleGrid<TripletValueDTO>(store, selectionModel,
                getColumnModel(selectionModel));
        grid.setAutoExpandColumn("name");
        grid.setVisibleElementsCount(5);

        // Creates the main panel.
        final ContentPanel panel = new ContentPanel();
        panel.setHeading(getLabel());
        panel.setBorders(true);
        panel.setLayout(new FitLayout());

        panel.setTopComponent(topPanel);
        panel.add(grid);

        grid.addListener(Events.AfterEdit, new Listener<GridEvent<TripletValueDTO>>() {

            @Override
            public void handleEvent(GridEvent<TripletValueDTO> be) {
                // Edit an existing triplet
                final TripletValueDTO valueDTO = grid.getStore().getAt(be.getRowIndex());

                valueDTO.setIndex(be.getRowIndex());

                handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, valueDTO,
                        ValueEvent.ChangeType.EDIT));
            }

        });

        // Manages action buttons activations.
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<TripletValueDTO>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<TripletValueDTO> se) {
                final List<TripletValueDTO> selection = se.getSelection();

                final boolean enabledState = enabled && selection != null && !selection.isEmpty();
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
    private ColumnConfig[] getColumnModel(CheckBoxSelectionModel<TripletValueDTO> selectionModel) {

        final ColumnConfig codeColumn = new ColumnConfig();
        codeColumn.setId("code");
        codeColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListCode());
        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        codeColumn.setEditor(new CellEditor(text));
        codeColumn.setWidth(100);

        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId("name");
        nameColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListName());
        text = new TextField<String>();
        text.setAllowBlank(false);
        nameColumn.setEditor(new CellEditor(text));
        nameColumn.setWidth(100);

        final ColumnConfig periodColumn = new ColumnConfig();
        periodColumn.setId("period");
        periodColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListPeriod());
        text = new TextField<String>();
        text.setAllowBlank(false);
        periodColumn.setEditor(new CellEditor(text));
        periodColumn.setWidth(60);

        return new ColumnConfig[] { selectionModel.getColumn(), codeColumn, nameColumn, periodColumn };
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

                // Fires the value change event.
                handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, addedValue,
                        ValueEvent.ChangeType.ADD));

            }
            // Remove some existing triplets
            else {

                for (TripletValueDTO removedValue : grid.getSelectionModel().getSelectedItems()) {

                    removedValue.setIndex(grid.getStore().indexOf(removedValue));
                    grid.getStore().remove(removedValue);

                    // Fires the value change event.
                    handlerManager.fireEvent(new ValueEvent(TripletsListElementDTO.this, removedValue,
                            ValueEvent.ChangeType.REMOVE));
                }
            }

            // Required element ?
            if (getValidates()) {
                handlerManager.fireEvent(new RequiredValueEvent(grid.getStore().getCount() > 0));
            }
        }
    }

    @Override
    public Object renderHistoryToken(final HistoryTokenListDTO token) {

        final Label details = new Label();
        details.addStyleName("history-details");

        if (token.getTokens().size() == 1) {
            details.setText("1 " + I18N.CONSTANTS.historyModification());
        } else {
            details.setText(token.getTokens().size() + " " + I18N.CONSTANTS.historyModifications());
        }

        details.addClickHandler(new ClickHandler() {

            private Window window;

            @Override
            public void onClick(ClickEvent e) {

                if (window == null) {

                    // Builds window.
                    window = new Window();
                    window.setAutoHide(true);
                    window.setModal(false);
                    window.setPlain(true);
                    window.setHeaderVisible(true);
                    window.setClosable(true);
                    window.setLayout(new FitLayout());
                    window.setWidth(750);
                    window.setHeight(400);
                    window.setBorders(false);
                    window.setResizable(true);

                    // Builds grid.
                    final ListStore<TripletValueDTO> store = new ListStore<TripletValueDTO>();
                    final com.extjs.gxt.ui.client.widget.grid.Grid<TripletValueDTO> grid = new com.extjs.gxt.ui.client.widget.grid.Grid<TripletValueDTO>(
                            store, new ColumnModel(Arrays.asList(getColumnModel())));
                    grid.getView().setForceFit(true);
                    grid.setBorders(false);
                    grid.setAutoExpandColumn("name");

                    window.add(grid);

                    // Fills store.
                    for (final HistoryTokenDTO t : token.getTokens()) {

                        final List<String> l = ValueResultUtils.splitElements(t.getValue());

                        final TripletValueDTO triplet = new TripletValueDTO();
                        triplet.setCode(l.get(0));
                        triplet.setName(l.get(1));
                        triplet.setPeriod(l.get(2));
                        triplet.setType(t.getType());

                        store.add(triplet);
                    }
                }

                window.setPagePosition(e.getNativeEvent().getClientX(), e.getNativeEvent().getClientY());
                window.show();
            }

            private ColumnConfig[] getColumnModel() {

                // Change type.
                final ColumnConfig typeColumn = new ColumnConfig();
                typeColumn.setId("type");
                typeColumn.setHeader(I18N.CONSTANTS.historyModificationType());
                typeColumn.setWidth(150);
                typeColumn.setSortable(false);
                typeColumn.setRenderer(new GridCellRenderer<TripletValueDTO>() {

                    @Override
                    public Object render(TripletValueDTO model, String property, ColumnData config, int rowIndex,
                            int colIndex, ListStore<TripletValueDTO> store, Grid<TripletValueDTO> grid) {

                        if (model.getType() != null) {
                            switch (model.getType()) {
                            case ADD:
                                return I18N.CONSTANTS.historyAdd();
                            case EDIT:
                                return I18N.CONSTANTS.historyEdit();
                            case REMOVE:
                                return I18N.CONSTANTS.historyRemove();
                            default:
                                return "-";
                            }
                        } else {
                            return "-";
                        }
                    }
                });

                // Code.
                final ColumnConfig codeColumn = new ColumnConfig();
                codeColumn.setId("code");
                codeColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListCode());
                codeColumn.setWidth(200);

                // Name.
                final ColumnConfig nameColumn = new ColumnConfig();
                nameColumn.setId("name");
                nameColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListName());
                nameColumn.setWidth(200);

                // Period.
                final ColumnConfig periodColumn = new ColumnConfig();
                periodColumn.setId("period");
                periodColumn.setHeader(I18N.CONSTANTS.flexibleElementTripletsListPeriod());
                periodColumn.setWidth(200);

                return new ColumnConfig[] { typeColumn, codeColumn, nameColumn, periodColumn };
            }
        });

        return details;
    }
}
