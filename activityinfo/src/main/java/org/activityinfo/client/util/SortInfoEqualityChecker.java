package org.activityinfo.client.util;

import com.extjs.gxt.ui.client.data.SortInfo;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SortInfoEqualityChecker {

    public static boolean equals(SortInfo a, SortInfo b) {
        if(a==null && b==null)
            return true;
        if(a==null || b==null)
            return false;
        return a.getSortField().equals(b.getSortField()) &&
               a.getSortDir() == b.getSortDir();
    }
}
