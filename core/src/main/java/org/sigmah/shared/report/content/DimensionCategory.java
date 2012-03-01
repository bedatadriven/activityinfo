/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sigmah.shared.report.model.typeadapter.CategoryAdapter;

/**
 * I'm not sure if this is the proper OLAP terminology, but 
 * for our purposes we consider a Dimension like year, partner, province,
 * etc, to be divided into "categories". Each DimensionCategory has a label
 * and can be ordered. 
 *  
 */
@XmlJavaTypeAdapter(CategoryAdapter.class)
public interface DimensionCategory extends Serializable {


    /**
     *
     * @return  The value by which to sort this category
     */
    Comparable getSortKey();
    
    
    String getLabel();
    
}
