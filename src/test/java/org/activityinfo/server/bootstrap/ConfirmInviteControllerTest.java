/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.junit.Test;

public class ConfirmInviteControllerTest extends ControllerTestCase<ConfirmInviteController> {
    public void fillOutForm() {
        req.addParameter("key", NEW_USER_KEY);
        req.addParameter("password", "mynewpassword123");
        req.addParameter("locale", NEW_USER_CHOSEN_LOCALE);
        req.addParameter("name", NEW_USER_NAME);
    }

    @Test
    public void requestWithValidKeyShouldGetView() throws Exception {
        req.setQueryString(NEW_USER_KEY);

        get();

        assertTemplateUsed(ConfirmInvitePageModel.class);
        assertEquals(NEW_USER_KEY, lastNewUserPageModel().getUser().getChangePasswordKey());
    }

    @Test
    public void badKeyShouldGetProblemPage() throws Exception {

        req.setQueryString(BAD_KEY);

        get();

        assertTemplateUsed(InvalidInvitePageModel.class);
    }

    @Test
    public void passwordShouldBeSetAfterNewUserCompletion() throws Exception {

        fillOutForm();

        post();

        assertNotNull("password set", newUser.getHashedPassword());
        assertEquals("name set", NEW_USER_NAME, newUser.getName());
        assertEquals("locale set", NEW_USER_CHOSEN_LOCALE, newUser.getLocale());

        assertNull("change password key cleared", newUser.getChangePasswordKey());
        assertFalse("new user flag cleared", newUser.isNewUser());
    }

    @Test
    public void emptyPasswordShouldNotBeAccepted() throws Exception {

        fillOutForm();
        req.setParameter("password", "");

        post();

        assertTemplateUsed(ConfirmInvitePageModel.class);
        assertTrue("error message set", lastNewUserPageModel().isFormIncomplete());
    }

}
