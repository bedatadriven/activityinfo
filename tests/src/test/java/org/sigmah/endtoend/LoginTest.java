/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.sigmah.endtoend.page.GxtApplication;
import org.sigmah.endtoend.page.LoginPage;

import static org.junit.Assert.assertThat;

public class LoginTest extends EndToEndTestCase {


    @Test
    public void correctLoginSucceeds() {

        dataLoader.load("/dbunit/login.db.xml");

        login();

        GxtApplication app = getApp();
        app.assertTextIsPresent(EMAIL_ADDRESS);
    }

    @Test
    public void bookMarkIsSentToHostPage() {

        dataLoader.load("/dbunit/login.db.xml");

        navigate("/#charts");

        LoginPage loginPage = getLoginPage();
        loginPage.login(EMAIL_ADDRESS, CORRECT_PASSWORD);

        assertThat(driver.getCurrentUrl(), Matchers.endsWith("#charts"));
    }
}