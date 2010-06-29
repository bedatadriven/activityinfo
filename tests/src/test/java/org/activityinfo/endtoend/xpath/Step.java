/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.endtoend.xpath;

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
