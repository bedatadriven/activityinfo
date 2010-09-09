/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import org.sigmah.shared.domain.Indicator;

/**
 * Data Access Object for {@link org.sigmah.shared.domain.Indicator} domain objects. Implemented
 * by {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy}.
 *
 * @author Alex Bertram
 */
public interface IndicatorDAO extends DAO<Indicator, Integer> {
}
