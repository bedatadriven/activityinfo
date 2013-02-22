package org.activityinfo.shared.report.model;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.typeadapter.DimensionAdapter;

/*
 * Olap dimension, mapping to rows or columns in pivot tables
 */
@XmlJavaTypeAdapter(DimensionAdapter.class)
public class Dimension implements Serializable {

    public static final Dimension TARGET = new Dimension(DimensionType.Target);

    private DimensionType type;
    private String color;

    private Map<DimensionCategory, CategoryProperties> categories =
        new HashMap<DimensionCategory, CategoryProperties>(0);

    private List<DimensionCategory> ordering = new ArrayList<DimensionCategory>();

    /**
     * Required for GWT serialization
     */
    protected Dimension() {
    }

    public Dimension(DimensionType type) {
        this.type = type;
    }

    public DimensionType getType() {
        return type;
    }

    private void setType(DimensionType type) {
        this.type = type;
    }

    /**
     * @return The <i>type</i> of order applied to this dimension
     */
    @XmlTransient
    public boolean isOrderDefined() {
        return ordering.size() != 0;
    }

    /**
     * @return The model-supplied (i.e. specified in the XML) category order of
     *         this dimension.
     */
    @XmlTransient
    public List<DimensionCategory> getOrdering() {
        return ordering;
    }

    private void setOrdering(List<DimensionCategory> ordering) {
        this.ordering = ordering;
    }

    /**
     * @param category
     * @return The model-supplied (i.e. specified in the XML) category label for
     *         a given category in this dimension
     */
    public String getLabel(DimensionCategory category) {
        CategoryProperties props = categories.get(category);
        return props == null ? null : props.getLabel();
    }

    @XmlTransient
    public Map<DimensionCategory, CategoryProperties> getCategories() {
        return categories;
    }

    public void setCategories(
        Map<DimensionCategory, CategoryProperties> categories) {
        this.categories = categories;
    }

    /**
     * Adds a model supplied label for a given category in this dimension
     * 
     * @param category
     * @param props
     */
    public void setProperties(DimensionCategory category,
        CategoryProperties props) {
        categories.put(category, props);
    }

    public void setProperties(int id, CategoryProperties props) {
        categories.put(new EntityCategory(id), props);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Dimension)) {
            return false;
        }

        Dimension that = (Dimension) other;

        return this.type == that.type;
    }

    @Override
    public int hashCode() {
        return this.type.hashCode();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCategoryColor(int id, int color) {
        EntityCategory cat = new EntityCategory(id);
        CategoryProperties props = categories.get(cat);
        if (props == null) {
            props = new CategoryProperties();
            categories.put(cat, props);
        }
        props.setColor(color);
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
