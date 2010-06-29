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

package org.activityinfo.endtoend.xpath.ext;

import org.activityinfo.endtoend.xpath.Conjunction;
import org.activityinfo.endtoend.xpath.Predicate;
import org.activityinfo.endtoend.xpath.Step;

import static org.activityinfo.endtoend.xpath.ext.HtmlXPath.ofClass;
import static org.activityinfo.endtoend.xpath.XPath.*;

public class GxtXPath {

    public static PanelPredicateBuilder isAPanel() {
        return new PanelPredicateBuilder();
    }

    public static Predicate isAGrid() {
        return ofClass("x-grid3");
    }

    public static Predicate isComboBox() {
        return new Conjunction(
                ofClass("x-form-field-wrap"),
                having(descendant(isFormTrigger())));
    }

    public static Predicate isFormTrigger() {
        return ofClass("x-form-trigger");
    }

    public static Predicate isToolbar() {
        return ofClass("x-toolbar");
    }

    public static Step gridRow(int index) {
        return descendant(isGridRow(), position(index));
    }

    private static Predicate isGridRow() {
        return ofClass("x-grid3-row");
    }

    public static Step gridCell(int index) {
        return descendant(ofClass("x-grid3-cell"), position(index));
    }

    public static class PanelPredicateBuilder extends Conjunction {

        public PanelPredicateBuilder() {
            add(ofClass("x-panel"));
        }

        public PanelPredicateBuilder withHeaderText(String heading) {
            add(having(descendant(ofClass("x-panel-header-text"), havingTextEqualTo(heading))));
            return this;
        }
    }


}
