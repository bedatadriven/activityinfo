/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.UserPermission;

/**
 * Data Access Object for the {@link org.sigmah.server.domain.UserPermission} domain class.
 * Implemented by {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy}
 *
 * @author Alex Bertram
 */
public interface UserPermissionDAO extends DAO<UserPermission, Integer> {
}
