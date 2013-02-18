package org.activityinfo.server.endpoint.refine;

import java.util.List;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;

import com.google.common.collect.Lists;


public class AdminEntityPreview {

	private AdminEntity entity;
	private List<AdminEntity> parents;
	
	public AdminEntityPreview(AdminEntity entity) {
		super();
		this.entity = entity;
		this.parents = Lists.newArrayList();
		
		AdminEntity parent = entity.getParent();
		while(parent != null) {
			parents.add(parent);
			parent = parent.getParent();
		}
		
	}

	public AdminEntity getEntity() {
		return entity;
	}

	public List<AdminEntity> getParents() {
		return parents;
	}
	
	
	
}
