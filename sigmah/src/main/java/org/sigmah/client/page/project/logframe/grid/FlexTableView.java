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

        // At the beginning, the shift count is equals to the row index.
        shift = row;

        // The first column is used to show the group of rows (rowspan).
        groupColumnIndex = 0;

        // The number of the others columns with can contains widgets.
        this.columnsCount = columnsCount - 1;
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

        // Applies the row styles.
        table.getFlexCellFormatter().setRowSpan(shift, groupColumnIndex,
                table.getFlexCellFormatter().getRowSpan(shift, groupColumnIndex) + 1);
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

        // Impacts the removing of row on dependencies.
        decrementDependencies();
    }

    // ------------------------------------------------------------------------
    // -- GROUPS
    // ------------------------------------------------------------------------

    /**
     * Builds and returns the widget of a group.
     * 
     * @param group
     *            The group.
     * @return The widget.
     */
    protected Widget buildGroupWidget(RowsGroup<?> group) {

        // Displays the group's label.
        final Label label = new Label(group.getTitle());
        return label;
    }

    /**
     * Adds a new group of rows at the last position.
     * 
     * @param group
     *            The group. Must not be <code>null</code>.
     * 
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the code is already used by another group.
     */
    public void addGroup(final RowsGroup<?> group) {
        insertGroup(groupsOrderedList.size() + 1, group);
    }

    /**
     * Inserts a new group of rows at the given position.
     * 
     * @param position
     *            The desired position. Must be included in the interval
     *            [1;GROUPS_COUNT].
     * @param group
     *            The group. Must not be <code>null</code>.
     * 
     * @throws NullPointerException
     *             If the group is <code>null</code>.
     * @throws IllegalArgumentException
     *             If the code is already used by another group.
     */
    public void insertGroup(int position, final RowsGroup<?> group) {

        // Checks if the group is valid.
        if (group == null) {
            throw new NullPointerException("group must not be null");
        }

        final int code = group.getId();
        // Checks if the group doesn't exist already.
        if (groupsCodesMap.get(code) != null) {
            throw new IllegalArgumentException("a group with code '" + code + "' already exist");
        }

        // Re-adjusts the position to avoid out of bounds errors.
        if (position <= 0 || groupsOrderedList.isEmpty()) {
            position = 1;
        } else if (position > groupsOrderedList.size()) {
            position = groupsOrderedList.size() + 1;
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[addGroupAtIndex] Inserts a new group at position # " + position + " : " + group);
        }

        // Computes new group indexes.
        int row = computeGroupIndex(position);
        row = insertTableRow(row);
        int column = 0;

        // Builds group's widget.
        final Widget widget = buildGroupWidget(group);

        // Adds widget and sets row.
        table.setWidget(row, column, widget);
        table.getFlexCellFormatter().setColSpan(row, column, columnsCount + 2);
        HTMLTableUtils.applyCellStyles(table, row, column, false, true);
        table.getFlexCellFormatter().addStyleName(row, column, CSS_GROUP_CELL_STYLE_NAME);
        widget.addStyleName(CSS_GROUP_LABEL_STYLE_NAME);

        // Adds the group locally at the correct position.
        groupsOrderedList.add(position - 1, group);
        groupsCodesMap.put(group.getId(), group);

        if (Log.isDebugEnabled()) {
            Log.debug("[addGroupAtIndex] Groups list: " + groupsOrderedList);
            Log.debug("[addGroupAtIndex] Groups map: " + groupsOrderedList);
        }

        // Hide the group header if needed.
        if (!group.isVisible()) {
            widget.setVisible(false);
        }
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
     *            The index at which the group will be inserted among the groups
     *            number (for example, a index equals to <code>2</code> means
     *            that the group will be the second one).<br/>
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
            index += 1 + groupsCodesMap.get(group.getId()).getRowsCount();
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[computeGroupIndex] The group computed row index is #" + index + ".");
        }

        return index;
    }

    /**
     * Gets a group position. The group must be added in the local groups list
     * before asking its position.
     * 
     * @param group
     *            The group.
     * @return The group position.
     */
    protected int getGroupPosition(RowsGroup<?> group) {

        // Default index (no group already displayed).
        int position = 0;

        // Browses the list of existing groups until the searched group is
        // reached.
        for (final RowsGroup<?> g : groupsOrderedList) {

            position++;

            if (g.equals(group)) {

                if (Log.isDebugEnabled()) {
                    Log.debug("[getGroupPosition] Found the position #" + position + " for the group: '"
                            + group.getId() + "'.");
                }

                return position;
            }
        }

        // The group hasn't been found.
        throw new IllegalArgumentException("the group with code '" + group.getId() + "' isn't already displayed");
    }

    // ------------------------------------------------------------------------
    // -- ROWS (IN GROUP)
    // ------------------------------------------------------------------------

    /**
     * Inserts a row in the given group at the last position.
     * 
     * @param <T>
     *            The type of the object displayed by the row.
     * @param groupId
     *            The group id.
     * @param row
     *            The row.
     */
    public <T> void addRow(final int groupId, final Row<T> row) {

        // By default the row is inserted at the end of the group.
        insertRow(Integer.MAX_VALUE, groupId, row);
    }

    /**
     * Inserts a row in the given group at the given position.
     * 
     * @param <T>
     *            The type of the object displayed by the row.
     * @param position
     *            The row position in its group.
     * @param groupId
     *            The group id.
     * @param row
     *            The row.
     */
    @SuppressWarnings("unchecked")
    public <T> void insertRow(int position, final int groupId, final Row<T> row) {

        // Checks if the group code is valid.
        final RowsGroup<?> group;
        if ((group = groupsCodesMap.get(groupId)) == null) {
            throw new IllegalArgumentException("group with code '" + groupId + "' does'nt exist");
        }

        // Checks if the row is valid.
        if (row == null) {
            throw new NullPointerException("row must not be null");
        }

        // Re-adjusts the position to avoid out of bounds errors.
        if (position <= 0 || group.getRowsCount() == 0) {
            position = 1;
        } else if (position > group.getRowsCount()) {
            position = group.getRowsCount() + 1;
        }

        if (Log.isDebugEnabled()) {
            Log.debug("[insertRow] Inserts a new row in group #" + groupId + " at position #" + position + " : " + row);
        }

        // Computes new row indexes.
        int rowIndex = computeRowIndex(group, position);
        rowIndex = insertTableRow(rowIndex);

        if (Log.isDebugEnabled()) {
            Log.debug("[insertRow] Inserts a new row in group # " + groupId + " : " + row);
        }

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
                final Row<?> lastRow;
                if ((lastRow = group.getLastRow()) != null) {

                    // If the rows properties are similar, removes the widget.
                    if (row.isSimilar(j, row.getUserObject(), ((Row<T>) lastRow).getUserObject())) {
                        table.setWidget(rowIndex, column, new Label(""));
                        table.getFlexCellFormatter().addStyleName(rowIndex, column, CSS_MERGED_ROWS_STYLE_NAME);
                    }
                }
            }

            column++;
        }

        // Adds the row locally.
        group.addRow(row);
    }

    /**
     * Gets the index at which a row must be inserted to be contained in the
     * given group at the given position. This index <strong>does'nt</strong>
     * consider the shift.<br/>
     * Use the {@link FlexTableView#insertTableRow(int)} method to insert a row
     * considering the shift.
     * 
     * @param group
     *            The group at which the row belongs.
     * @param position
     *            The position of the row in this group.
     * @see FlexTableView#insertTableRow(int)
     */
    protected int computeRowIndex(RowsGroup<?> group, int position) {

        // Computes group row index.
        final int groupPosition = getGroupPosition(group);
        final int groupRow = computeGroupIndex(groupPosition);

        // The row position is added to the group row index.
        return groupRow + position;
    }

    /**
     * Removes a row from the given group.
     * 
     * @param groupId
     *            The row id.
     * @param rowId
     *            The row id.
     */
    public void removeRow(final int groupId, final int rowId) {

        // Checks if the group exists.
        final RowsGroup<?> group;
        if ((group = groupsCodesMap.get(groupId)) == null) {
            throw new IllegalArgumentException("group with id #" + groupId + " does'nt exist");
        }

        // Checks if the row exists in this group.
        final Row<?> row;
        if ((row = group.getRow(rowId)) == null) {
            throw new IllegalArgumentException("row with id #" + rowId + " does'nt exist in group #" + groupId + "");
        }

        // Gets the row index.
        // TODO search correct index
        final int index = 0;

        // Removes the row in the table.
        removeTableRow(index);

        // Removes the row locally.
        group.removeRow(row);
    }

    /**
     * Moves a row inside its group.
     * 
     * @param groupId
     *            The group id.
     * @param rowId
     *            The id of the row to move.
     * @param move
     *            The number of moves to execute. If this count is higher than
     *            the available moves inside the row's group, the excess is
     *            ignored. <br/>
     *            A null integer has no effect. <br/>
     *            A positive integer will move the row upward.<br/>
     *            A negative integer will move the row downward.
     */
    @SuppressWarnings("unused")
    public void moveRow(final int groupId, final int rowId, final int move) {

        // Nothing to do.
        if (move == 0) {
            return;
        }

        // Checks if the group exists.
        final RowsGroup<?> group;
        if ((group = groupsCodesMap.get(groupId)) == null) {
            throw new IllegalArgumentException("group with id #" + groupId + " does'nt exist");
        }

        // Checks if the row exists in this group.
        final Row<?> row;
        if ((row = group.getRow(rowId)) == null) {
            throw new IllegalArgumentException("row with id #" + rowId + " does'nt exist in group #" + groupId + "");
        }

        // TODO move row
    }
}
