package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.PivotDAOImpl;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;

import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@ImplementedBy(PivotDAOImpl.class)
public interface PivotDAO {

    List<Bucket> aggregate(Filter filter, Set<Dimension> dimensions);

    List<String> getFilterLabels(DimensionType type, Collection<Integer> ids);

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
