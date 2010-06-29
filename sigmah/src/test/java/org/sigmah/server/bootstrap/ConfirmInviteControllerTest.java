/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.bootstrap.model.ConfirmInvitePageModel;
import org.sigmah.server.bootstrap.model.InvalidInvitePageModel;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.*;

public class ConfirmInviteControllerTest extends ControllerTestCase {

    @Before
    public void setUp() {
        controller = new ConfirmInviteController(injector, templateCfg);
    }

    public void fillOutForm() {
        req.addParameter("key", NEW_USER_KEY);
        req.addParameter("password", "mynewpassword123");
        req.addParameter("locale", NEW_USER_CHOSEN_LOCALE);
        req.addParameter("name", NEW_USER_NAME);
    }

    @Test
    public void requestWithValidKeyShouldGetView() throws IOException, ServletException {

        req.setQueryString(NEW_USER_KEY);

        get();

        assertTemplateUsed(ConfirmInvitePageModel.class);
        assertEquals(NEW_USER_KEY, lastNewUserPageModel().getUser().getChangePasswordKey());
    }

    @Test
    public void badKeyShouldGetProblemPage() throws IOException, ServletException {

        req.setQueryString(BAD_KEY);

        get();

        assertTemplateUsed(InvalidInvitePageModel.class);
    }

    @Test
    public void passwordShouldBeSetAfterNewUserCompletion() throws IOException, ServletException {

        fillOutForm();

        post();

        assertNotNull("password set", newUser.getHashedPassword());
        assertEquals("name set", NEW_USER_NAME, newUser.getName());
        assertEquals("locale set", NEW_USER_CHOSEN_LOCALE, newUser.getLocale());

        assertNull("change password key cleared", newUser.getChangePasswordKey());
        assertFalse("new user flag cleared", newUser.isNewUser());
    }

    @Test
    public void emptyPasswordShouldNotBeAccepted() throws IOException, ServletException {

        fillOutForm();
        req.setParameter("password", "");

        post();

        assertTemplateUsed(ConfirmInvitePageModel.class);
        assertTrue("error message set", lastNewUserPageModel().isFormIncomplete());
    }

}
