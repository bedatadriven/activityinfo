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
