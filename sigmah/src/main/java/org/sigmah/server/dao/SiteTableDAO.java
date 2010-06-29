/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.ImplementedBy;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.sigmah.server.dao.hibernate.SiteTableDAOHibernate;
import org.sigmah.server.domain.User;

import java.util.List;

/**       
 * 
 * Data Access Object for projections based on the {@link org.sigmah.server.domain.Site Site} domain object.
 * 
 * Information associated with Sites is stored across several entities, including
 * {@link org.sigmah.server.domain.Location Location},
 * {@link org.sigmah.server.domain.Partner},
 * {@link org.sigmah.server.domain.AttributeValue},
 * {@link org.sigmah.server.domain.ReportingPeriod ReportingPeriod}, and
 * {@link org.sigmah.server.domain.IndicatorValue}, but often we need this information in
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
