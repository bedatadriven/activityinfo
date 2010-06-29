/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath.ext;

import org.sigmah.endtoend.xpath.Predicate;
import org.sigmah.endtoend.xpath.PredicateLiteral;
import org.sigmah.endtoend.xpath.XPath;

public class HtmlXPath {

    public static Predicate ofClass(final String className) {
        return ofClasses(className);
    }

    public static Predicate ofClasses(String... classNames) {
        StringBuilder predicate = new StringBuilder("@class");
        for(String className : classNames) {
            predicate.append(XPath.format(" and contains(concat(' ', normalize-space(@class), ' '), '%s')",
                " " + className.trim() + " "));
        }
        return new PredicateLiteral(predicate.toString());
    }

}
