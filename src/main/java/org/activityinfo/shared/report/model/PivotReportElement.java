

package org.activityinfo.shared.report.model;

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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.content.PivotContent;

/**
 * Abstract base class that exposes properties common to 
 * the PivotTable and PivotChart elements
 * 
 * @author Alex Bertram
 *
 */
public abstract class PivotReportElement<ContentT extends PivotContent> extends ReportElement<ContentT> {


	/**
	 * Returns the set of all dimensions that figure in the 
	 * pivot table/chart, whether in the row, column, horizantal
	 * axis, etc.
	 * 
	 * @return
	 */
	public abstract Set<Dimension> allDimensions();

	/**
	 * Returns the set of all dimension <i>types</i> that figures in the 
	 * pivot table/chart. See {@link #allDimensions()}
	 * 
	 * @return
	 */
	public Set<DimensionType> allDimensionTypes() {
		Set<DimensionType> set = new HashSet<DimensionType>();
		
		for(Dimension dimension : allDimensions()) {
			set.add(dimension.getType());
		}
		return set;
	}
	
	@Override
	public ContentT getContent() {
		return super.getContent();
	}

	@Override
	@XmlTransient
	public Set<Integer> getIndicators() {
		return getFilter().getRestrictions(DimensionType.Indicator);
	}

	
    
}
