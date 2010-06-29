/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.page;

import org.openqa.selenium.WebElement;
import org.sigmah.endtoend.xpath.ext.HtmlXPath;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.isFormTrigger;

public class GxtComboBox extends GxtComponent {
    public GxtComboBox(WebElement element) {
        super(element);
    }

    public void trigger() {
        find(isFormTrigger()).click();
    }

    public GxtComponent findList() {
        return findAbsolute(
                descendant(element("body")),
                child(HtmlXPath.ofClass("x-combo-list")));
    }

    public void trigger(String valueToSelect) {
        trigger();
        findAbsolute(
                descendant(element("body")),
                child(HtmlXPath.ofClass("x-combo-list")),
                descendant(havingTextEqualTo(valueToSelect))
        ).click();
    }
}
