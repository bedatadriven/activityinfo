/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.shared.auth.AuthenticatedUser;

public final class AuthCookieUtil {
    
	private AuthCookieUtil() {}
	
    private static final int THIRTY_DAYS = 30 * 24 * 60 * 60;
    private static final int THIS_SESSION = -1;

    public static void addAuthCookie(HttpServletResponse response, Authentication auth, boolean remember) {
        Cookie authCookie = new Cookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, auth.getId());
        authCookie.setMaxAge(remember ? THIRTY_DAYS : THIS_SESSION);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        
        // user info in cookies
        Cookie emailCookie = new Cookie(AuthenticatedUser.EMAIL_COOKIE, auth.getUser().getEmail());
        emailCookie.setMaxAge(remember ? THIRTY_DAYS : THIS_SESSION);
        emailCookie.setPath("/");
        response.addCookie(emailCookie);
        
        Cookie userIdCookie = new Cookie(AuthenticatedUser.USER_ID_COOKIE, String.valueOf(auth.getUser().getId()));
        userIdCookie.setMaxAge(remember ? THIRTY_DAYS : THIS_SESSION);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);
        
    }
}
