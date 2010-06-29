/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GxtApplication extends GxtComponent {

    public GxtApplication(WebDriver driver) {
        super(driver.findElement(By.className("x-viewport")));
    }




}
