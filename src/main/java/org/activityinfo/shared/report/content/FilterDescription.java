/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	private Map<Integer, String> labels;

    /**
     * Required for GWT serialization
     */
    private FilterDescription() {

    }

	public FilterDescription(DimensionType type, Map<Integer, String> labels) {
		this.type = type;
		this.labels = labels;
	}
	
	public FilterDescription(DimensionType type, int id, String label) {
		this.type = type;
		this.labels = new HashMap<Integer, String>();
		this.labels.put(id,label);
	}
	

	public DimensionType getDimensionType() {
		return type;
	}

	/**
	 * @return The labels of the dimension categories specified in this restriction.
	 */
	public Map<Integer, String> getLabels() {
		return labels;
	}
	
	public String joinLabels(String delimeter) {
		StringBuilder sb = new StringBuilder();
		for(String label : labels.values()) {
			if(sb.length()!=0) {
				sb.append(delimeter);
			}
			sb.append(label);
		}
		return sb.toString();
	}
}