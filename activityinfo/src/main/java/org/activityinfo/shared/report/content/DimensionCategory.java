package org.activityinfo.shared.report.content;

import java.io.Serializable;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface DimensionCategory extends Serializable {


    /**
     *
     * @return  The value by which to sort this category
     */
    public Comparable getSortKey();
    

}
