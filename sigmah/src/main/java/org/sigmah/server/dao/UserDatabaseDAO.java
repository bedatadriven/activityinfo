/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.UserDatabase;

import java.util.List;

/**
 * Data Access Object for {@link UserDatabase} domain classes. Implemented automatically
 * by {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy}.
 */
public interface UserDatabaseDAO extends DAO<UserDatabase, Integer> {

    List<UserDatabase> queryAllUserDatabasesAlphabetically();

}
