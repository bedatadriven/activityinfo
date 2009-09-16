package org.activityinfo.server.dao.hibernate;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.model.*;

import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface PivotDAO {
    
    List<Bucket> aggregate(Filter filter, Set<Dimension> dimensions);

    List<String> getFilterLabels(DimensionType type, Collection<Integer> ids);

    static class Bucket {

        private double value;

        private Map<Dimension, DimensionCategory> categories = new HashMap<Dimension, DimensionCategory>();

        public Bucket() {

        }

        public Bucket(double doubleValue) {
            this.value =  doubleValue;
        }

        public Bucket(double value, Dimension.AndCategory... pairs) {
            this.value = value;
            for(Dimension.AndCategory pair : pairs) {
                categories.put(pair.getDimension(), pair.getCategory());
            }
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
