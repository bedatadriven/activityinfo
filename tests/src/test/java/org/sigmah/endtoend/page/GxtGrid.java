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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.sigmah.endtoend.xpath.XPath;

import java.util.List;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.gridCell;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.gridRow;
import static org.sigmah.endtoend.xpath.ext.HtmlXPath.ofClass;
import static org.sigmah.endtoend.xpath.ext.HtmlXPath.ofClasses;

public class GxtGrid extends GxtComponent {
    public GxtGrid(WebElement element) {
        super(element);
    }

    public WebElement cell(int row, int col) {
        return find(
                gridRow(row),
                gridCell(col)
        ).getElement();
    }

    public int columnIndexFromLabel(String label) {
        List<WebElement> headers = element.findElements(By.xpath(relative(
                descendant(XPath.element("td"), ofClasses("x-grid3-header", "x-grid3-cell"))
        )));
        for(int i=0;i!=headers.size(); ++i) {
            String text = headers.get(i).getText().trim();
            if(text.equalsIgnoreCase(label)) {
                return i+1;
            }
        }
        throw new AssertionError("Column labeled " + label + " does not exist.");
    }



    public GxtComponent editor() {
        return find(descendant(ofClass("x-grid-editor"), not(ofClass("x-hide-display"))));
    }
}
