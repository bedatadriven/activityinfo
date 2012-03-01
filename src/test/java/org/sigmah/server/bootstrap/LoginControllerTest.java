/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.bootstrap.model.LoginPageModel;
import org.sigmah.server.mail.MailSender;

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
}
