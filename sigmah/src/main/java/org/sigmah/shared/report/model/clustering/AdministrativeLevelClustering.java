package org.sigmah.shared.report.model.clustering;

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdministrativeLevelClustering other = (AdministrativeLevelClustering) obj;
		return adminLevels.equals(other.adminLevels);
	}

}