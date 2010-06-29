/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class Step {
    private Axis axis;
    private NodeTest nodeTest;
    private Predicate[] predicates;

    Step(Axis axis, NodeTest nodeTest, Predicate[] predicates) {
        this(axis, predicates);
        this.nodeTest = nodeTest;
    }

    Step(Axis axis, Predicate[] predicates) {
        this.axis = axis;
        this.predicates = predicates;
    }

    public Step(Axis axis, NodeTest nodeTest) {
        this.axis = axis;
        this.nodeTest = nodeTest;
        this.predicates = new Predicate[0];
    }

    @Override
    public String toString() {
        StringBuilder step = new StringBuilder();
        step.append(axis.getKeyword()).append("::");
        step.append(nodeTest == null ? "*" : nodeTest.toString());

        for(Predicate predicate : predicates) {
            step.append("[");
            step.append(predicate.toString());
            step.append("]");
        }
        return step.toString();
    }
}
