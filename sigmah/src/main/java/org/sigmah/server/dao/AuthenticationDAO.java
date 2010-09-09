/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.Authentication;
import org.sigmah.shared.dao.DAO;

/**
 * Data Access Objects for the {@link org.sigmah.server.domain.Authentication} domain object.
 *
 * @author Alex Bertram
 */
public interface AuthenticationDAO extends DAO<Authentication, String> {
}
