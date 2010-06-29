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
