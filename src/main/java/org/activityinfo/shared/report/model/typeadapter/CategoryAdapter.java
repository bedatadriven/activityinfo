/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model.typeadapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;

/**
 * @author Alex Bertram
 */
public class CategoryAdapter extends XmlAdapter<CategoryAdapter.Category, DimensionCategory> {

    public static class Category {

        @XmlAttribute
        private Integer id;
    }

    @Override
    public DimensionCategory unmarshal(Category category) throws Exception {
        if(category.id != null) {
            return new EntityCategory(category.id);
        }
        return null;
    }

    @Override
    public Category marshal(DimensionCategory v) throws Exception {
        return null;
    }
}
