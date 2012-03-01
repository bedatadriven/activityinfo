/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class AssertUtils {

    public static <T> void assertSorted(String name, Collection<T> collection,
                                        Comparator<T> comparator) {
        if (collection.size() <= 1) {
            return;
        }

        Iterator<T> it = collection.iterator();
        T last = it.next();

        while (it.hasNext()) {
            T next = it.next();
            if (comparator.compare(last, next) > 0) {
                throw new AssertionError("The collection '" + name + "' is not sorted, " +
                        last.toString() + " > " + next.toString());
            }
            last = next;
        }
    }

}
