/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath.ext;

import org.sigmah.endtoend.xpath.Conjunction;
import org.sigmah.endtoend.xpath.Predicate;
import org.sigmah.endtoend.xpath.Step;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.HtmlXPath.ofClass;

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
