/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.bootstrap.fixtures.MockHttpServletResponse;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class LoginControllerTest extends ControllerTestCase<LoginController> {
	MockHttpServletResponse mockResponse = new MockHttpServletResponse();

	@Override
	protected Module getContainerModule() {
		return Modules.combine(new AbstractModule() {
			@Override
			protected void configure() {
				bind(HttpServletResponse.class).toInstance(mockResponse);
			}
		}, super.getContainerModule());
	}

	@Before
	public void setUp() {
		replay(sender);

		req.setRequestURL("http://activityinfo.org/login");
	}

	@Test
	public void requestShouldReceiveView() throws Exception {
		get();

		assertTemplateUsed(LoginPageModel.class);
	}

	@Test
	public void urlSuffixIsParsedCorrectly() throws Exception {

		get("http://activityinfo.org/login#charts");

		assertEquals("#charts", controller.parseUrlSuffix(req));
	}

	@Test
	public void bookmarkShouldBeIncludedInModel() throws Exception {

		get("http://activityinfo.org/login#charts");

		assertTemplateUsed(LoginPageModel.class);
		assertEquals("#charts", lastLoginPageModel().getUrlSuffix());
	}
}
