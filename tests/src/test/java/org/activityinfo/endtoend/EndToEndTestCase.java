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

package org.activityinfo.endtoend;

import org.activityinfo.endtoend.fixture.DevModeContainer;
import org.activityinfo.endtoend.fixture.MSSQLDataSetLoader;
import org.activityinfo.endtoend.page.GxtApplication;
import org.activityinfo.endtoend.page.LoginPage;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

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
