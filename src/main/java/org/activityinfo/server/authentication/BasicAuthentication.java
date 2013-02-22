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

import java.io.IOException;

import javax.persistence.NoResultException;

import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BasicAuthentication {

    private final ServerSideAuthProvider authProvider;
    private final Provider<UserDAO> userDAO;
    private final Provider<Authenticator> authenticator;

    @Inject
    public BasicAuthentication(
        ServerSideAuthProvider authProvider,
        Provider<UserDAO> userDAO,
        Provider<Authenticator> authenticator) {
        this.authProvider = authProvider;
        this.userDAO = userDAO;
        this.authenticator = authenticator;
    }

    public Authentication tryAuthenticate(String authorizationHeader) {
        User user;
        try {
            user = doAuthentication(authorizationHeader);
        } catch (IOException e) {
            return null;
        }
        Authentication auth = new Authentication(user);
        auth.setId("");
        return auth;
    }

    public User doAuthentication(String auth) throws IOException {

        User user = authenticate(auth);

        if (user == null) {
            return null;
        }

        authProvider.set(new AuthenticatedUser("", user.getId(), user
            .getEmail()));

        return user;
    }

    // This method checks the user information sent in the Authorization
    // header against the database of users maintained in the users Hashtable.

    public User authenticate(String auth) throws IOException {
        if (auth == null) {
            // no auth
            return null;
        }
        if (!auth.toUpperCase().startsWith("BASIC ")) {
            // we only do BASIC
            return null;
        }
        // Get encoded user and password, comes after "BASIC "
        String emailpassEncoded = auth.substring(6);

        // Decode it, using any base 64 decoder

        byte[] emailpassDecodedBytes = Base64.decodeBase64(emailpassEncoded
            .getBytes());
        String emailpassDecoded = new String(emailpassDecodedBytes,
            Charsets.UTF_8);
        String[] emailPass = emailpassDecoded.split(":");

        if (emailPass.length != 2) {
            return null;
        }

        // look up the user in the database
        User user = null;
        try {
            user = userDAO.get().findUserByEmail(emailPass[0]);
        } catch (NoResultException e) {
            return null;
        }

        if (!authenticator.get().check(user, emailPass[1])) {
            return null;
        }
        return user;

    }
}
