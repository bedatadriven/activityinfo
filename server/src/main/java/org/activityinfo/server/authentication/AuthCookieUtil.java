/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.shared.auth.AuthenticatedUser;

public final class AuthCookieUtil {
    
	private AuthCookieUtil() {}
	
    private static final int THIRTY_DAYS = 30 * 24 * 60 * 60;
    private static final int THIS_SESSION = -1;
    private static final String ROOT= "/";
    
    public static void addAuthCookie(HttpServletResponse response, Authentication auth, boolean remember) {
        
    	response.addCookie(createCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE,auth.getId(), remember ));

    	response.addCookie(createCookie(AuthenticatedUser.USER_ID_COOKIE,String.valueOf(auth.getUser().getId()), remember ));
        
    	response.addCookie(createCookie(AuthenticatedUser.EMAIL_COOKIE,auth.getUser().getEmail(), remember ));
    }
    
    public static Cookie createCookie(String name, String value, boolean remember){
    	Cookie cookie = new Cookie(name, value);
    	cookie.setMaxAge(remember ? THIRTY_DAYS : THIS_SESSION);
    	cookie.setPath(ROOT);
        
        return cookie;
    }
}
