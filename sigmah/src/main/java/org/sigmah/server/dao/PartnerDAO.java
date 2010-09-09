/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.domain.OrgUnit;

/**
 * Data Access Object for the {@link org.sigmah.shared.domain.OrgUnit} domain object. Implemented automatically by
 * {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy}
 *
 * @author Alex Bertram
 */
public interface PartnerDAO extends DAO<OrgUnit, Integer> {
}
