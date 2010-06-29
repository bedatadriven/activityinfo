/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.sigmah.endtoend.fixture.DevModeContainer;
import org.sigmah.endtoend.fixture.MSSQLDataSetLoader;
import org.sigmah.endtoend.page.GxtApplication;
import org.sigmah.endtoend.page.LoginPage;

public class EndToEndTestCase {
    protected MSSQLDataSetLoader dataLoader = new MSSQLDataSetLoader();
    protected static final String EMAIL_ADDRESS = "alex@bertram.com";
    protected static final String CORRECT_PASSWORD = "monday";

    protected FirefoxDriver driver = new FirefoxDriver("GWT_TESTING");
    protected final DevModeContainer container = new DevModeContainer();

    public EndToEndTestCase() {

    }


    protected LoginPage getLoginPage() {
        return PageFactory.initElements(driver, LoginPage.class);
    }


    protected GxtApplication getApp() {
        return new GxtApplication(driver);
    }

    protected void navigate(String page) {
        String url = container.getUrl(page);
        System.err.println("Navigation to " + url);
        driver.get(url);
    }

    protected void removeAuthCookie() {
        driver.manage().deleteCookieNamed("authToken");
    }

    protected void login() {
      //  removeAuthCookie();
        navigate("/");

        LoginPage loginPage = getLoginPage();
        loginPage.login(EMAIL_ADDRESS, CORRECT_PASSWORD);
    }


    protected void navigateToAppWithAuthorization() {
        navigate("/?auth=XYZ123");
    }

}
