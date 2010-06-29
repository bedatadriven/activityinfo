package org.sigmah.server.dao;

import org.sigmah.server.domain.Indicator;

/**
 * Data Access Object for {@link org.sigmah.server.domain.Indicator} domain objects. Implemented
 * by {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy}.
 *
 * @author Alex Bertram
 */
public interface IndicatorDAO extends DAO<Indicator, Integer> {
}
