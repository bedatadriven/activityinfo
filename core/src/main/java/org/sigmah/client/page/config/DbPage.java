package org.sigmah.client.page.config;

import org.sigmah.client.page.Page;
import org.sigmah.shared.dto.UserDatabaseDTO;

public interface DbPage extends Page {

	void go(UserDatabaseDTO db);

}
