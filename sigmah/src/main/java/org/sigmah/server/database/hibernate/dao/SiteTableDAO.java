/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.List;

import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.Filter;

/**       
 * 
 * Data Access Object for projections based on the {@link org.sigmah.server.database.hibernate.entity.Site Site} domain object.
 * 
 * Information associated with Sites is stored across several entities, including
 * {@link org.sigmah.server.database.hibernate.entity.Location Location},
 * {@link org.sigmah.server.database.hibernate.entity.Partner},
 * {@link org.sigmah.server.database.hibernate.entity.AttributeValue},
 * {@link org.sigmah.server.database.hibernate.entity.ReportingPeriod ReportingPeriod}, and
 * {@link org.sigmah.server.database.hibernate.entity.IndicatorValue}, but often we need this information in
 * a table format with all the different data in columns, and this class does the heavy lifting.
 *
 * 
 * @author Alex Bertram
 */
public interface SiteTableDAO {

    int RETRIEVE_ALL = 0xFF;
    int RETRIEVE_NONE = 0x00;
    int RETRIEVE_ADMIN = 0x01;
    int RETRIEVE_INDICATORS = 0x02;
    int RETRIEVE_ATTRIBS = 0x04;


    <RowT> List<RowT> query(
            User user,
            Filter filter,
            List<SiteOrder> orderings,
            SiteProjectionBinder<RowT> binder,
            int retrieve,
            int offset,
            int limit);

    int queryCount(User user, Filter filter);

    int queryPageNumber(User user, Filter filter, List<SiteOrder> orderings, int pageSize, int siteId);
}
