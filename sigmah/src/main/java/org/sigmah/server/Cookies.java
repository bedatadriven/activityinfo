/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server;

import org.sigmah.server.domain.Authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
    public static final String AUTH_TOKEN_COOKIE = "authToken";
    
    private class For {
        private static final int THIRTY_DAYS = 30 * 24 * 60 * 60;
        private static final int THIS_SESSION = -1;
    }


    public static void addAuthCookie(HttpServletResponse response, Authentication auth, boolean remember) {
        Cookie authCookie = new Cookie(AUTH_TOKEN_COOKIE, auth.getId());
        authCookie.setMaxAge(remember ? For.THIRTY_DAYS : For.THIS_SESSION);
        authCookie.setPath("/");
        response.addCookie(authCookie);
    }
}
