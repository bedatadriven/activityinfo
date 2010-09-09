/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;

import org.sigmah.server.auth.SecureTokenGenerator;
import org.sigmah.server.mail.Invitation;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.UserPermissionDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
        return em.createNamedQuery("findUserByEmail")
                .setParameter("email", email)
                .getResultList().size() == 1;
    }

    @Override
    public User findUserByEmail(String email) throws NoResultException {

        return (User) em.createNamedQuery("findUserByEmail")
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public User findUserByChangePasswordKey(String key) {
        return (User) em.createNamedQuery("findUserByChangePasswordKey")
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
        user.setLocale(locale);
        user.setChangePasswordKey(SecureTokenGenerator.generate());
        return user;
    }
}
