package org.activityinfo.client.page.config.link;

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

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.activityinfo.client.page.common.nav.Link;
import org.activityinfo.shared.command.result.IndicatorLink;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class LinkGraph {

	private List<IndicatorLink> links;

	public LinkGraph() {
		links = Collections.emptyList();
	}
	
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
