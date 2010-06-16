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

package org.activityinfo.server.bootstrap;

import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.junit.Before;
import org.junit.Test;

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
