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
import org.activityinfo.endtoend.page.GxtComponent;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import static org.activityinfo.endtoend.xpath.XPath.*;
import static org.activityinfo.endtoend.xpath.ext.GxtXPath.*;

public class CreateDatabaseTest extends EndToEndTestCase {


    @Test
    public void canCreateDatabase() {
        dataLoader.load("/dbunit/createdatabase.db.xml");

        navigateToAppWithAuthorization();

        GxtApplication app = getApp();
        app.clickOn("Setup");
        app.find(
                descendant(isToolbar()),
                descendant(havingTextEqualTo("New Database"))
        ).click();

        GxtComponent window = app.activeWindow();
        window.find(
                descendant(havingTextEqualTo("Name:")),
                following(element("input"))
        ).sendKeys("My New Database");

        window.find(
                descendant(havingTextEqualTo("Description:")),
                following(element("input"))
        ).sendKeys("A full description of my new database");

        window.find(
                descendant(havingTextEqualTo("Country:")),
                following(isComboBox())
        ).asComboBox().trigger("Haiti");

        window.clickOn("Save");

        // should now appear in grid
        app.find(
                descendant(havingTextEqualTo("A full description of my new database"))
        );
    }

    private void addAuthCookie() {
        removeAuthCookie();
        driver.manage().addCookie(new Cookie.Builder("authToken", "XYZ123").domain("localhost").build());
    }
}
