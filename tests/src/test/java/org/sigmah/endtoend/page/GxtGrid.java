/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
