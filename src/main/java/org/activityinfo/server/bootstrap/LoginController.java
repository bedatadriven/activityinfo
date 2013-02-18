/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Provider;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.authentication.Authenticator;
import org.activityinfo.server.bootstrap.exception.LoginException;
import org.activityinfo.server.bootstrap.exception.PasswordExpiredException;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.bootstrap.model.Redirect;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;

@Path(LoginController.ENDPOINT)
public class LoginController extends AbstractController {
    
	public static final String ENDPOINT = "/login";

	@Inject
	private Provider<Authenticator> authenticator;

    
    @GET
    @LogException(emailAlert = true)
    public Response onGet(@Context HttpServletRequest req) throws Exception {
		return writeView(req, new LoginPageModel(parseUrlSuffix(req)));
    }

    @POST
    @LogException(emailAlert = true)
	public Response onPost(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws Exception {
		boolean ajax = "true".equals(req.getParameter("ajax"));
		ResponseBuilder response = Response.ok();
		
		try {
			AuthenticatedUser user = login(resp, req.getParameter("email"), req.getParameter("password"), 
					false);
			addLocaleCookie(response, user);
			if(!ajax)
				response = Response.ok(new Redirect("/"));
		} catch (LoginException e) {
			if(ajax) {
				response.status(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				return writeView(req,
						LoginPageModel.unsuccessful(parseUrlSuffix(req)));
			}
		}
		
		return response.build();
	}
    

	private void checkPassword(String password, User user) throws LoginException {
		if (!authenticator.get().check(user, password)) {
			throw new LoginException();
		}
	}
	

	private AuthenticatedUser createAuthCookie(HttpServletResponse resp, User user, boolean rememberLogin) {
        Authentication auth = createNewAuthToken(user);
        
        /*
         * We're 'inlining the code in AuthCookieUtil.addAuthCookie
         */
        {
        	final int maxAge = rememberLogin ? AuthCookieUtil.THIRTY_DAYS : AuthCookieUtil.THIS_SESSION;
        	
        	Collection<Cookie> cookiesToAdd = new ArrayList<Cookie>();
        	
        	cookiesToAdd.add(new Cookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, auth.getId()));
        	cookiesToAdd.add(new Cookie(AuthenticatedUser.USER_ID_COOKIE,String.valueOf(auth.getUser().getId())));
        	cookiesToAdd.add(new Cookie(AuthenticatedUser.EMAIL_COOKIE,auth.getUser().getEmail()));

			for (Cookie c : cookiesToAdd) {
				c.setMaxAge(maxAge);
				
				resp.addCookie(c);
			}
        }
        
		return new AuthenticatedUser(auth.getId(), auth.getUser().getId(), auth.getUser().getEmail());
	}
    

	public AuthenticatedUser login(HttpServletResponse resp, String email, String password, boolean rememberLogin) throws LoginException {
		User user = findUserByEmail(email);
		if(user.getHashedPassword() == null || user.getHashedPassword().length() == 0) {
			throw new PasswordExpiredException();
		} else {
			checkPassword(password, user);
			return createAuthCookie(resp, user, rememberLogin); 
		}
	}

	/**
	 * Adds the user's locale as a cookie, so that we preserve the right
	 * language even after they log out.
	 * @param response
	 * @param user
	 */
	private void addLocaleCookie(ResponseBuilder response,
			AuthenticatedUser user) {
		response.cookie(new NewCookie("locale", user.getUserLocale(), "/", null, null, 60 * 60 * 24 * 365, false));
	}
}
