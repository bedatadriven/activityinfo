

package org.activityinfo.shared.report.content;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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