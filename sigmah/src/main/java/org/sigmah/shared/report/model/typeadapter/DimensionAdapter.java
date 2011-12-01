/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.typeadapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.CategoryProperties;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

/**
 * @author Alex Bertram
 */
public class DimensionAdapter extends XmlAdapter<DimensionAdapter.DimensionElement, Dimension>  {

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
        for(T value : values) {
            if(value.toString().equalsIgnoreCase(text)) {
                return value;
            }
        }
        throw new IllegalArgumentException("'" + text + "' is not a member of " + values[0].getClass().getName());
    }

    @Override
    public Dimension unmarshal(DimensionElement element) {
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
    public DimensionElement marshal(Dimension dim)  {
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
