package org.activityinfo.server.dao;

import org.activityinfo.server.domain.Indicator;

/**
 * Data Access Object for {@link org.activityinfo.server.domain.Indicator} domain objects. Implemented
 * by {@link org.activityinfo.server.dao.hibernate.DAOInvocationHandler proxy}.
 *
 * @author Alex Bertram
 */
public interface IndicatorDAO extends DAO<Indicator, Integer> {
}
