package org.sigmah.client.page.project.logframe.grid;

import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * An utility class to manage a HTML table with GXT CSS styles.
 * 
 * @author tmi
 * 
 */
public class HTMLTableUtils {

    /**
     * Provides only static methods.
     */
    private HTMLTableUtils() {
    }

    /**
     * Applies the CSS column-header styles to a cell.
     * 
     * @param table
     *            The GWT table.
     * @param row
     *            The row index.
     * @param column
     *            The column index.
     */
    public static void applyColumnHeaderStyles(HTMLTable table, int row, int column) {
        table.getCellFormatter().addStyleName(row, column, "x-grid3-header");
        table.getCellFormatter().addStyleName(row, column, "x-grid3-hd");
        table.getCellFormatter().addStyleName(row, column, "x-grid3-hd-row");
        table.getCellFormatter().addStyleName(row, column, "x-grid3-td-favorite");
        table.getCellFormatter().addStyleName(row, column, "x-grid3-cell");

        final Widget w = table.getWidget(row, column);
        if (w != null) {
            w.addStyleName("x-grid3-hd-inner");
            w.addStyleName("x-grid3-hd-favorite ");
            w.addStyleName("x-component");
        }
    }

    /**
     * Applies the CSS row-header styles to a cell.
     * 
     * @param table
     *            The GWT table.
     * @param row
     *            The row index.
     * @param column
     *            The column index.
     */
    public static void applyRowHeaderStyles(HTMLTable table, int row, int column) {
        applyColumnHeaderStyles(table, row, column);
        table.getCellFormatter().addStyleName(row, column, "html-grid-header-row");
    }

    /**
     * Applies the CSS header styles to a GWT table.
     * 
     * @param table
     *            The GWT table.
     * @param applyToRows
     *            If the first column contains also headers (double entry
     *            array).
     */
    public static void applyHeaderStyles(HTMLTable table, boolean applyToRows) {

        // Rows.
        if (applyToRows) {
            for (int row = 0; row < table.getRowCount(); row++) {
                applyRowStyles(table, row);
                applyRowHeaderStyles(table, row, 0);
//                for (int column = 0; column < table.getCellCount(row); column++) {
//                    
//                }
            }
        }

        // Columns.
        for (int column = 0; column < table.getCellCount(0); column++) {
            applyColumnHeaderStyles(table, 0, column);
        }
    }

    /**
     * Applies the CSS row styles to a row.
     * 
     * @param table
     *            The GWT table.
     * @param row
     *            The row index.
     */
    public static void applyRowStyles(HTMLTable table, int row) {
        table.getRowFormatter().addStyleName(row, "x-grid3-hd-row");
        table.getRowFormatter().addStyleName(row, "x-grid3-row");
    }

    /**
     * Applies the CSS content style to a cell.
     * 
     * @param table
     *            The GWT table.
     * @param row
     *            The row index.
     * @param column
     *            The column index.
     * @param first
     *            If the cell is the first of its row.
     * @param last
     *            If the cell is the last of its row.
     */
    public static void applyCellStyles(HTMLTable table, int row, int column, boolean first, boolean last) {
        table.getCellFormatter().addStyleName(row, column, "x-grid3-col");
        table.getCellFormatter().addStyleName(row, column, "x-grid3-cell");
        table.getCellFormatter().addStyleName(row, column, "html-table-cell");

        if (first) {
            table.getCellFormatter().addStyleName(row, column, "x-grid3-cell-first");
        }

        if (last) {
            table.getCellFormatter().addStyleName(row, column, "x-grid3-cell-last");
            table.getCellFormatter().addStyleName(row, column, "html-table-cell-last");
        }

        final Widget w = table.getWidget(row, column);
        if (w != null) {
            w.addStyleName("x-grid3-cell-inner");
        }
    }
}
