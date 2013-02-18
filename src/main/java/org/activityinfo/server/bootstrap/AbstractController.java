/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;
import java.util.Locale;

import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.bootstrap.exception.IncompleteFormException;
import org.activityinfo.server.bootstrap.exception.InvalidKeyException;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.bootstrap.model.TemplateDirective;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.exception.InvalidLoginException;
import org.apache.commons.beanutils.MethodUtils;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Serves either the initial login page, or the GWT host page depending on
 * authentication status.
 * <p/>
 * The servlet uses FreeMarker (http://www.freemarker.org) to generate content
 * based on the templates in /war/ftl
 * <p/>
 * Using a static login page, rather than incorporating login into the app
 * itself simplifies the app a bit, allows browsers to remember
 * username/passwords, and allows us to load the correct, localized, permutation
 * of ActivityInfo depending on the user's choice of locale.
 * 
 * @author Alex Bertram
 */
public class AbstractController {
	@Inject
	protected Injector injector;

	@Inject
	protected Configuration templateCfg;

	private static final String GWT_CODE_SERVER_HOST = "gwt.codesvr";

	@Context
	protected Locale locale;

	/**
	 * Calls doGet. Used to be wrapped in package visibilty for testing. No
	 * longer - We hate warnings
	 */
	Response callGet(@Context HttpServletRequest req) throws Exception {
		return (Response) this.getClass().getDeclaredMethod("onGet", HttpServletRequest.class).invoke(this, req);
	}

	/**
	 * Calls doPost. Used to be wrapped in package visibilty for testing. No
	 * longer - We hate warnings
	 */
	Response callPost(@Context HttpServletRequest req) throws Exception {
		return (Response) this.getClass().getDeclaredMethod("onPost", HttpServletRequest.class).invoke(this, req);
	}

	protected Response writeView(HttpServletRequest request, PageModel model)
			throws IOException {
		Template template = templateCfg.getTemplate(model.getTemplateName());

		if (null != locale)
			template.setLocale(locale);

		return Response.ok(new TemplateDirective(template, model),
				MediaType.TEXT_HTML).build();
	}

	protected String getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null) {
			return null;
		}

		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	protected void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	protected void assertParameterPresent(String value) {
		if (value == null || value.length() == 0) {
			throw new IncompleteFormException();
		}
	}

	protected User findUserByEmail(String email) throws InvalidLoginException {
		try {
			UserDAO userDAO = getInjector().getInstance(UserDAO.class);
			return userDAO.findUserByEmail(email);

		} catch (NoResultException e) {
			throw new InvalidLoginException();
		}
	}

	protected void createAuthCookie(HttpServletRequest req,
			ResponseBuilder resp, User user, boolean remember)
			throws IOException {
		Authentication auth = createNewAuthToken(user);
		AuthCookieUtil.addAuthCookie(resp, auth, remember);
	}

	@Transactional
	protected Authentication createNewAuthToken(User user) {
		Authentication auth = new Authentication(user);
		AuthenticationDAO authDAO = getInjector().getInstance(
				AuthenticationDAO.class);
		authDAO.persist(auth);
		return auth;
	}

	String parseUrlSuffix(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		if (req.getParameter(GWT_CODE_SERVER_HOST) != null
				&& req.getParameter(GWT_CODE_SERVER_HOST).length() != 0) {
			sb.append("?" + GWT_CODE_SERVER_HOST + "=").append(
					req.getParameter(GWT_CODE_SERVER_HOST));
		}

		int hash = req.getRequestURL().lastIndexOf("#");
		if (hash != -1) {
			sb.append(req.getRequestURL().substring(hash));
		}
		return sb.toString();
	}

	protected final String getRequiredParameter(HttpServletRequest request,
			String name) {
		String value = request.getParameter(name);
		if (Strings.isNullOrEmpty(value)) {
			throw new IncompleteFormException();
		}

		return value;
	}

	@Transactional
	protected User findUserByKey(String key) throws InvalidKeyException {
		try {
			if (key == null || key.length() == 0) {
				throw new InvalidKeyException();
			}

			UserDAO userDAO = getInjector().getInstance(UserDAO.class);
			User user = userDAO.findUserByChangePasswordKey(key);
			if (user == null) {
				throw new InvalidKeyException();
			}
			return user;
		} catch (NoResultException e) {
			throw new InvalidKeyException();
		}
	}

	protected Injector getInjector() {
		return injector;
	}
}
