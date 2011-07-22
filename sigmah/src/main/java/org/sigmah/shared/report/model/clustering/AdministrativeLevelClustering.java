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

}