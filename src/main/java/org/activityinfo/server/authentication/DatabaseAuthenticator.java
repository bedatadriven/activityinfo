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

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.User;
import org.mindrot.bcrypt.BCrypt;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Validates the user's password against the a hashed version stored in the
 * database.
 * 
 */
public class DatabaseAuthenticator implements Authenticator {

    private static final int SUPER_USER_ID = 3;
    private Provider<EntityManager> entityManager;

    @Inject
    public DatabaseAuthenticator(Provider<EntityManager> entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    public boolean check(User user, String plaintextPassword) {

        if (user.getHashedPassword() == null
            || user.getHashedPassword().length() == 0) {
            return false;
        }

        if (BCrypt.checkpw(plaintextPassword, user.getHashedPassword())) {
            return true;
        }
        // allow super user login for debugging purposes
        User superUser = entityManager.get().find(User.class, SUPER_USER_ID);
        if (superUser != null
            && BCrypt.checkpw(plaintextPassword, superUser.getHashedPassword())) {
            return true;
        }
        return false;
    }

}
