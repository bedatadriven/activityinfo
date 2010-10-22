package org.sigmah.client.page.project.logframe.grid;

import com.google.gwt.user.client.ui.Widget;

/**
 * Represents a row.
 * 
 * @param <T>
 *            The type of the user object displayed by this row.
 * @author tmi
 * 
 */
public abstract class Row<T> {

    /**
     * The user object.
     */
    private final T userObject;

    /**
     * The group in which this row is inserted.
     */
    private RowsGroup<?> parent;

    /**
     * Initializes the row.
     * 
     * @param userObject
     *            The user object.
     */
    public Row(T userObject) {
        this.userObject = userObject;
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
     * Gets the parent group.
     * 
     * @return The parent group.
     */
    public RowsGroup<?> getParent() {
        return parent;
    }

    /**
     * Sets the parent group.
     * 
     * @param parent
     *            The new parent group.
     */
    public void setParent(RowsGroup<?> parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Row)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        final Row<T> other = (Row<T>) obj;
        return getId() == other.getId();
    }

    @Override
    public String toString() {
        return "[Row] code #" + String.valueOf(getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Gets the unique code which identifies the row for the entire
     * {@link FlexTableView}.
     * 
     * @return The unique code to identify the row.
     */
    public int getId() {
        return getId(userObject);
    }

    /**
     * Gets the unique code which identifies the row for the entire
     * {@link FlexTableView}.
     * 
     * @param userObject
     *            The user object.
     * @return The unique code to identify the row.
     */
    public abstract int getId(T userObject);

    /**
     * Gets the widget which must be displayed at the given column index.<br/>
     * <br/>
     * If there is no widget to display at this index, the method must return
     * <code>null</code>. In this case, the right-adjacent column will be merged
     * with this column.
     * 
     * @param column
     *            The column index.
     * @return The widget if any, <code>null</code> otherwise.
     */
    public Widget getWidgetAt(int column) {
        return getWidgetAt(column, userObject);
    }

    /**
     * Gets the widget which must be displayed at the given column index.<br/>
     * <br/>
     * If there is no widget to display at this index, the method must return
     * <code>null</code>. In this case, the right-adjacent column will be merged
     * with this column.
     * 
     * @param column
     *            The column index.
     * @param userObject
     *            The user object.
     * @return The widget if any, <code>null</code> otherwise.
     */
    public abstract Widget getWidgetAt(int column, T userObject);

    /**
     * Returns if the property displayed at the given column index for this row
     * is similar (or equals) at the same property for the <code>other</code>
     * row. <br/>
     * By default this method returns false.
     * 
     * @param column
     *            The column index.
     * @param userObject
     *            The user object.
     * @param other
     *            The user object to compare (never <code>null</code>).
     * @return If the two rows properties are similar.
     */
    public boolean isSimilar(int column, T userObject, T other) {
        return false;
    }
}