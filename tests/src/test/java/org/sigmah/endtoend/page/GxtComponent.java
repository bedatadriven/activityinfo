/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.SystemClock;
import org.sigmah.endtoend.xpath.Predicate;
import org.sigmah.endtoend.xpath.Step;
import org.sigmah.endtoend.xpath.XPath;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.HtmlXPath.ofClass;

public class GxtComponent {

    protected final WebElement element;
    private String text;

    public GxtComponent(WebElement element) {
        this.element = element;
    }

    public GxtComponent find(Step... steps) {
        String xpath = XPath.relative(steps);
        System.err.println("query = " + xpath);
        return new GxtComponent(findElementByXpath(xpath));
    }

    public GxtComponent find(Predicate... predicate) {
        return find(descendant(predicate));
    }

    public GxtComponent findAbsolute(Step... steps) {
        return new GxtComponent(findElementByXpath(absolute(steps)));
    }

    public void assertTextIsPresent(String text) {
        find(descendant(containingText(text)));
    }

    public WebElement findElementByXpath(String xpath, Object... args) {
        return findElement(By.xpath(String.format(xpath, args)));
    }

    public WebElement findElement(By by) {
        return new SlowLoadableElement(new SystemClock(), 10, element, by).get().getElement();
    }

    public void clickOn(String text) {
        GxtComponent child = find(descendant(havingTextEqualTo(text)));
        System.err.println("element.getText() = " + child.getText());

        child.click();
    }

    public void click() {
        element.click();
    }

    public WebElement onInputWithLabel(String label) {
        return find(
                descendant(havingTextEqualTo(label)),
                following(element("input"))
        ).getElement();
    }

    public WebElement getElement() {
        return element;
    }

    public String getText() {
        return text;
    }

    public GxtGrid asGrid() {
        return new GxtGrid(element);
    }

    public GxtComboBox asComboBox() {
        return new GxtComboBox(element);
    }

   public GxtComponent sendKeys(CharSequence keysToSend) {
       element.sendKeys(keysToSend);
       return this;
   }

    public GxtComponent activeWindow() {
        return findAbsolute(descendant(ofClass("x-window")));
    }

}
