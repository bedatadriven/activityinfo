/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigmah.server.dao.hibernate.PivotHibernateDAO;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import com.google.inject.ImplementedBy;

/**
 * PivotDAO is a reporting data access object that provides aggregation ("or pivoting")
 *  {@link org.sigmah.shared.domain.Site}s by a given set of dimensions.
 *
 * @author Alex Bertram
 */
@ImplementedBy(PivotHibernateDAO.class)
public interface PivotDAO {

    /**
     *
     * @param userId the id of the User for whom the data is restricted
     * @param filter a {@link org.sigmah.shared.dao.Filter filter} restricting the sites
     * @param dimensions
     * @return
     */
    List<Bucket> aggregate(int userId, Filter filter, Set<Dimension> dimensions);

    Map<Integer, String> getFilterLabels(DimensionType type, Set<Integer> map);

}
