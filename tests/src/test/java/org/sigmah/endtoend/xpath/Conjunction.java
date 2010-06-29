/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class Conjunction implements Predicate {

    private StringBuilder xpath = new StringBuilder();

    protected Conjunction() {

    }

    public Conjunction(Predicate... predicates) {
        for(Predicate predicate : predicates) {
            add(predicate);
        }
    }

    protected void add(Predicate predicate) {
        if(xpath.length() != 0) {
            xpath.append(" and ");
        }
        xpath.append("(").append(predicate.toString()).append(")");
    }

    public String toString() {
        return xpath.toString();
    }
}
