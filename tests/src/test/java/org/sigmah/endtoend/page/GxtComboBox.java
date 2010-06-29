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
