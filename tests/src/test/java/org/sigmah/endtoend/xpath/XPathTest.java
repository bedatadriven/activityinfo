/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
