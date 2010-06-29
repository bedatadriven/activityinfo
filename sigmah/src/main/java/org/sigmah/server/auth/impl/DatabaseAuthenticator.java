/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth.impl;

import org.sigmah.server.auth.Authenticator;
import org.sigmah.server.domain.User;

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
