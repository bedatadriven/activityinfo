package org.activityinfo.shared.util;

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

import java.util.Collection;

public class CollectionUtil {
    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> col) {
        return col != null && col.size() > 0;
    }

    public static <T> boolean containsEqual(Collection<T> c1, Collection<T> c2) {
        if (c1 == null && c2 == null) {
            return true;
        }

        if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
            return false;
        }
        
        if (c1.size() != c2.size()) {
            return false;
        }
        
        return c1.containsAll(c2);
    }

    public static <T> boolean notContainsEqual(Collection<T> c1, Collection<T> c2) {
        return !containsEqual(c1, c2);
    }
}
