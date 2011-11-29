package org.sigmah.shared.dto;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class AdminLevelPredicates {

	private AdminLevelPredicates() {}

	
	public static List<AdminLevelDTO> breadthFirstSort(List<AdminLevelDTO> allLevels) {
		List<AdminLevelDTO> sorted = Lists.newArrayList();
		Predicate<AdminLevelDTO> predicate = rootLevels();
		Collection<AdminLevelDTO> next;
		while(!(next = Collections2.filter(allLevels, predicate)).isEmpty()){
			sorted.addAll(next);
			predicate = childrenOf(next);
		} 
		return sorted;
	}
	
	public static Predicate<AdminLevelDTO> rootLevels() {
		return new Predicate<AdminLevelDTO>() {
			
			@Override
			public boolean apply(AdminLevelDTO level) {
				return level.isRoot();
			}
		};
	}
	
	public static Predicate<AdminLevelDTO> childrenOf(final Collection<AdminLevelDTO> parents) {
		return new Predicate<AdminLevelDTO>() {
			
			@Override
			public boolean apply(AdminLevelDTO level) {
				for(AdminLevelDTO parent : parents) {
					if(!level.isRoot() && parent.getId() == level.getParentLevelId()) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	
}
