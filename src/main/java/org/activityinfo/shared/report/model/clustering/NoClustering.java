package org.activityinfo.shared.report.model.clustering;

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

import javax.xml.bind.annotation.XmlRootElement;


/*
 * Does not do any clustering on map markers. Instead it just maps a pointvalue to a cluster.
 */
@XmlRootElement
public class NoClustering implements Clustering {
	@Override
	public boolean isClustered() {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass().equals(getClass());
	}

	@Override
	public int hashCode() {
		return 0;
	}
}