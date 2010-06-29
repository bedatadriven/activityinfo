/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend;

import org.junit.Test;
import org.sigmah.endtoend.page.GxtApplication;
import org.sigmah.endtoend.page.GxtComboBox;
import org.sigmah.endtoend.page.GxtGrid;

import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.*;


public class MonthlyReportIsCorrectlySaved extends EndToEndTestCase {

    @Test
    public void monthlyReportsAreCorrectlySaved() {
        dataLoader.load("/dbunit/monthlyreports.db.xml");

        navigateToAppWithAuthorization();

        GxtApplication app = getApp();
        app.clickOn("Data Entry");
        app.clickOn("NRC");

        GxtComboBox monthCombo = app.find(
                descendant(havingTextEqualTo("Month:")), 
                following(isComboBox())).asComboBox();
        monthCombo.trigger("Feb 2010");

        GxtGrid grid = app.find(
                descendant(isAPanel().withHeaderText("Monthly Reports")),
                descendant(isAGrid())).asGrid();

        
        grid.cell(1, grid.columnIndexFromLabel("Nov 09")).click();
        grid.editor().sendKeys("1492");
        grid.cell(1, 1).click();
        app.clickOn("Save");
    }
}
