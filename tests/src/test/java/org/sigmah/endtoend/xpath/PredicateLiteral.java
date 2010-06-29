/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class PredicateLiteral implements Predicate {
    private String expression;

    public PredicateLiteral(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return this.expression;
    }
}
