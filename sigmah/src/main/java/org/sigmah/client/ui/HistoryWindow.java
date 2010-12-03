package org.sigmah.client.ui;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.history.HistoryTokenListDTO;
import org.sigmah.shared.dto.history.HistoryTokenManager;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A simple window to show the history of a field.
 * 
 * @author tmi
 * 
 */
public final class HistoryWindow {

    /**
     * Singleton.
     */
    private static HistoryWindow instance;

    /**
     * Show the history.
     * 
     * @param tokens
     *            The history.
     * @param manager
     *            The history manager.
     */
    public static void show(List<HistoryTokenListDTO> tokens, HistoryTokenManager manager) {

        if (instance == null) {
            instance = new HistoryWindow();
        }

        instance.showHistory(tokens, manager);
    }

    /**
     * Returns the display name of the user in a history token.
     * 
     * @param token
     *            The history token.
     * @return The display name;
     */
    private static String getUserDisplayName(HistoryTokenListDTO token) {
        return token.getUserFirstName() != null ? token.getUserFirstName() + ' ' + token.getUserName() : token
                .getUserName();
    }

    private final Window window;
    private final ListStore<HistoryTokenListDTO> store;
    private final Grid<HistoryTokenListDTO> grid;
    private HistoryTokenManager manager;
    private final Label noHistoryLabel;

    /**
     * Builds the window.
     */
    private HistoryWindow() {

        // Store.
        store = new ListStore<HistoryTokenListDTO>();
        store.setStoreSorter(new StoreSorter<HistoryTokenListDTO>() {

            @Override
            public int compare(Store<HistoryTokenListDTO> store, HistoryTokenListDTO m1, HistoryTokenListDTO m2,
                    String property) {

                if ("user".equals(property)) {
                    return getUserDisplayName(m1).compareToIgnoreCase(getUserDisplayName(m2));
                } else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });

        // Plugins.
        final RowNumberer countColumn = new RowNumberer();

        // Grid.
        grid = new Grid<HistoryTokenListDTO>(store, new ColumnModel(Arrays.asList(getColumnModel(countColumn))));
        grid.getView().setForceFit(true);
        grid.setBorders(false);
        grid.setAutoExpandColumn("value");
        grid.addPlugin(countColumn);

        // Builds the window.
        window = new Window();
        window.setWidth(750);
        window.setHeight(400);
        window.setPlain(true);
        window.setModal(true);
        window.setBlinkModal(true);
        window.setLayout(new FitLayout());

        // Builds the no history label.
        noHistoryLabel = new Label(I18N.CONSTANTS.historyNoHistory());

        window.add(grid);
    }

    /**
     * Builds the columns model.
     * 
     * @param countColumn
     *            The row numberer plugin.
     * @return The columns model.
     */
    private ColumnConfig[] getColumnModel(RowNumberer countColumn) {

        final DateTimeFormat format = DateTimeFormat.getFormat(I18N.CONSTANTS.historyDateFormat());

        // Date.
        final ColumnConfig dateColumn = new ColumnConfig();
        dateColumn.setId("date");
        dateColumn.setHeader(I18N.CONSTANTS.historyDate());
        dateColumn.setWidth(125);
        dateColumn.setDateTimeFormat(format);

        // User.
        final ColumnConfig userColumn = new ColumnConfig();
        userColumn.setId("user");
        userColumn.setHeader(I18N.CONSTANTS.historyUser());
        userColumn.setWidth(135);
        userColumn.setRenderer(new GridCellRenderer<HistoryTokenListDTO>() {

            @Override
            public Object render(HistoryTokenListDTO model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<HistoryTokenListDTO> store, Grid<HistoryTokenListDTO> grid) {
                return getUserDisplayName(model);
            }
        });

        // Value.
        final ColumnConfig valueColumn = new ColumnConfig();
        valueColumn.setId("tokens");
        valueColumn.setHeader(I18N.CONSTANTS.historyValue());
        valueColumn.setSortable(false);
        valueColumn.setWidth(490);
        valueColumn.setRenderer(new GridCellRenderer<HistoryTokenListDTO>() {

            @Override
            public Object render(HistoryTokenListDTO model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<HistoryTokenListDTO> store, Grid<HistoryTokenListDTO> grid) {

                if (manager != null) {
                    return manager.renderHistoryToken(model);
                } else {
                    return null;
                }
            }
        });

        return new ColumnConfig[] { countColumn, dateColumn, userColumn, valueColumn };
    }

    /**
     * Show the history.
     * 
     * @param tokens
     *            The history.
     * @param manager
     *            The history manager.
     */
    private void showHistory(List<HistoryTokenListDTO> tokens, HistoryTokenManager manager) {

        // Hides if shown.
        window.hide();

        // Sets the current manager.
        this.manager = manager;

        // Reset window.
        window.removeAll();

        // Reloads store.
        store.removeAll();
        store.sort("date", SortDir.DESC);

        // Adds the tokens grid if there is a history.
        if (tokens != null && !tokens.isEmpty()) {
            store.add(tokens);
            window.add(grid);
        }
        // Adds the no-history label if there isn't a history.
        else {
            window.add(noHistoryLabel);
        }

        window.layout();

        // Shows window.
        window.setHeading(I18N.CONSTANTS.history() + ": " + manager.getElementLabel());
        window.show();
    }
}
