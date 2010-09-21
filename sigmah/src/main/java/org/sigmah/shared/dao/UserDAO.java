/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import org.sigmah.shared.domain.User;


/**
 * Data Access Object for the {@link org.sigmah.shared.domain.User} domain class.
 *
 * @author Alex Bertram
 */
public interface UserDAO extends DAO<User, Integer> {

    boolean doesUserExist(String email);

    User findUserByEmail(String email);

    User findUserByChangePasswordKey(String key);

   
}
