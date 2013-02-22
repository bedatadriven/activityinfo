

package org.activityinfo.shared.report.model.layers;

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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.activityinfo.shared.command.Filter;

/*
 * Convenience implementation for MapLayer implementors
 */
public abstract class AbstractMapLayer implements MapLayer {
	private boolean isVisible = true;
	private List<Integer> indicatorIds = new ArrayList<Integer>();
	private String name;
	private Filter filter = new Filter();

	@Override
	public void addIndicatorId(int id) {
	    indicatorIds.add(id);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	@XmlElement(name = "indicator")
	@XmlElementWrapper(name = "indicators")
	public List<Integer> getIndicatorIds() {
	    return indicatorIds;
	}
	
	/*
	 * Returns true when there is a list of indicatorIDs and the amount of indicatorIDs is more then one
	 */
	@Override
	public boolean hasMultipleIndicators() {
		return indicatorIds != null && indicatorIds.size() > 1;
	}

	@XmlElement
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public String toString() {
		return  getClass() + "[isVisible=" + isVisible + ", indicatorIds="
				+ indicatorIds 
				+ filter + "]";
	}
}
