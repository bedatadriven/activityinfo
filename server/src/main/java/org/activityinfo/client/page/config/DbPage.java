package org.activityinfo.client.page.config;

import org.activityinfo.client.page.Page;
import org.activityinfo.shared.dto.UserDatabaseDTO;

public interface DbPage extends Page {

	void go(UserDatabaseDTO db);

}
