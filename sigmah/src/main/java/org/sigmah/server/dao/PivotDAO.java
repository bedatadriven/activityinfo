/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.ImplementedBy;
import org.sigmah.server.dao.hibernate.PivotHibernateDAO;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import java.util.*;

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

    List<String> getFilterLabels(DimensionType type, Collection<Integer> ids);

    /**
     * Contains the aggregate value for an intersection of dimension categories.
     */
    public static class Bucket {
        private double value;
        private Map<Dimension, DimensionCategory> categories = new HashMap<Dimension, DimensionCategory>();

        public Bucket() {
        }

        public Bucket(double doubleValue) {
            this.value = doubleValue;
        }

        public Bucket(double value, Dimension.AndCategory... pairs) {
            this.value = value;
            for (Dimension.AndCategory pair : pairs) {
                categories.put(pair.getDimension(), pair.getCategory());
            }
        }

        public Collection<Dimension> dimensions() {
            return categories.keySet();
        }

        public void setCategory(Dimension dimension, DimensionCategory category) {
            this.categories.put(dimension, category);
        }

        public DimensionCategory getCategory(Dimension dimension) {
            return categories.get(dimension);
        }

        public double doubleValue() {
            return value;
        }

        public void setDoubleValue(double value) {
            this.value = value;
        }
    }
}
