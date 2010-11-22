package org.sigmah.client.page.project.logframe.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Maintains a group of rows in a flex table. The group of rows is shown with a
 * rowspan on the first column of the table.
 * 
 * @author tmi
 * 
 */
public class FlexTableView {

    /**
     * Listen to view events.
     * 
     * @author tmi
     */
    public static interface FlexTableViewListener {

        /**
         * Method called when a group is added to this view.
         * 
         * @param group
         *            The new group.
         */
        public void groupAdded(RowsGroup<?> group);

        /**
         * Method called when a row is added to a group.
         * 
         * @param group
         *            The group.
         * @param row
         *            The new row.
         */
        public void rowAdded(RowsGroup<?> group, Row<?> row);

        /**
         * Method called when a row is removed from a group.
         * 
         * @param group
         *            The group.
         * @param row
         *            The old row.
         */
        public void rowRemoved(RowsGroup<?> group, Row<?> row);
    }

    /**
     * CSS style name for the entire view.
     */
    private static final String CSS_FLEX_TABLE_VIEW_STYLE_NAME = "flextable-view";

    /**
     * CSS style name for the cells which display groups.
     */
    private static final String CSS_GROUP_CELL_STYLE_NAME = CSS_FLEX_TABLE_VIEW_STYLE_NAME + "-group-cell";

    /**
     * CSS style name for the labels which display groups.
     */
    private static final String CSS_GROUP_LABEL_STYLE_NAME = CSS_FLEX_TABLE_VIEW_STYLE_NAME + "-group-label";

    /**
     * CSS style name for the merged rows.
     */
    private static final String CSS_MERGED_ROWS_STYLE_NAME = CSS_FLEX_TABLE_VIEW_STYLE_NAME + "-merged-row";

    /**
     * The parent flex table.
     */
    private final FlexTable table;

    /**
     * The other views (lower in the same table) which depend on this view. When
     * this view adds or removes rows, its dependent views will be incremented
     * or decremented to keep a consistent index.
     */
    private final ArrayList<FlexTableView> dependencies;

    /**
     * The difference between the first row index of this view and the first row
     * index of the entire table. This value should be incremented by others
     * views if there are displayed above.
     */
    private int shift;

    /**
     * The column to show the group of rows.
     */
    private final int groupColumnIndex;

    /**
     * The number of fillable columns.
     */
    private final int columnsCount;

    /**
     * An ordered list of the current displayed groups.
     */
    private final ArrayList<RowsGroup<?>> groupsOrderedList;

    /**
     * The key of this map is the unique integer which identifies a group of
     * rows.
     */
    private final HashMap<Integer, RowsGroup<?>> groupsCodesMap;

    /**
     * Listeners.
     */
    private final ArrayList<FlexTableViewListener> listeners;

    /**
     * Initializes this group of rows.
     * 
     * @param table
     *            The parent flex table.
     * @param row
     *            The row index of this group of rows.
     * @param enableParentsCodes
     *            If the parent codes must be displayed.
     */
    public FlexTableView(FlexTable table, int columnsCount, int row) {

        // Checks if the table isn't null.
        if (table == null) {
            throw new NullPointerException("table must not be null");
        }

        // Checks if the row indexes an existing row.
        if (row < 0 || row >= table.getRowCount()) {
            throw new IllegalArgumentException("the flex table does not have a row at index #" + row + ".");
        }

        // Checks if the table contains enough columns.
        if (columnsCount < 2) {
            throw new IllegalArgumentException("the flex table does not contains enought columns (min 2).");
        }

        // Sets the table.
        this.table = table;

        // Initializes the local lists.
        groupsOrderedList = new ArrayList<RowsGroup<?>>();
        groupsCodesMap = new HashMap<Integer, RowsGroup<?>>();
        dependencies = new ArrayList<FlexTableView>();
        listeners = new ArrayList<FlexTableViewListener>();

        // At the beginning, the shift count is equals to the row index.
        shift = row;

        // The first column is used to show the group of rows (rowspan).
        groupColumnIndex = 0;

        // The number of the others columns with can contains widgets.
        this.columnsCount = columnsCount - 1;
    }

    // ------------------------------------------------------------------------
    // -- LISTENERS
    // ------------------------------------------------------------------------

    /**
     * Adds a listener.
     * 
     * @param l
     *            The new listener.
     */
    public void addFlexTableViewListener(FlexTableViewListener l) {
        this.listeners.add(l);
    }

    /**
     * Removes a listener.
     * 
     * @param l
     *            The old listener.
     */
    public void removeFlexTableViewListener(FlexTableViewListener l) {
        this.listeners.remove(l);
    }

    /**
     * Method called when a group is added to this view.
     * 
     * @param group
     *            The new group.
     */
    protected void fireGroupAdded(final RowsGroup<?> group) {
        for (final FlexTableViewListener l : listeners) {
            l.groupAdded(group);
        }
    }

    /**
     * Method called when a row is added to a group.
     * 
     * @param group
     *            The group.
     * @param row
     *            The new row.
     */
    protected void fireRowAdded(RowsGroup<?> group, Row<?> row) {
        for (final FlexTableViewListener l : listeners) {
            l.rowAdded(group, row);
        }
    }

    /**
     * Method called when a row is removed from a group.
     * 
     * @param group
     *            The group.
     * @param row
     *            The old row.
     */
    protected void fireRowRemoved(RowsGroup<?> group, Row<?> row) {
        for (final FlexTableViewListener l : listeners) {
            l.rowRemoved(group, row);
        }
    }

    // ------------------------------------------------------------------------
    // -- DEPENDENCIES
    // ------------------------------------------------------------------------

    /**
     * Adds a dependency to this view.
     * 
     * @param other
     *            The other view.
     */
    public void addDependency(FlexTableView other) {

        // Checks if the other view is correct.
        if (other == null) {
            throw new NullPointerException("other must not be null");
        }

        // Checks if the two views share the same table.
        if (table != other.table) {
            throw new IllegalArgumentException("the other view doesn't share the same table as the current view");
        }

        dependencies.add(other);
    }

    /**
     * Increments the shift count of each dependency.
     */
    private void incrementDependencies() {

        for (final FlexTableView dependency : dependencies) {
            dependency.shift++;
        }
    }

    /**
     * Decrements the shift count of each dependency.
     */
    private void decrementDependencies() {

        for (final FlexTableView dependency : dependencies) {
            dependency.shift--;
        }
    }

    // ------------------------------------------------------------------------
    // -- FLEX TABLE
    // ------------------------------------------------------------------------

    /**
     * Inserts a row in the table. This method considers the shift.
     * 
     * @param beforeRow
     *            The index before which the new row will be inserted.
     * @return The new row index.
     */
    protected int insertTableRow(int beforeRow) {

        // Inserts the new row.
        int row = table.insertRow(beforeRow + shift);

        // Adjusts the row span.
        table.getFlexCellFormatter().setRowSpan(shift, groupColumnIndex,
                table.getFlexCellFormatter().getRowSpan(shift, groupColumnIndex) + 1);

        // Applies the row styles.
        HTMLTableUtils.applyRowStyles(table, row);

        // Impacts the adding of row on dependencies.
        incrementDependencies();

        return row;
    }

    /**
     * Removes a row from the table. This method considers the shift.
     * 
     * @param row
     *            The index of the row to remove.
     */
    protected void removeTableRow(int row) {

        // Removes the row.
        table.removeRow(row + shift);

        // Adjusts the row span.
        table.getFlexCellFormatter().setRowSpan(shift, groupColumnIndex,
                table.getFlexCellFormatter().getRowSpan(shift, groupColumnIndex) - 1);

        // Impacts the removing of row on dependencies.
        decrementDependencies();
    }

    // ------------------------------------------------------------------------
    // -- GROUPS
    // ------------------------------------------------------------------------

    /**
     * Inserts a new group of rows at the last position.
     * 
     * @param group
     *            The group.
     * 
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If a group with the same id already exists.
     */
    public void addGroup(final RowsGroup<?> group) {
        insertGroup(groupsOrderedList.size() + 1, group);
    }

    /**
     * Inserts a new group of rows at the given position.
     * 
     * @param position
     *            The position at which the group will be inserted among the
     *            groups list (for example, a index equals to <code>2</code>
     *            means that the group will be the second one).<br/>
     *            If this index is lower or equal than <code>0</code>, the group
     *            will be the first one. An index greater than the number of
     *            group will insert the group at the last position.
     * 
     * @param group
     *            The group.
     * 
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If a group with the same id already exists.
     */
    public void insertGroup(int position, final RowsGroup<?> group) {

        // Checks if the group is valid.
        if (group == null) {
            throw new NullPointerException("The group must not be null.");
        }

        final int id = group.getId();
        // Checks if the group doesn't exist already.
        if (groupsCodesMap.get(id) != null) {
            throw new IllegalArgumentException("The group with id #" + id + " already exists.");
        }

        // Re-adjusts the position to avoid out of bounds errors.
        if (position <= 0 || groupsOrderedList.isEmpty()) {
            position = 1;
        } else if (position > groupsOrderedList.size()) {
            position = groupsOrderedList.size() + 1;
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[insertGroup] Inserts the new group #" + id + " at position # " + position + ".");
        }

        // Computes new group indexes.
        int row = computeGroupIndex(position);
        row = insertTableRow(row);
        int column = 0;

        // Builds group's widget.
        final Widget widget = group.getWidget();

        // Adds widget and sets row.
        table.setWidget(row, column, widget);
        table.getFlexCellFormatter().setColSpan(row, column, columnsCount + 2);
        HTMLTableUtils.applyCellStyles(table, row, column, false, true);
        table.getFlexCellFormatter().addStyleName(row, column, CSS_GROUP_CELL_STYLE_NAME);
        widget.addStyleName(CSS_GROUP_LABEL_STYLE_NAME);

        // Adds the group locally at the correct position.
        groupsOrderedList.add(position - 1, group);
        groupsCodesMap.put(group.getId(), group);

        // Hides the group header if needed.
        if (!group.isVisible()) {
            widget.setVisible(false);
        }

        fireGroupAdded(group);
    }

    /**
     * Gets the group with the given id if it exists. Returns <code>null</code>
     * otherwise.
     * 
     * @param groupId
     *            The group id.
     * @return The group with this id, <code>null</code> otherwise.
     */
    public RowsGroup<?> getGroup(int groupId) {
        return groupsCodesMap.get(groupId);
    }

    /**
     * Gets the index at which a group must be inserted to be at the given
     * position. This index <strong>does'nt</strong> consider the shift.<br/>
     * Use the {@link FlexTableView#insertTableRow(int)} method to insert a row
     * considering the shift.
     * 
     * @param position
     *            The position at which the group will be inserted among the
     *            groups list (for example, a index equals to <code>2</code>
     *            means that the group will be the second one).<br/>
     *            If this index is lower or equal than <code>0</code>, the group
     *            will be the first one. An index greater than the number of
     *            group will insert the group at the last position.
     * 
     * @see FlexTableView#insertTableRow(int)
     */
    protected int computeGroupIndex(int position) {

        // Default index (no group already displayed).
        int index = 1;

        // Browses the list of existing groups until the desired position is
        // reached.
        RowsGroup<?> group;
        for (int i = 0; i < position - 1 && i < groupsOrderedList.size(); i++) {

            // For each group, increments the index with the number of elements
            // it contains.
            group = groupsOrderedList.get(i);
            index += 1 + group.getRowsCount();
        }

        return index;
    }

    /**
     * Gets the position of a group. The position is included in the interval
     * [1;GROUPS_COUNT]. If the group doesn't exist, an exception is thrown.
     * 
     * @param group
     *            The group.
     * @return The group position in the interval [1;GROUPS_COUNT].
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the group doesn't exist.
     */
    protected int getGroupPosition(final RowsGroup<?> group) {

        // Checks if the group is valid.
        if (group == null) {
            throw new NullPointerException("The group must not be null.");
        }

        // Gets the group index in the ordered list.
        final int position = groupsOrderedList.indexOf(group);

        // The group doesn't exist.
        if (position == -1) {
            throw new IllegalArgumentException("The group with id #" + group.getId() + " doesn't exist.");
        }

        return position + 1;
    }

    /**
     * Gets the row index of a group. If the group doesn't exist, an exception
     * is thrown.
     * 
     * @param group
     *            The group.
     * @return The row index of this group.
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the group doesn't exist.
     */
    protected int getGroupRowIndex(final RowsGroup<?> group) {

        // Checks if the group is valid.
        if (group == null) {
            throw new NullPointerException("The group must not be null.");
        }

        // Default index (no group already displayed).
        int index = 0;

        // Browses the list of existing groups until the searched group is
        // reached.
        for (final RowsGroup<?> g : groupsOrderedList) {

            index++;

            if (g.equals(group)) {
                return index;
            }

            // For each group, increments the index with the number of elements
            // it contains.
            index += g.getRowsCount();
        }

        // The group hasn't been found.
        throw new IllegalArgumentException("The group with id #" + group.getId() + " doesn't exist.");
    }

    /**
     * Gets the number of groups in this view.
     * 
     * @return The number of groups in this view.
     */
    public int getGroupsCount() {
        return groupsOrderedList.size();
    }

    /**
     * Refreshes the group widget.
     * 
     * @param group
     *            The group.
     */
    public void refreshGroupWidget(final RowsGroup<?> group) {

        // Checks if the group is valid.
        if (group == null) {
            throw new NullPointerException("The group must not be null.");
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[refreshGroupWidget] Refreshes the group #" + group.getId() + ".");
        }

        // Computes new group indexes.
        int row = getGroupRowIndex(group) + shift;
        int column = 0;

        // Builds group's widget.
        final Widget widget = group.getWidget();

        // Sets the new widget.
        table.setWidget(row, column, widget);

        // Applies style names.
        HTMLTableUtils.applyCellStyles(table, row, column, false, true);
        widget.addStyleName(CSS_GROUP_LABEL_STYLE_NAME);
    }

    // ------------------------------------------------------------------------
    // -- ROWS (IN GROUP)
    // ------------------------------------------------------------------------

    /**
     * Inserts a row in the given group at the last position.
     * 
     * @param <T>
     *            The type of the user object contained in this row.
     * @param groupId
     *            The id of the group in which the row will be inserted.
     * @param row
     *            The row.
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     * @throws IllegalArgumentException
     *             If there isn't a group with the given id.
     */
    public <T> void addRow(final int groupId, final Row<T> row) {

        // By default the row is inserted at the end of the group.
        // (sets the position to the infinite value to avoid group searching
        // which is done by the sub method).
        insertRow(Integer.MAX_VALUE, groupId, row);
    }

    /**
     * Inserts a row in the given group at the given position.
     * 
     * @param <T>
     *            The type of the user object contained in this row.
     * @param position
     *            The row position in its group (for example, a index equals to
     *            <code>2</code> means that the row will be the second one in
     *            its group).<br/>
     *            If this index is lower or equal than <code>0</code>, the row
     *            will be the first one. An index greater than the number of
     *            rows in this group will insert the row at the last position.
     * @param groupId
     *            The id of the group in which the row will be inserted.
     * @param row
     *            The row.
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     * @throws IllegalArgumentException
     *             If there isn't a group with the given id.
     */
    @SuppressWarnings("unchecked")
    public <T> void insertRow(int position, final int groupId, final Row<T> row) {

        // Checks if the row is valid.
        if (row == null) {
            throw new NullPointerException("The row must not be null.");
        }

        // Checks if the group code is valid.
        final RowsGroup<?> group;
        if ((group = groupsCodesMap.get(groupId)) == null) {
            throw new IllegalArgumentException("The group #" + groupId + " does'nt exist.");
        }

        // Re-adjusts the position to avoid out of bounds errors.
        if (position <= 0 || group.getRowsCount() == 0) {
            position = 1;
        } else if (position > group.getRowsCount()) {
            position = group.getRowsCount() + 1;
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[insertRow] Inserts the new row #" + row.getId() + " in group #" + groupId + " at position #"
                    + position + ".");
        }

        // Computes new row indexes.
        int rowIndex = computeRowIndex(group, position);
        rowIndex = insertTableRow(rowIndex);

        // Indexes of the columns which manage merging.
        final List<Integer> merge = new ArrayList<Integer>();
        for (int index : group.getMergedColumnIndexes()) {
            merge.add(index);
        }

        int column = 0;
        int colSpan = 0;
        // Adds each column widget.
        for (int j = 0; j < columnsCount; j++) {

            // Gets the widget at this column index.
            final Widget w = row.getWidgetAt(j);

            if (w == null) {
                colSpan++;
                column--;
            } else {

                table.setWidget(rowIndex, column, w);

                // If there is any col span to perform.
                if (colSpan != 0) {
                    table.getFlexCellFormatter().setColSpan(rowIndex, column, colSpan + 1);
                }

                HTMLTableUtils.applyCellStyles(table, rowIndex, column, false, false);

                // Reinit the col span.
                colSpan = 0;
            }

            // Checks if this column can be merged.
            if (merge.contains(j)) {

                // Gets the top row if any.
                final Row<?> topRow;
                if ((topRow = group.getRowAtPosition(position - 1)) != null) {

                    // If the rows properties are similar, removes the widget.
                    if (row.isSimilar(j, row.getUserObject(), ((Row<T>) topRow).getUserObject())) {
                        table.setWidget(rowIndex, column, new Label(""));
                        table.getFlexCellFormatter().addStyleName(rowIndex, column, CSS_MERGED_ROWS_STYLE_NAME);
                    }
                }
            }

            column++;
        }

        // Adds the row locally.
        group.addRow(row, position);

        try {
            // Refreshes the direct bottom row of the just inserted row.
            refreshMergedRow(group, position + 1);
        }
        // The row doesn't exist, nothing to do.
        catch (IndexOutOfBoundsException e) {
            // Digests exception.
        }

        fireRowAdded(group, row);
    }

    /**
     * Removes the given row from the given group.
     * 
     * @param group
     *            The group in which the row is inserted.
     * @param rowId
     *            The row id.
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the row doesn't exist.
     */
    public void removeRow(final RowsGroup<?> group, final int rowId) {

        // Checks if the group is valid.
        if (group == null) {
            throw new IllegalArgumentException("The group must not be null.");
        }

        // Checks if the row exists in this group.
        final Row<?> row;
        if ((row = group.getRow(rowId)) == null) {
            throw new IllegalArgumentException("The row with id #" + rowId + " does'nt exist in group #"
                    + group.getId() + ".");
        }

        // Saves the old position of the removed row.
        final int oldPosition = group.getRowPosition(row);

        // Gets the row index.
        final int index = getRowIndex(row);

        // Removes the row in the table.
        removeTableRow(index);

        // Removes the row locally.
        group.removeRow(row);

        try {
            // Refreshes the direct bottom row of the just removed row.
            refreshMergedRow(group, oldPosition);
        }
        // The row doesn't exist, nothing to do.
        catch (IndexOutOfBoundsException e) {
            // Digests exception.
        }

        fireRowRemoved(group, row);
    }

    /**
     * Refreshes a row styles and widgets considering the merged columns
     * indexes.
     * 
     * @param group
     *            The group in which the row is inserted.
     * @param position
     *            The position of the row to refresh.
     * @throws IndexOutOfBoundsException
     *             If there is no row at the given position.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void refreshMergedRow(RowsGroup<?> group, int position) throws IndexOutOfBoundsException {

        // If the row was the last one, nothing to do.
        if (group.getRowsCount() == 0) {
            return;
        }

        // Gets the row to refresh.
        final Row row = group.getRowAtPosition(position);

        // Computes this row index.
        int rowIndex = computeRowIndex(group, position) + shift;

        // Indexes of the columns which manage merging.
        final List<Integer> merge = new ArrayList<Integer>();
        for (int index : group.getMergedColumnIndexes()) {
            merge.add(index);
        }

        int column = 0;
        // Adds each column widget.
        for (int j = 0; j < columnsCount; j++) {

            // Gets the widget at this column index.
            final Widget w = row.getWidgetAt(j);

            if (w == null) {
                column--;
            }

            // Checks if this column can be merged.
            if (merge.contains(j)) {

                // Gets the top row if any.
                final Row topRow;
                if ((topRow = group.getRowAtPosition(position - 1)) != null) {

                    // If the rows properties are similar, removes the widget.
                    if (row.isSimilar(j, row.getUserObject(), topRow.getUserObject())) {
                        table.setWidget(rowIndex, column, new Label(""));
                        table.getFlexCellFormatter().addStyleName(rowIndex, column, CSS_MERGED_ROWS_STYLE_NAME);
                    } else {
                        table.setWidget(rowIndex, column, w);
                        HTMLTableUtils.applyCellStyles(table, rowIndex, column, false, false);
                        table.getFlexCellFormatter().removeStyleName(rowIndex, column, CSS_MERGED_ROWS_STYLE_NAME);
                    }
                } else {
                    table.setWidget(rowIndex, column, w);
                    HTMLTableUtils.applyCellStyles(table, rowIndex, column, false, false);
                    table.getFlexCellFormatter().removeStyleName(rowIndex, column, CSS_MERGED_ROWS_STYLE_NAME);
                }
            }

            column++;
        }
    }

    /**
     * Moves a row inside its group.
     * 
     * @param group
     *            The group in which the row is inserted.
     * @param rowId
     *            The id of the row to move.
     * @param move
     *            The number of moves to execute. If this count is higher than
     *            the available moves inside the row's group, the excess is
     *            ignored. <br/>
     *            A null integer has no effect. <br/>
     *            A positive integer will move the row upward.<br/>
     *            A negative integer will move the row downward.
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the row doesn't exist.
     */
    public void moveRow(final RowsGroup<?> group, final int rowId, int move) {

        // No move.
        if (move == 0) {
            return;
        }

        // Checks if the group is valid.
        if (group == null) {
            throw new IllegalArgumentException("The group must not be null.");
        }

        // The group contains no or only one row, nothing to do.
        final int rowsCount = group.getRowsCount();
        if (group.getRowsCount() <= 1) {
            return;
        }

        // Checks if the row exists in this group.
        final Row<?> row;
        if ((row = group.getRow(rowId)) == null) {
            throw new IllegalArgumentException("The row #" + rowId + " does'nt exist in group #" + group.getId() + ".");
        }

        // Gets the row position in its group.
        final int rowPosition = row.getParent().getRowPosition(row);

        // Checks if the row can be moved.
        if (move > 0) {

            // The row is already the first one, nothing to do.
            if (rowPosition == 1) {
                return;
            }
        } else {

            // The row is already the last one, nothing to do.
            if (rowPosition == rowsCount) {
                return;
            }
        }

        // Re-adjusts the moves count to avoid out of bounds errors.
        if (move > 0) {

            final int avalaibleMovesCount = rowPosition - 1;
            if (move > avalaibleMovesCount) {
                move = avalaibleMovesCount;
            }
        } else {

            final int avalaibleMovesCount = rowsCount - rowPosition;
            if (Math.abs(move) > avalaibleMovesCount) {
                move = -avalaibleMovesCount;
            }
        }

        // Removes the row.
        removeRow(group, rowId);

        // Re-inserts it at its new position.
        insertRow(rowPosition - move, group.getId(), row);
    }

    /**
     * Returns if a row can be moved for the given moves count.
     * 
     * @param group
     *            The group in which the row is inserted.
     * @param rowId
     *            The id of the row to move.
     * @param move
     *            The number of moves to execute. If this count is higher than
     *            the available moves inside the row's group, the excess is
     *            ignored. <br/>
     *            A null integer has no effect. <br/>
     *            A positive integer will move the row upward.<br/>
     *            A negative integer will move the row downward.
     * @return If the row can be moved for the given moves count.
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the row doesn't exist.
     */
    public boolean canBeMoved(final RowsGroup<?> group, final int rowId, int move) {

        // No move.
        if (move == 0) {
            return false;
        }

        // Checks if the group is valid.
        if (group == null) {
            throw new IllegalArgumentException("The group must not be null.");
        }

        // The group contains no or only one row, nothing to do.
        final int rowsCount = group.getRowsCount();
        if (group.getRowsCount() <= 1) {
            return false;
        }

        // Checks if the row exists in this group.
        final Row<?> row;
        if ((row = group.getRow(rowId)) == null) {
            throw new IllegalArgumentException("The row #" + rowId + " does'nt exist in group #" + group.getId() + ".");
        }

        // Gets the row position in its group.
        final int rowPosition = row.getParent().getRowPosition(row);

        // Checks if the row can be moved.
        if (move > 0) {

            // The row is already the first one, nothing to do.
            if (rowPosition == 1) {
                return false;
            }
        } else {

            // The row is already the last one, nothing to do.
            if (rowPosition == rowsCount) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the index at which a row must be inserted to be contained in the
     * given group at the given position. This index <strong>does'nt</strong>
     * consider the shift.<br/>
     * Use the {@link FlexTableView#insertTableRow(int)} method to insert a row
     * considering the shift.
     * 
     * @param group
     *            The group in which the row will be inserted.
     * @param position
     *            The row position in its group (for example, a index equals to
     *            <code>2</code> means that the row will be the second one in
     *            its group).<br/>
     *            If this index is lower or equal than <code>0</code>, the row
     *            will be the first one. An index greater than the number of
     *            rows in this group will insert the row at the last position.
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the group doesn't exist.
     * @see FlexTableView#insertTableRow(int)
     */
    protected int computeRowIndex(final RowsGroup<?> group, int position) {

        // Computes group row index.
        final int groupRowIndex = getGroupRowIndex(group);

        // Re-adjusts the position to avoid out of bounds errors.
        if (position <= 0 || group.getRowsCount() == 0) {
            position = 1;
        } else if (position > group.getRowsCount()) {
            position = group.getRowsCount() + 1;
        }

        // The row position is added to the group row index.
        return groupRowIndex + position;
    }

    /**
     * Gets the row index of a row.
     * 
     * @param row
     *            The row.
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     * @throws IllegalArgumentException
     *             If this row doesn't exist.
     */
    protected int getRowIndex(final Row<?> row) {

        // Checks if the row is valid.
        if (row == null) {
            throw new NullPointerException("The row must not be null.");
        }

        // Gets the row group.
        final RowsGroup<?> parent = row.getParent();

        // Computes group row index.
        final int groupRowIndex = getGroupRowIndex(parent);

        // Gets the row position.
        final int rowPosition = parent.getRowPosition(row);

        return groupRowIndex + rowPosition;
    }

    /**
     * Gets the number of rows in this view.
     * 
     * @return The number of rows in this view.
     */
    public int getRowsCount() {

        int count = 0;
        for (final RowsGroup<?> group : groupsOrderedList) {
            count += group.getRowsCount();
        }

        return count;
    }
}
