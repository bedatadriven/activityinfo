/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.activityinfo.server.authentication.Authenticator;
import org.activityinfo.server.bootstrap.fixtures.ContainerRule;
import org.activityinfo.server.bootstrap.fixtures.MockHttpServletRequest;
import org.activityinfo.server.bootstrap.fixtures.MockTemplateConfiguration;
import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.bootstrap.model.TemplateDirective;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.easymock.IAnswer;
import org.junit.Rule;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import freemarker.template.Configuration;

/**
 * @author Alex Bertram
 */
public abstract class ControllerTestCase<K extends AbstractController> {
	public class CoreContainerModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(HttpServletRequest.class).toInstance(
					req = new MockHttpServletRequest());

			User user = new User();
			user.setEmail(USER_EMAIL);
			user.setHashedPassword("$2$Foo");

			newUser.setEmail(NEW_USER_EMAIL);
			newUser.setNewUser(true);
			newUser.setEmailNotification(false);
			newUser.setHashedPassword("$2$Foo");
			newUser.setChangePasswordKey(NEW_USER_KEY);

			UserDAO userDAO = createMock(UserDAO.class);
			expect(userDAO.findUserByEmail(USER_EMAIL)).andReturn(user);
			expect(userDAO.findUserByEmail(not(eq(USER_EMAIL)))).andThrow(
					new NoResultException());

			expect(userDAO.findUserByChangePasswordKey(NEW_USER_KEY))
					.andReturn(newUser);
			expect(userDAO.findUserByChangePasswordKey(BAD_KEY)).andThrow(
					new NoResultException());
			replay(userDAO);

			Authentication auth = new Authentication(user);
			auth.setId(GOOD_AUTH_TOKEN);

			AuthenticationDAO authDAO = createNiceMock(AuthenticationDAO.class);
			expect(authDAO.findById(eq(GOOD_AUTH_TOKEN))).andReturn(auth);
			authDAO.persist(isA(Authentication.class));
			expectLastCall().andAnswer(new IAnswer<Object>() {
				@Override
				public Object answer() throws Throwable {
					Authentication auth = (Authentication) getCurrentArguments()[0];
					auth.setId(NEW_AUTH_TOKEN);
					return null;
				}
			});
			replay(authDAO);

			Authenticator authenticator = createMock(Authenticator.class);
			expect(authenticator.check(eq(user), eq(WRONG_USER_PASS)))
					.andReturn(false);
			expect(authenticator.check(eq(user), eq(CORRECT_USER_PASS)))
					.andReturn(true);
			replay(authenticator);

			bind(AuthenticationDAO.class).toInstance(authDAO);
			bind(Authenticator.class).toInstance(authenticator);
			bind(UserDAO.class).toInstance(userDAO);

			try {
				bind(Configuration.class).toInstance(
						templateCfg = new MockTemplateConfiguration());
			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}

			bind(MailSender.class).toInstance(createMock(MailSender.class));

			requestInjection(ControllerTestCase.this);
		}
	}

	@Inject
	protected MockHttpServletRequest req;

	protected Response resp;

	@Inject
	protected Injector injector;

	protected MockTemplateConfiguration templateCfg;

	@Inject
	protected K controller;

	@Inject
	protected MailSender sender;

	protected User newUser;

	protected static final String USER_EMAIL = "alex@bertram.com";
	protected static final String CORRECT_USER_PASS = "mypass";
	protected static final String WRONG_USER_PASS = "notmypass";

	protected static final String NEW_AUTH_TOKEN = "XYZ123";

	protected static final String NEW_USER_KEY = "ABC456";
	protected static final String NEW_USER_EMAIL = "bart@bart.nl";
	protected static final String BAD_KEY = "muwahaha";
	protected static final String NEW_USER_NAME = "Henry";
	protected static final String NEW_USER_CHOSEN_LOCALE = "fr";

	protected static final String GOOD_AUTH_TOKEN = "BXD556";
	protected static final String BAD_AUTH_TOKEN = "NONSENSE";

	public ControllerTestCase() {
		newUser = new User();
	}

	@Rule
	public final ContainerRule containerRule = new ContainerRule(
			getContainerModule());

	protected Module getContainerModule() {
		ParameterizedType c = (ParameterizedType) (getClass()
				.getGenericSuperclass());
		Class<?> clazz = (Class<?>) c.getActualTypeArguments()[0];

		return Modules.combine(new CoreContainerModule(),
				new SingleControllerModule(clazz));
	}

	public void get() throws Exception {
		resp = controller.callGet(req);
	}

	protected void get(String url) throws Exception {
		req.setRequestURL(url);
		get();
	}

	protected void post() throws Exception {
		resp = controller.callPost(req);
	}

	protected <T extends PageModel> void assertTemplateUsed(Class<T> modelclass) {
		assertEquals(PageModel.getTemplateName(modelclass),
				templateCfg.lastTemplateName);
	}

	protected LoginPageModel lastLoginPageModel() {
		return LoginPageModel.class.cast(TemplateDirective.class.cast(
				resp.getEntity()).getPageModel());
	}

	protected ConfirmInvitePageModel lastNewUserPageModel() {
		return ConfirmInvitePageModel.class.cast(TemplateDirective.class.cast(
				resp.getEntity()).getPageModel());
	}
}
