/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

/**
 * DimensionCategory class for all categories of Dimensions 
 * that are defined by database entities, like partner, activity,
 * indicator, etc.
 */
public class EntityCategory implements DimensionCategory {

    private int id;
    private String label;
    private Integer sortOrder;

    /**
     * Required for GWT serialization
     */
    protected EntityCategory() {

    }

    public EntityCategory(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public EntityCategory(int id) {
        this.id = id;
    }

    public EntityCategory(int id, String label, int sortOrder) {
        this.id = id;
        this.label = label;
        this.sortOrder = sortOrder;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Comparable getSortKey() {
        // sort by our label if we don't have an explicit sort order
        return sortOrder == null ? label : sortOrder;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntityCategory that = (EntityCategory) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        return result;
    }

    @Override
    public String toString() {
        return "EntityCategory{" + id +  " '" + label + "'}";
    }
}

