/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alex Bertram
 */
public class PageModelTest {

    @Test
    public void testGetTemplateName() throws Exception {
        assertEquals("page/Login.ftl", PageModel.getTemplateName(LoginPageModel.class));
    }
}
