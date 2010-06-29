/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend;

import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.sigmah.endtoend.page.GxtApplication;
import org.sigmah.endtoend.page.GxtComponent;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.isComboBox;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.isToolbar;

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
