package org.sigmah.client.page.config.link;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.sigmah.client.page.common.nav.Link;
import org.sigmah.shared.command.result.IndicatorLink;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.google.common.collect.Sets;

public class LinkGraph {

	private List<IndicatorLink> links;

	public LinkGraph(List<IndicatorLink> links) {
		this.links = links;
	}
	
	public Set<Integer> sourceDatabases() {
		Set<Integer> set = Sets.newHashSet();
		for(IndicatorLink link : links) {
			set.add(link.getSourceDatabaseId());
		}
		return set;
	}
	
	public Set<Integer> destDatabases() {
		Set<Integer> set = Sets.newHashSet();
		for(IndicatorLink link : links) {
			set.add(link.getDestinationDatabaseId());
		}
		return set;
	}

	public Set<Integer> sourcesForDatabase(UserDatabaseDTO model) {
		Set<Integer> set = Sets.newHashSet();
		for(IndicatorLink link : links) {
			if(link.getDestinationDatabaseId() == model.getId()) {
				set.add(link.getSourceDatabaseId());
			}
		}
		return set;
	}
	
	public Set<Integer> destinationForDatabase(UserDatabaseDTO model) {
		Set<Integer> set = Sets.newHashSet();
		for(IndicatorLink link : links) {
			if(link.getSourceDatabaseId() == model.getId()) {
				set.add(link.getDestinationDatabaseId());
			}
		}
		return set;
	}

	public Set<Integer> sourceIndicators(UserDatabaseDTO source,
			UserDatabaseDTO dest) {
		Set<Integer> set = Sets.newHashSet();
		
		if(source != null && dest != null) {
		
			for(IndicatorLink link : links) {
				if(link.getSourceDatabaseId() == source.getId() &&
					link.getDestinationDatabaseId() == dest.getId()) {
					
					set.add(link.getSourceIndicatorId());
				}
			}
		}
		return set;
	}
	
	public Set<Integer> destinationIndicators(UserDatabaseDTO source,
			UserDatabaseDTO dest) {
		Set<Integer> set = Sets.newHashSet();
		
		if(source != null && dest != null) {
		
			for(IndicatorLink link : links) {
				if(link.getSourceDatabaseId() == source.getId() &&
					link.getDestinationDatabaseId() == dest.getId()) {
					
					set.add(link.getDestinationIndicatorId());
				}
			}
		}
		return set;
	}

	public Set<Integer> destForIndicator(IndicatorDTO source, UserDatabaseDTO dest) {
		Set<Integer> set = Sets.newHashSet();
		
		if(source != null && dest != null) {
		
			for(IndicatorLink link : links) {
				if(link.getSourceIndicatorId() == source.getId() &&
					link.getDestinationDatabaseId() == dest.getId()) {
					
					set.add(link.getDestinationIndicatorId());
				}
			}
		}
		return set;
	}
	
	public Set<Integer> sourceForIndicator(UserDatabaseDTO source, IndicatorDTO dest) {
		Set<Integer> set = Sets.newHashSet();
		
		if(source != null && dest != null) {
		
			for(IndicatorLink link : links) {
				if(link.getSourceDatabaseId() == source.getId() &&
					link.getDestinationIndicatorId() == dest.getId()) {
					
					set.add(link.getSourceIndicatorId());
				}
			}
		}
		return set;
	}

	public boolean linked(IndicatorDTO source, IndicatorDTO dest) {
		for(IndicatorLink link : links) {
			if(link.getSourceIndicatorId() == source.getId() &&
					link.getDestinationIndicatorId() == dest.getId()) {
				return true;
			}
		}
		return false;
	}

	public void link(UserDatabaseDTO sourceDb, IndicatorDTO source, UserDatabaseDTO destDb, IndicatorDTO dest) {
		IndicatorLink link = new IndicatorLink();
		link.setSourceDatabaseId(sourceDb.getId());
		link.setSourceIndicatorId(source.getId());
		link.setDestinationDatabaseId(destDb.getId());
		link.setDestinationIndicatorId(dest.getId());
		links.add(link);
	}

	public void unlink(IndicatorDTO source, IndicatorDTO dest) {
		for(ListIterator<IndicatorLink> it = links.listIterator(); it.hasNext();) {
			IndicatorLink link = it.next();
			if(link.getSourceIndicatorId() == source.getId() && link.getDestinationIndicatorId() == dest.getId()) {
				it.remove();
			}
		}
	}


}
