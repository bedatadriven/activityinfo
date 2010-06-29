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
