/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.authentication;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.shared.auth.AuthenticatedUser;

public final class AuthCookieUtil {
    
	private AuthCookieUtil() {}
	
    public static final int THIRTY_DAYS = 30 * 24 * 60 * 60;
    public static final int THIS_SESSION = -1;
    
    private static final String ROOT= "/";
    
    public static void addAuthCookie(ResponseBuilder response, Authentication auth, boolean remember) {
    	response.cookie(createCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE,auth.getId(), remember ));

    	response.cookie(createCookie(AuthenticatedUser.USER_ID_COOKIE,String.valueOf(auth.getUser().getId()), remember ));
        
    	response.cookie(createCookie(AuthenticatedUser.EMAIL_COOKIE,auth.getUser().getEmail(), remember ));
    }
    
    public static NewCookie createCookie(String name, String value, boolean remember) {
    	NewCookie cookie = new NewCookie(name, value, ROOT, null, 1, null, remember ? THIRTY_DAYS : THIS_SESSION, false);
    	
        return cookie;
    }
}
