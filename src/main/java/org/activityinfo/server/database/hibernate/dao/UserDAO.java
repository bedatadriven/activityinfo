/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.dao;

import org.activityinfo.server.database.hibernate.entity.User;


/**
 * Data Access Object for the {@link org.activityinfo.server.database.hibernate.entity.User} domain class.
 *
 * @author Alex Bertram
 */
public interface UserDAO extends DAO<User, Integer> {

    boolean doesUserExist(String email);

    User findUserByEmail(String email);

    User findUserByChangePasswordKey(String key);

   
}
