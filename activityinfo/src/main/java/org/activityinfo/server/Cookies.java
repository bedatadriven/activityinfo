/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server;

import org.activityinfo.server.domain.Authentication;

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
