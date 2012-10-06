/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.server.DefaultSelectionServlet;
import com.bedatadriven.rebar.appcache.server.PropertyProvider;
import com.bedatadriven.rebar.appcache.server.SelectionException;
import com.bedatadriven.rebar.appcache.server.UnknownUserAgentException;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Overrides the behavior of the default rebar-appcache
 * servlet to do custom locale selection based on the 
 * authenticated user's profile.
 * 
 * @author alex
 *
 */
@Singleton
public class SelectionServlet extends DefaultSelectionServlet {

	@Inject
    public SelectionServlet(Provider<EntityManager> entityManager) {
        registerProvider("locale", new LocaleProvider(entityManager));
        registerProvider("gwt.logging.logLevel", new LogLevelProvider());
    }

    private class LocaleProvider implements PropertyProvider {

        private final Provider<EntityManager> entityManager;

        public LocaleProvider(Provider<EntityManager> entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public String get(HttpServletRequest req) {
            Authentication auth = entityManager.get().find(Authentication.class, getAuthToken(req));
            if(auth == null) {
            	throw new UserNotAuthenticatedException("expired authtoken");
            }
            return auth.getUser().getLocale();
        }

        private String getAuthToken(HttpServletRequest req) {
            for(Cookie cookie : req.getCookies()) {
                if(cookie.getName().equals(AuthenticatedUser.AUTH_TOKEN_COOKIE)) {
                    return cookie.getValue();
                }
            }
            throw new UserNotAuthenticatedException("No authToken cookie");
        } 
    }
    
    private class UserNotAuthenticatedException extends SelectionException {

		public UserNotAuthenticatedException(String message) {
			super(message);
		}
    }
    
    @Override
	protected void handleSelectionException(Path path, Exception e,
			HttpServletResponse resp) throws IOException {
		if(e instanceof UnknownUserAgentException) {
			resp.getWriter().print("window.location = 'browser.html'");
		} else {
			resp.sendError(CACHE_OBSOLETE, e.getMessage());
		}
	}

	@Override
	protected void handleNoAvailablePermutation(Path path,
			HttpServletResponse resp) throws IOException {
		if(path.file.endsWith(".js")) {
			resp.getWriter().println("window.location = 'browser.html';");
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported browser");
		}
	}

	/**
     * Set the log_level to be used based on the host name
     */
    private class LogLevelProvider implements PropertyProvider {
        @Override
        public String get(HttpServletRequest request) {
            if(request.getServerName().contains("localhost") ||
               request.getServerName().contains("127.0.0.1") ||
               request.getServerName().contains("appspot")) {

                return "FINEST";

            } else {
                return "SEVERE";
            }
        }
    }

    @VisibleForTesting
    void testInit(ServletConfig config) throws ServletException {
    	init(config);
    }
    
    @VisibleForTesting
    void testGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doGet(req, resp);
    }
}
