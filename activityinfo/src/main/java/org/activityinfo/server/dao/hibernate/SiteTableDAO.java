package org.activityinfo.server.dao.hibernate;

import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.domain.User;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface SiteTableDAO {
    int RETRIEVE_ALL = 0xFF;
    int RETRIEVE_NONE = 0x00;
    int RETRIEVE_ADMIN = 0x01;
    int RETRIEVE_INDICATORS = 0x02;
    int RETRIEVE_ATTRIBS = 0x04;




    <RowT> List<RowT> query(
            User user,
Criterion criterion,
            List<Order> orderings,
            SiteProjectionBinder<RowT> binder,
            int retrieve,
            int offset,
            int limit);

    int queryCount(Conjunction criterion);

    int queryPageNumber(User user, Criterion criterion, List<Order> orderings, int pageSize, int siteId);
}
