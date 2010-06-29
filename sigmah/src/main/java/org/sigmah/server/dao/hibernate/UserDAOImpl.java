/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.sigmah.server.dao.UserDAO;
import org.sigmah.server.domain.User;

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
}
