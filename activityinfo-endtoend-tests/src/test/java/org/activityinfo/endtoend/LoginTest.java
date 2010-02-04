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

import org.activityinfo.endtoend.page.GxtApplication;
import org.activityinfo.endtoend.page.LoginPage;
import org.hamcrest.Matchers;
import org.junit.Test;

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