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

package org.activityinfo.endtoend.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SlowLoadableComponent;

public class SlowLoadableElement extends SlowLoadableComponent<SlowLoadableElement> {
  
    private final WebElement parentElement;

    private NoSuchElementException lastException;
    private WebElement element;
    protected By by;


    public SlowLoadableElement(Clock clock, int timeOutInSeconds,
                               WebElement parentElement, By by) {
        super(clock, timeOutInSeconds);
        this.parentElement = parentElement;
        this.by = by;
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        try {
        element = parentElement.findElement(by);
//        if (!isElementUsable(element)) {
//          throw new NoSuchElementException("Element is not usable");
//        }
      } catch (NoSuchElementException e) {
        lastException = e;
        // Should use JUnit's AssertionError, but it may not be present
        throw new AssertionError("Unable to locate the element " + by.toString());
      }
    }

    public WebElement getElement() {
        return element;
    }
}
