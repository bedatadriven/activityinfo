/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.typeadapter;

import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.EntityCategory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Alex Bertram
 */
public class CategoryAdapter extends XmlAdapter<CategoryAdapter.Category, DimensionCategory> {

    public static class Category {

        @XmlAttribute
        public Integer id;
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
