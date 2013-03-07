package org.activityinfo.shared.report.content;

import com.google.common.base.Preconditions;

public class AttributeCategory implements DimensionCategory {

    private String value;
    private int sortOrder;
    
    private AttributeCategory() {
        
    }
    
    public AttributeCategory(String value, int sortOrder) {
        Preconditions.checkNotNull(value);
        this.value = value;
        this.sortOrder = sortOrder;
    }
    
    @Override
    public Integer getSortKey() {
        return sortOrder;
    }

    @Override
    public String getLabel() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AttributeCategory other = (AttributeCategory) obj;
        return value.equals(other.value);
    }
 }
