/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.Cookies;
import org.sigmah.server.bootstrap.model.HostPageModel;

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
        req.addCookie(Cookies.AUTH_TOKEN_COOKIE, GOOD_AUTH_TOKEN);

        get();

        assertTemplateUsed(HostPageModel.class);
    }

    @Test
    public void verifyThatRequestWithFakeAuthTokensAreRedirectedToLogin() throws IOException, ServletException {
        req.addCookie(Cookies.AUTH_TOKEN_COOKIE, BAD_AUTH_TOKEN);

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
