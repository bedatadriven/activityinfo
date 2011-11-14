package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

public interface SiteGrid {

	void showAll(SchemaDTO schema);

	void showActivity(ActivityDTO activity);

	void showDatabase(UserDatabaseDTO database);

}
