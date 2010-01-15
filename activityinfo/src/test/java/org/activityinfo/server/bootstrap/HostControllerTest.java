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

import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HostControllerTest extends ControllerTestCase {

    @Before
    public void setupController() {
        controller = new HostController(injector, templateCfg);
        req.setRequestURL("http://www.activityinfo.org");
    }

    @Test
    public void verifyThatRequestsWithoutAuthTokensAreRedirectedToLoginPage() throws IOException, ServletException {

        get();

        assertEquals(resp.redirectUrl, LoginController.ENDPOINT);
    }

    @Test
    public void verifyThatRequestWithValidAuthTokensReceiveTheView() throws IOException, ServletException {
        req.addCookie(AbstractController.AUTH_TOKEN_COOKIE, GOOD_AUTH_TOKEN);

        get();

        assertTemplateUsed(HostPageModel.class);
    }

    @Test
    public void verifyThatRequestWithFakeAuthTokensAreRedirectedToLogin() throws IOException, ServletException {
        req.addCookie(AbstractController.AUTH_TOKEN_COOKIE, BAD_AUTH_TOKEN);

        get();

        assertEquals(LoginController.ENDPOINT, resp.redirectUrl);
    }

    @Test
    public void verifyThatBookmarksArePassedOnToLoginPage() throws IOException, ServletException {

        req.setRequestURL("http://www.activityinfo.org/#charts");

        get();

        assertTrue(resp.redirectUrl.endsWith("#charts"));
    }
}
