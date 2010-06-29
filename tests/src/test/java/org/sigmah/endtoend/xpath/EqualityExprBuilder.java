/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class EqualityExprBuilder<T> implements Predicate {
    protected StringBuilder expr = new StringBuilder();

    public Predicate equalTo(T rightHandSide) {
        return compose("=", rightHandSide);
    }

    public Predicate notEqualTo(T rightHandSide) {
        return compose("!=", rightHandSide);
    }

    protected Predicate compose(String operator, Object value) {
        expr.append(operator);
        expr.append(value.toString());
        return new PredicateLiteral(expr.toString());
    }
}
