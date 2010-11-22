package org.sigmah.client.page.project.logframe.grid;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;

/**
 * Represents a group of rows currently displayed.
 * 
 * @param <T>
 *            The type of the user object displayed by this group.
 * @author tmi
 * 
 */
public abstract class RowsGroup<T> {

    /**
     * An ordered list of the rows of this group.
     */
    private final ArrayList<Row<?>> rowsOrderedList;

    /**
     * The key of this map is the unique integer which identifies a row in this
     * group.
     */
    private final HashMap<Integer, Row<?>> rowsIdsMap;

    /**
     * The user object.
     */
    private final T userObject;

    /**
     * Initializes this group.
     * 
     * @param userObject
     *            The user object.
     */
    public RowsGroup(T userObject) {
        rowsOrderedList = new ArrayList<Row<?>>();
        rowsIdsMap = new HashMap<Integer, Row<?>>();
        this.userObject = userObject;
    }

    /**
     * Update the position of each rows.
     */
    private void updatePositions() {

        for (int i = 0; i < rowsOrderedList.size(); i++) {
            final Row<?> row = rowsOrderedList.get(i);
            row.setPosition(i + 1);
        }
    }

    /**
     * Gets the user object.
     * 
     * @return The user object.
     */
    public T getUserObject() {
        return userObject;
    }

    /**
     * Gets the number of rows of this group.
     * 
     * @return The number of rows of this group.
     */
    public int getRowsCount() {
        return rowsOrderedList.size();
    }

    /**
     * Adds a row at the end of this group.
     * 
     * @param row
     *            The new row.
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     */
    protected void addRow(Row<?> row) {
        addRow(row, rowsOrderedList.size());
    }

    /**
     * Adds a row at the given position in this group.
     * 
     * @param row
     *            The new row.
     * @param position
     *            The row position in this group.
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     * @throws IndexOutOfBoundsException
     *             If the position ins't included in the interval
     *             [1;ROWS_COUNT].
     */
    protected void addRow(Row<?> row, int position) {

        // Checks if the row is not null.
        if (row == null) {
            throw new NullPointerException("The row must not be null.");
        }

        // Checks if the position is correct.
        if (position < 1 || position > getRowsCount() + 1) {
            throw new IndexOutOfBoundsException("The position #" + position + " doesn't exist.");
        }

        rowsOrderedList.add(position - 1, row);
        rowsIdsMap.put(row.getId(), row);

        row.setParent(this);
        updatePositions();
    }

    /**
     * Gets the last row of this group. If the group is empty, <code>null</code>
     * is returned.
     * 
     * @return The last row of the group, <code>null</code> otherwise.
     */
    protected Row<?> getLastRow() {
        if (rowsOrderedList.isEmpty()) {
            return null;
        } else {
            return rowsOrderedList.get(rowsOrderedList.size() - 1);
        }
    }

    /**
     * Gets the row at the given position. If the group is empty or if there
     * isn't a row at this position, <code>null</code> is returned.
     * 
     * @param position
     *            The row position in this group in the interval
     *            [1;GROUPS_COUNT].
     * @return The row at the given position, <code>null</code> otherwise.
     */
    protected Row<?> getRowAtPosition(int position) {
        if (rowsOrderedList.isEmpty()) {
            return null;
        } else {

            // This position doen't exist.
            if (position < 1 || position > rowsOrderedList.size() + 1) {
                return null;
            }

            return rowsOrderedList.get(position - 1);
        }
    }

    /**
     * Gets the row with the given id if it exists, <code>null</code> otherwise.
     * 
     * @param rowId
     *            The row id.
     * @return The row if it exists, <code>null</code> otherwise.
     */
    protected Row<?> getRow(int rowId) {
        return rowsIdsMap.get(rowId);
    }

    /**
     * Gets the row position in this group. The position is included in the
     * interval [1;ROWS_COUNT]. If the row doesn't exist, an exception is
     * thrown.
     * 
     * @param row
     *            The row.
     * @return The row position in the interval [1;GROUPS_COUNT].
     * @throws NullPointerException
     *             If the row is <code>null</code>.
     * @throws IllegalArgumentException
     *             If this row doesn't exist in this group.
     */
    protected int getRowPosition(final Row<?> row) {

        // Checks if the group is valid.
        if (row == null) {
            throw new NullPointerException("The row must not be null.");
        }

        // Gets the row index in the ordered list.
        final int position = rowsOrderedList.indexOf(row);

        // The row doesn't exist.
        if (position == -1) {
            throw new IllegalArgumentException("The row with id #" + row.getId() + " doesn't exist in this group.");
        }

        return position + 1;
    }

    /**
     * Removes a row from this group.
     * 
     * @param rowId
     *            The row id.
     * @return If the row has been removed.
     */
    protected boolean removeRow(int rowId) {

        // Gets the row.
        final Row<?> row = rowsIdsMap.get(rowId);

        // Removes it if it exists.
        return removeRow(row);
    }

    /**
     * Removes a row from this group.
     * 
     * @param row
     *            The row.
     * @return If the row has been removed.
     */
    protected boolean removeRow(final Row<?> row) {

        if (row != null) {

            // Removes it if it exists.
            if (rowsIdsMap.get(row.getId()) != null) {

                // Detaches the row from this group.
                row.setParent(null);
                row.setPosition(null);

                // Removes it locally.
                rowsOrderedList.remove(row);
                rowsIdsMap.remove(row.getId());
                updatePositions();

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof RowsGroup)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        final RowsGroup<T> other = (RowsGroup<T>) obj;
        return getId() == other.getId();
    }

    @Override
    public String toString() {
        return "[Group] code #" + String.valueOf(getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Gets the unique code which identifies the group.
     * 
     * @return The unique code to identify the group.
     */
    protected int getId() {
        return getId(userObject);
    }

    /**
     * Gets the unique code which identifies the group.
     * 
     * @param userObject
     *            The user object.
     * @return The unique code to identify the group.
     */
    public abstract int getId(T userObject);

    /**
     * Gets the group title.
     * 
     * @return The group title.
     */
    protected String getTitle() {
        return getTitle(userObject);
    }

    /**
     * Gets the group title.
     * 
     * @param userObject
     *            The user object.
     * @return The group title.
     */
    public abstract String getTitle(T userObject);

    /**
     * Builds and returns the widget of a group.
     * 
     * @return The widget.
     */
    public abstract Widget getWidget();

    /**
     * Gets the indexes of the columns which rows will be merged if their cells
     * contents are similar.
     * 
     * @return The indexes of the columns which rows can be merged.
     */
    protected int[] getMergedColumnIndexes() {
        return getMergedColumnIndexes(userObject);
    }

    /**
     * Gets the indexes of the columns which rows will be merged if their cells
     * contents are similar.
     * 
     * @param userObject
     *            The user object.
     * @return The indexes of the columns which rows can be merged.
     */
    public int[] getMergedColumnIndexes(T userObject) {
        return new int[0];
    }

    /**
     * Returns if the group header must be shown.
     * 
     * @return If the group header must be show.
     */
    protected boolean isVisible() {
        return isVisible(userObject);
    }

    /**
     * Returns if the group header must be shown.<br/>
     * Returns <code>true</code> by default.
     * 
     * @param userObject
     *            The user object.
     * @return If the group header must be show.
     */
    public boolean isVisible(T userObject) {
        return true;
    }
}