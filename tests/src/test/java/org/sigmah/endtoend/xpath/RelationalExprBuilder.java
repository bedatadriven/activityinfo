/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class RelationalExprBuilder<T extends Number> extends EqualityExprBuilder<T> {

    public RelationalExprBuilder(String leftHandSide) {
        expr.append(leftHandSide);
    }

    public Predicate greaterThan(T rightHandSide) {
        return compose(">", rightHandSide);
    }

}
