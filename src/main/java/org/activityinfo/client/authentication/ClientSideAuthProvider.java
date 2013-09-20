package org.activityinfo.client.authentication;

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

import java.util.Date;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Provider;

/**
 * Supplies user <code>Authentication</code> information from the
 * <code>authToken</code> and <code>email</code> cookies, or failing that, from
 * the <code>UserInfo</code> dictionary.
 * 
 * @author Alex Bertram
 */
public class ClientSideAuthProvider implements Provider<AuthenticatedUser> {

    private static final long ONE_YEAR = 365L * 24L * 60L * 60L * 1000L;

    @Override
    public AuthenticatedUser get() {

        String authToken = Cookies
            .getCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE);
        String userId = Cookies.getCookie(AuthenticatedUser.USER_ID_COOKIE);
        String email = Cookies.getCookie(AuthenticatedUser.EMAIL_COOKIE);

        if (authToken != null && userId != null && email != null) {

            return new AuthenticatedUser(
                authToken,
                Integer.parseInt(userId),
                email.replaceAll("\"", ""),
                currentLocale());

        }

        throw new InvalidAuthTokenException("Request is not authenticated");
    }

    private String currentLocale() {
        return LocaleInfo.getCurrentLocale().getLocaleName();
    }

    /**
     * unless the user requests to stay logged in, the authToken is set to expire at the end of the user's session,
     * which means that it won't be available if the user opens the app via the appcache later on. Since
     * BootstrapScriptServlet relies on the token to select the appropriate locale, without the cookie set, trying to
     * retrieve the latest manifest will fail
     */
    public static void persistAuthentication() {

        AuthenticatedUser user = new ClientSideAuthProvider().get();

        Cookies.setCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE,
            user.getAuthToken(), oneYearLater());
        Cookies.setCookie(AuthenticatedUser.USER_ID_COOKIE,
            Integer.toString(user.getUserId()), oneYearLater());
        Cookies.setCookie(AuthenticatedUser.EMAIL_COOKIE, user.getEmail(),
            oneYearLater());
    }

    private static Date oneYearLater() {
        long time = new Date().getTime();
        return new Date(time + ONE_YEAR);
    }
}
