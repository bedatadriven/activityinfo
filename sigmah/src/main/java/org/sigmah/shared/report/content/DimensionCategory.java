/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import org.sigmah.shared.report.model.typeadapter.CategoryAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@XmlJavaTypeAdapter(CategoryAdapter.class)
public interface DimensionCategory extends Serializable {


    /**
     *
     * @return  The value by which to sort this category
     */
    public Comparable getSortKey();
    

}
