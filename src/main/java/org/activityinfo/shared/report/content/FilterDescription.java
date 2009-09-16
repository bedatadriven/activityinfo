package org.activityinfo.shared.report.content;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import org.activityinfo.shared.report.model.DimensionType;

/**
 * Encapsulates a text description of a given filter restriction.
 * 
 * @param filter
 * @param excludeDims
 * @return
 */
public class FilterDescription implements Serializable {
	
	private DimensionType type;
	private List<String> labels;

    /**
     * Required for GWT serialization
     */
    private FilterDescription() {

    }

	public FilterDescription(DimensionType type, List<String> labels) {
		this.type = type;
		this.labels = labels;
	}
	
	public FilterDescription(DimensionType type, String label) {
		this.type = type;
		this.labels = new ArrayList<String>(1);
		this.labels.add(label);
	}
	

	public DimensionType getDimensionType() {
		return type;
	}

	/**
	 * @return The labels of the dimension categories specified in this restriction.
	 */
	public List<String> getLabels() {
		return labels;
	}
	
	public String joinLabels(String delimeter) {
		StringBuilder sb = new StringBuilder();
		for(String label : labels) {
			if(sb.length()!=0) {
				sb.append(delimeter);
			}
			sb.append(label);
		}
		return sb.toString();
	}
}