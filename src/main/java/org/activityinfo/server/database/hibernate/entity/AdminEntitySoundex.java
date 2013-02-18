package org.activityinfo.server.database.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="adminentity_soundex")
public class AdminEntitySoundex {
	
	@Id
	private int id;
	private int levelId;
	private String soundex;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getSoundex() {
		return soundex;
	}
	public void setSoundex(String soundex) {
		this.soundex = soundex;
	}
	
}
