package org.sigmah.shared.command.result;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.model.Dimension;

/**
 * Contains the aggregate value for an intersection of dimension categories.
 */
public class Bucket implements Serializable {
    private double value;
    private Map<Dimension, DimensionCategory> categories = new HashMap<Dimension, DimensionCategory>();

    public Bucket() {
    }

    public Bucket(double doubleValue) {
        this.value = doubleValue;
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