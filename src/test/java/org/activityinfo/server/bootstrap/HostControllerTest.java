/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.util.Properties;

import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Module;
import com.google.inject.util.Modules;

public class HostControllerTest extends ControllerTestCase<HostController> {
	private static final DeploymentConfiguration DEPLOYMENT_CFG = new DeploymentConfiguration(new Properties());

	protected Module getContainerModule() {
		return Modules.combine(new CoreContainerModule(), new SingleControllerModule(HostController.class) {
			@Override
			protected void configure() {
				super.configure();
				
				bind(DeploymentConfiguration.class).toInstance(DEPLOYMENT_CFG);
			}
		});
	}

	@Before
    public void setupController() {
        req.setRequestURL("http://www.activityinfo.org");
    }

	@Test
	public void verifyThatRequestsWithoutAuthTokensAreRedirectedToLoginPage() throws Exception {

		get();

		assertTemplateUsed(LoginPageModel.class);
	}

	@Test
	public void verifyThatRequestWithValidAuthTokensReceiveTheView() throws Exception {
		req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, GOOD_AUTH_TOKEN);

		get();

		assertTemplateUsed(HostPageModel.class);
	}

	@Test
	public void verifyThatRequestWithFakeAuthTokensAreRedirectedToLogin() throws Exception {
		req.addCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, BAD_AUTH_TOKEN);

		get();

		assertTemplateUsed(LoginPageModel.class);
	}

}
