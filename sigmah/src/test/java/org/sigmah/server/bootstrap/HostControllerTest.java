/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.bootstrap.model.HostPageModel;
import org.sigmah.shared.auth.AuthenticatedUser;

public class HostControllerTest extends ControllerTestCase {

    private static final String HOME_PAGE = "/content/";

	@Before
    public void setupController() {
        controller = new HostController(injector, templateCfg);
        req.setRequestURL("http://www.activityinfo.org");
    }

    @Test
    public void verifyThatRequestsWithoutAuthTokensAreRedirectedToLoginPage() throws IOException, ServletException {

        get();

        assertEquals(resp.redirectUrl, HOME_PAGE);
    }

    @Test
    public void verifyThatRequestWithValidAuthTokensReceiveTheView() throws IOException, ServletException {
        req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, GOOD_AUTH_TOKEN);

        get();

        assertTemplateUsed(HostPageModel.class);
    }

    @Test
    public void verifyThatRequestWithFakeAuthTokensAreRedirectedToLogin() throws IOException, ServletException {
        req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, BAD_AUTH_TOKEN);

        get();

        assertEquals(HOME_PAGE, resp.redirectUrl);
    }
}
