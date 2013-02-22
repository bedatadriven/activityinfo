package org.activityinfo.shared.report.model.typeadapter;

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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.CategoryProperties;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

/**
 * @author Alex Bertram
 */
public class DimensionAdapter extends
    XmlAdapter<DimensionAdapter.DimensionElement, Dimension> {

    public static class CategoryElement {
        @XmlAttribute(required = true)
        private String name;

        @XmlAttribute
        private String label;

        @XmlAttribute
        private String color;
    }

    public static class DimensionElement {

        @XmlAttribute
        private String type;

        @XmlAttribute
        private Integer levelId;

        @XmlAttribute
        private String dateUnit;

        @XmlAttribute
        private Integer attributeGroupId;

        @XmlElement(name = "category")
        private List<CategoryElement> categories = new ArrayList<CategoryElement>(
            0);
    }

    private Dimension createDim(DimensionElement element) {
        if ("admin".equals(element.type)) {
            return new AdminDimension(element.levelId);
        } else if ("date".equals(element.type)) {
            return new DateDimension(findEnumValue(DateUnit.values(),
                element.dateUnit));
        } else if ("attribute".equals(element.type)) {
            return new AttributeGroupDimension(element.attributeGroupId);
        } else {
            return new Dimension(findEnumValue(DimensionType.values(),
                element.type));
        }
    }

    private <T extends Enum<T>> T findEnumValue(T[] values, String text) {
        for (T value : values) {
            if (value.toString().equalsIgnoreCase(text)) {
                return value;
            }
        }
        throw new IllegalArgumentException("'" + text + "' is not a member of "
            + values[0].getClass().getName());
    }

    @Override
    public Dimension unmarshal(DimensionElement element) {
        Dimension dim = createDim(element);

        for (CategoryElement category : element.categories) {
            CategoryProperties props = new CategoryProperties();
            props.setLabel(category.label);
            if (category.color != null) {
                props.setColor(decodeColor(category.color));
            }
            EntityCategory entityCategory = new EntityCategory(
                Integer.parseInt(category.name));
            dim.getCategories().put(entityCategory, props);
            dim.getOrdering().add(entityCategory);
        }

        return dim;
    }

    private int decodeColor(String color) {
        if (color.startsWith("#")) {
            return Integer.parseInt(color.substring(1), 16);
        } else {
            return Integer.parseInt(color, 16);
        }
    }

    @Override
    public DimensionElement marshal(Dimension dim) {
        DimensionElement element = new DimensionElement();
        element.type = dim.getType().toString();
        if (dim instanceof AdminDimension) {
            element.type = "admin";
            element.levelId = ((AdminDimension) dim).getLevelId();
        } else if (dim instanceof DateDimension) {
            element.type = "date";
            element.dateUnit = ((DateDimension) dim).getUnit().toString()
                .toLowerCase();
        } else if (dim instanceof AttributeGroupDimension) {
            element.type = "attribute";
            element.attributeGroupId = ((AttributeGroupDimension) dim)
                .getAttributeGroupId();
        }
        return element;
    }
}
