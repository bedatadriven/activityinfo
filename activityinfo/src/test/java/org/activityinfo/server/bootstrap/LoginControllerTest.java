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

import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.*;

public class LoginControllerTest extends ControllerTestCase {


    @Before
    public void setUp() {
        controller = new LoginController(injector, templateCfg);

        req.setRequestURL("http://activityinfo.org/");
    }

    @Test
    public void requestShouldReceiveView() throws IOException, ServletException {

        get();

        assertNull(resp.redirectUrl);
        assertTemplateUsed(LoginPageModel.class);
    }

    @Test
    public void urlSuffixIsParsedCorrectly() throws IOException, ServletException {

        get("http://activityinfo.org/#charts");

        assertEquals("#charts", controller.parseUrlSuffix(req));
    }

    @Test
    public void bookmarkShouldBeSentToLoginForm() throws IOException, ServletException {

        get("http://activityinfo.org/#charts");

        assertNull(resp.redirectUrl);
        assertTemplateUsed(LoginPageModel.class);
        assertEquals("#charts", lastLoginPageModel().getUrlSuffix());
    }


    @Test
    public void invalidEmailShouldResultInLoginPageWithErrorMessage() throws IOException, ServletException {

        req.addParameter("email", "notreallyanemail@dot.com");
        req.addParameter("password", WRONG_USER_PASS);

        post();

        assertTemplateUsed(LoginPageModel.class);
        assertTrue("error message is displayed", lastLoginPageModel().isLoginError());
    }


    @Test
    public void invalidPasswordShouldResultInLoginPageWithErrorMessage() throws IOException, ServletException {

        req.addParameter("email", USER_EMAIL);
        req.addParameter("password", WRONG_USER_PASS);

        post();

        assertTemplateUsed(LoginPageModel.class);
        assertTrue("error message is displayed", lastLoginPageModel().isLoginError());
    }

    @Test
    public void correctLoginShouldRedirectToHostPage() throws IOException, ServletException {

        req.addParameter("email", USER_EMAIL);
        req.addParameter("password", CORRECT_USER_PASS);

        post();

        assertNotNull("redirected", resp.redirectUrl);
        assertEquals("new auth token", NEW_AUTH_TOKEN, resp.getCookie(AbstractController.AUTH_TOKEN_COOKIE));
    }

    @Test
    public void bookmarkShouldBeSentToHostPage() throws IOException, ServletException {

        req.addParameter("email", USER_EMAIL);
        req.addParameter("password", CORRECT_USER_PASS);
        req.addParameter("urlSuffix", "#map");

        post();

        assertTrue(resp.redirectUrl.endsWith("#map"));
    }
    


}
