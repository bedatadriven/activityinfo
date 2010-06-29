/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.page;

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
