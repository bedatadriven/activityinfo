/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage {

    @FindBy(how = How.NAME, using="email")
    private WebElement emailElement;

    @FindBy(how = How.NAME, using="password")
    private WebElement passwordElement;

    public void login(String email, String password) {
        emailElement.sendKeys(email);
        passwordElement.sendKeys(password);
        passwordElement.submit();
    }

}
