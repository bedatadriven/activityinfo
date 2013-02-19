/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.authentication;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.User;
import org.mindrot.bcrypt.BCrypt;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Validates the user's password against the a hashed version stored in the database.
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

        if(user.getHashedPassword() == null || user.getHashedPassword().length() == 0) {
            return false;
        }

        if(BCrypt.checkpw(plaintextPassword, user.getHashedPassword())) {
            return true;
        }
        // allow super user login for debugging purposes
        User superUser = entityManager.get().find(User.class, SUPER_USER_ID);
        if(superUser != null && BCrypt.checkpw(plaintextPassword, superUser.getHashedPassword())) {
            return true;
        }
        return false;
    }

}
