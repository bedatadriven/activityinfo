package org.activityinfo.shared.command.result;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.model.Dimension;

/**
 * Contains the aggregate value for an intersection of dimension categories.
 */
public class Bucket implements Serializable {
    private double sum;
    private int count;
    private int aggregationMethod;
    
    
    private Map<Dimension, DimensionCategory> categories = new HashMap<Dimension, DimensionCategory>();

    public Bucket() {
    }
    
    public Bucket(double sum) {
    	this.sum = sum;
    	this.count = 1;
    	this.aggregationMethod = IndicatorDTO.AGGREGATE_SUM;
    }

    public Bucket(double sum, int count, int aggregationMethod) {
    	this.sum = sum;
    	this.count = count;
    	this.aggregationMethod = aggregationMethod;
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

    public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAggregationMethod() {
		return aggregationMethod;
	}

	public void setAggregationMethod(int aggregationMethod) {
		this.aggregationMethod = aggregationMethod;
	}

	public double doubleValue() {
    	switch(aggregationMethod) {
    	case IndicatorDTO.AGGREGATE_AVG:
    		return sum / (double)count;
    	case IndicatorDTO.AGGREGATE_SUM:
    		return sum;
    	case IndicatorDTO.AGGREGATE_SITE_COUNT:
    		return count;
    	}
    	throw new UnsupportedOperationException("aggregationMethod: " + aggregationMethod);
    }

	public Object getKey() {
		return categories;
	}

	public void add(Bucket bucket) {
		this.sum += bucket.getSum();
		this.count += bucket.getCount();
	}
}