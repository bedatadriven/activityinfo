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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdministrativeLevelClustering implements Clustering {
	private List<Integer> adminLevels = new ArrayList<Integer>();
	
	@XmlElement
	public List<Integer> getAdminLevels() {
		return adminLevels;
	}
	
	@Override
	public boolean isClustered() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adminLevels == null) ? 0 : adminLevels.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		AdministrativeLevelClustering other = (AdministrativeLevelClustering) obj;
		return adminLevels.equals(other.adminLevels);
	}

	@Override
	public String toString() {
		return "AdministrativeLevelClustering [adminLevels=" + adminLevels
				+ "]";
	}
}