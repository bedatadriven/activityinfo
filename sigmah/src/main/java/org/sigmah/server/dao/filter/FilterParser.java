package org.sigmah.server.dao.filter;

import org.hibernate.criterion.Criterion;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface FilterParser {

    public Criterion parse(String filter);
}
