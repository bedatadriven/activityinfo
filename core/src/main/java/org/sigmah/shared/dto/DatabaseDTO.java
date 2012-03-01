package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public final class DatabaseDTO extends BaseModelData implements DTO {

	private UserDatabaseDTO database = new UserDatabaseDTO();

	public DatabaseDTO() {
	}

	
	public UserDatabaseDTO getDatabase() {
		return database;
	}


	public void setDatabase(UserDatabaseDTO database) {
		this.database = database;
	}


	public List<ActivityDTO> getActivities() {

		List<ActivityDTO> publicActivity = new ArrayList<ActivityDTO>();

		List<ActivityDTO> activities = database.getActivities();
		for (ActivityDTO e : activities) {
			if (e != null) {
				publicActivity.add(e);
			}
		}

		return publicActivity;
	}

}
