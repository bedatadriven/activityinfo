

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

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.activityinfo.shared.report.model.typeadapter.CategoryAdapter;

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
