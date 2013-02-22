

package org.activityinfo.server.authentication;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
