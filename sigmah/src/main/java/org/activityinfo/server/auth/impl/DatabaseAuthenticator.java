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

package org.activityinfo.server.auth.impl;

import org.activityinfo.server.auth.Authenticator;
import org.activityinfo.server.domain.User;

/**
 * Validates the user's password against the a hashed version stored in the database.
 *
 * @author Alex Bertram
 */
public class DatabaseAuthenticator implements Authenticator {

    @Override
    public boolean check(User user, String plaintextPassword) {

        // TODO: this should not be allowed!
        // This only here because of an early bug which left many users without
        // passwords. These users should be issued new passwords and this hole closed.
        if (user.getHashedPassword() == null) {
            return true;
        }

        return BCrypt.checkpw(plaintextPassword, user.getHashedPassword());
    }

}
