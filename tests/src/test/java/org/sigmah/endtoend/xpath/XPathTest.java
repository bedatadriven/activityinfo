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

package org.sigmah.endtoend.xpath;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.sigmah.endtoend.xpath.XPath.*;
import static org.sigmah.endtoend.xpath.ext.GxtXPath.*;

public class XPathTest {

    @Test
    public void within() {
        System.err.println(
                relative(
                  descendant(isAPanel().withHeaderText("Monthly Reports")),
                  descendant(isAGrid())));
    }

    @Test
    public void grid() {
        System.err.println(relative(gridRow(3), gridCell(1)));
    }

    @Test
    public void multiplePredicates() {

        String xpath = XPath.relative(
                descendant(attribute("visible").equalTo("true"), position(3)));

        assertThat(xpath, is(equalTo("descendant::*[@visible = 'true'][position() = 3]")));

    }
}
