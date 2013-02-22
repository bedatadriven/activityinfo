package org.activityinfo.shared.auth;

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

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Encapsulates user identity and their authorization to access the server.
 * 
 * This is normally injected, see the default
 * {@link org.activityinfo.client.authentication.ClientSideAuthProvider}
 * 
 */
public class AuthenticatedUser implements IsSerializable {
    private String authToken;
    private int userId;
    private String userEmail;
    private String userLocale;
    /**
     * Authentication token.
     */
    public static final String AUTH_TOKEN_COOKIE = "authToken";
    public static final String EMAIL_COOKIE = "email";
    public static final String USER_ID_COOKIE = "userId";
    public static final String USER_LOCAL_COOKIE = "locale";

    public AuthenticatedUser() {

    }

    public AuthenticatedUser(String authToken, int userId, String userEmail,
        String userLocale) {
        super();
        this.authToken = authToken;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userLocale = userLocale;
    }

    public AuthenticatedUser(String authToken, int userId, String userEmail) {
        super();
        this.authToken = authToken;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userLocale = "en";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String locale) {
        this.userLocale = locale;
    }

    public int getId() {
        return userId;
    }

    public static AuthenticatedUser getAnonymous() {
        return new AuthenticatedUser(AnonymousUser.AUTHTOKEN, 0,
            AnonymousUser.USER_EMAIL);
    }

    public static AuthenticatedUser getAnonymous(LocaleInfo currentLocale) {
        return new AuthenticatedUser(AnonymousUser.AUTHTOKEN, 0,
            AnonymousUser.USER_EMAIL, currentLocale.getLocaleName());
    }

    public boolean isAnonymous() {
        return AnonymousUser.AUTHTOKEN.equals(authToken);
    }
}
