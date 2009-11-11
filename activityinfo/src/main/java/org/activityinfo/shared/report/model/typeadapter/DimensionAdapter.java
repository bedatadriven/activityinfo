/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.shared.report.model.typeadapter;

import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class DimensionAdapter extends XmlAdapter<DimensionAdapter.DimensionElement, Dimension>  {

    public static class CategoryElement {
        @XmlAttribute(required = true)
        public String name;

        @XmlAttribute
        public String label;

        @XmlAttribute
        public String color;
    }

    public static class DimensionElement {

        @XmlAttribute
        public String type;

        @XmlAttribute
        private Integer levelId;

        @XmlAttribute
        private String dateUnit;

        @XmlElement(name="category")
        private List<CategoryElement> categories = new ArrayList<CategoryElement>(0);
    }

    private Dimension createDim(DimensionElement element) {
         if("admin".equals(element.type)) {
            return new AdminDimension(element.levelId);
         } else if("date".equals(element.type)) {
             return new DateDimension(findEnumValue(DateUnit.values(), element.dateUnit));
         } else {
            return new Dimension(findEnumValue(DimensionType.values(), element.type));
        }
    }

    private <T extends Enum<T>> T findEnumValue(T[] values, String text) {
        String textLowered = text.toLowerCase();
        for(T value : values) {
            if(value.toString().toLowerCase().equals(textLowered))
                return value;
        }
        throw new RuntimeException("'" + text + "' is not a member of " + values[0].getClass().getName());
    }

    @Override
    public Dimension unmarshal(DimensionElement element) throws Exception {
        Dimension dim = createDim(element);

        for(CategoryElement category : element.categories) {
           CategoryProperties props = new CategoryProperties();
           props.setLabel(category.label);
            if(category.color != null) {
                props.setColor(decodeColor(category.color));
            }
            EntityCategory entityCategory = new EntityCategory(Integer.parseInt(category.name));
            dim.getCategories().put(entityCategory, props);
            dim.getOrdering().add(entityCategory);
        }

        return dim;
    }

    private int decodeColor(String color) {
        if(color.startsWith("#")) {
            return Integer.parseInt(color.substring(1),16);
        } else {
            return Integer.parseInt(color, 16);
        }
    }

    @Override
    public DimensionElement marshal(Dimension dim) throws Exception {
        DimensionElement element = new DimensionElement();
        element.type = dim.getType().toString();
        if(dim instanceof AdminDimension) {
            element.type = "admin";
            element.levelId = ((AdminDimension) dim).getLevelId();
        } else if(dim instanceof DateDimension) {
            element.type = "date";
            element.dateUnit = ((DateDimension) dim).getUnit().toString().toLowerCase();
        }
        return element;
    }
}
