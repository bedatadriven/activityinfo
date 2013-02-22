package org.activityinfo.shared.report.content;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

/**
 * DimensionCategory class for all categories of Dimensions that are defined by
 * database entities, like partner, activity, indicator, etc.
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
        return "EntityCategory{" + id + " '" + label + "'}";
    }
}
