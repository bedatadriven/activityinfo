/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.dao;

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.database.hibernate.entity.User;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class UserDAOImpl extends GenericDAO<User, Integer> implements UserDAO {

    @Inject
    public UserDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public boolean doesUserExist(String email) {
        return getEntityManager().createNamedQuery("findUserByEmail")
                .setParameter("email", email)
                .getResultList().size() == 1;
    }

    @Override
    public User findUserByEmail(String email)  {

        return (User) getEntityManager().createNamedQuery("findUserByEmail")
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public User findUserByChangePasswordKey(String key) {
        return (User) getEntityManager().createNamedQuery("findUserByChangePasswordKey")
                .setParameter("key", key)
                .getSingleResult();
    }
    
    /**
     * Initializes this User as a new User with a secure
     * changePasswordKey
     */
    public static User createNewUser(String email, String name, String locale) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setNewUser(true);
        user.setEmailNotification(false);
        user.setLocale(locale);
        user.setChangePasswordKey(SecureTokenGenerator.generate());
        return user;
    }
}
