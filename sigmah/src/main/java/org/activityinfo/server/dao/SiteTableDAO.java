package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;

/**       
 * 
 * Data Access Object for projections based on the {@link org.activityinfo.server.domain.Site Site} domain object.
 * 
 * Information associated with Sites is stored across several entities, including
 * {@link org.activityinfo.server.domain.Location Location},
 * {@link org.activityinfo.server.domain.Partner},
 * {@link org.activityinfo.server.domain.AttributeValue},
 * {@link org.activityinfo.server.domain.ReportingPeriod ReportingPeriod}, and
 * {@link org.activityinfo.server.domain.IndicatorValue}, but often we need this information in
 * a table format with all the different data in columns, and this class does the heavy lifting.
 *
 * 
 * @author Alex Bertram
 */
@ImplementedBy(SiteTableDAOHibernate.class)
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
