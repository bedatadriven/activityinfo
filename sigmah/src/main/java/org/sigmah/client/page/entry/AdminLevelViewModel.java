package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.SiteDTO;

public class AdminLevelViewModel extends SiteDTO {
	public int getLevelId() {
		return (Integer)get("levelId");
	}
	public AdminLevelViewModel setLevelId(int levelId) {
		set("levelId", levelId);
		return this;
	}
	public int getEntityId() {
		return (Integer)get("entityId");
	}
	public AdminLevelViewModel setEntityId(int entityId) {
		set("entityId",  entityId);
		return this;
	}
	public String getName() {
		return get("name");
	}
	public AdminLevelViewModel setName(String name) {
		set("name", name);
		return this;
	}
	
}
