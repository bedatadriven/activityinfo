/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.bootstrap.model.LoginPageModel;
import org.sigmah.server.mail.MailSender;
import org.sigmah.shared.auth.AuthenticatedUser;

public class LoginControllerTest extends ControllerTestCase {

    @Before
    public void setUp() {
    	MailSender sender = createMock(MailSender.class);
    	replay(sender);
    	
        controller = new LoginController(injector, templateCfg, sender);

        req.setRequestURL("http://activityinfo.org/login");
    }

    @Test
    public void requestShouldReceiveView() throws IOException, ServletException {

        get();

        assertNull(resp.redirectUrl);
        assertTemplateUsed(LoginPageModel.class);
    }

    @Test
    public void urlSuffixIsParsedCorrectly() throws IOException, ServletException {

        get("http://activityinfo.org/login#charts");

        assertEquals("#charts", controller.parseUrlSuffix(req));
    }

    @Test
    public void bookmarkShouldBeIncludedInModel() throws IOException, ServletException {

        get("http://activityinfo.org/login#charts");

        assertNull(resp.redirectUrl);
        assertTemplateUsed(LoginPageModel.class);
        assertEquals("#charts", lastLoginPageModel().getUrlSuffix());
    }


    @Test
    public void invalidEmailShouldResultInLoginPageWithErrorMessage() throws IOException, ServletException {

        req.addParameter("email", "notreallyanemail@dot.com");
        req.addParameter("password", WRONG_USER_PASS);
        try {
        	post();
        }catch (Exception e) {
        	//ignore
        }

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
        assertEquals("new auth token", NEW_AUTH_TOKEN, resp.getCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE));
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
