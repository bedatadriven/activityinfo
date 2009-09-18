package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * 
 * @author Alex Bertram
 *
 */
public class Dimension extends BaseModelData implements Serializable {


	private DimensionType type;
	private String color;

	private Map<DimensionCategory, CategoryProperties> categories =
            new HashMap<DimensionCategory, CategoryProperties>(0);
    
	private List<DimensionCategory> ordering = new ArrayList<DimensionCategory>();


    /**
     * Required for GWT serialization
     */
    protected Dimension() {

    }

	public Dimension(DimensionType type) {
		this.type = type;
	}

	public DimensionType getType() {
		return type;
	}

    private void setType(DimensionType type) {
        this.type = type;
    }

    /**
	 * @return The <i>type</i> of order applied to this dimension
	 */
	public boolean isOrderDefined() {
		return ordering.size()!=0;
	}

	/**
	 * @return The model-supplied (i.e. specified in the XML) category order of this dimension.
	 */
	public List<DimensionCategory> getOrdering() {
		return ordering;
	}

    private void setOrdering(List<DimensionCategory> ordering) {
        this.ordering = ordering;
    }

    /**
	 * 
	 * @param category
	 * @return The model-supplied (i.e. specified in the XML) category label for a given category
	 * 			in this dimension
	 */
	public String getLabel(DimensionCategory category) {
		CategoryProperties props = categories.get(category);
        return props == null ? null : props.getLabel();
	}

    public Map<DimensionCategory, CategoryProperties> getCategories() {
        return categories;
    }

    public void setCategories(Map<DimensionCategory, CategoryProperties> categories) {
        this.categories = categories;
    }

    /**
	 * Adds a model supplied label for a given category in this dimension
	 * 
	 * @param category
	 * @param props
	 */
	public void setProperties(DimensionCategory category, CategoryProperties props) {
		categories.put(category, props);
	}


    public void setProperties(int id, CategoryProperties props) {
        categories.put(new EntityCategory(id), props);
    }


	@Override
	public boolean equals(Object other) {
		if(this==other)
			return true;
		if(other==null)
			return false;
		if(!(other instanceof Dimension)) 
			return false;


		Dimension that = (Dimension)other;


		return this.type == that.type;

	}

	@Override 
	public int hashCode() {
		return this.type.hashCode();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

    public class AndCategory {

        private DimensionCategory category;

        public AndCategory(DimensionCategory category) {
            this.category = category;
        }

        public DimensionCategory getCategory() {
            return category;
        }

        public Dimension getDimension() {
            return Dimension.this;
        }
    }

    public AndCategory category(int id, String label) {
        return new AndCategory(new EntityCategory(id, label));
    }

}
