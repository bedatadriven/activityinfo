/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.ImplementedBy;
import org.sigmah.server.dao.hibernate.UserDAOImpl;
import org.sigmah.server.domain.User;

import javax.persistence.NoResultException;

/**
 * Data Access Object for the {@link org.sigmah.server.domain.User} domain class.
 *
 * @author Alex Bertram
 */
@ImplementedBy(UserDAOImpl.class)
public interface UserDAO extends DAO<User, Integer> {

    boolean doesUserExist(String email);

    User findUserByEmail(String email)
            throws NoResultException;

    User findUserByChangePasswordKey(String key);
}
