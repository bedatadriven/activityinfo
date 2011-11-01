package org.sigmah.client.page.entry.place;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.DimensionType;

public class DatabaseDataEntryPlace extends DataEntryPlace {
	
	public static final String TOKEN = "database";

	private int databaseId;
	
	public DatabaseDataEntryPlace(UserDatabaseDTO db) {
		this.databaseId = db.getId();
	}

	public DatabaseDataEntryPlace(int databaseId) {
		this.databaseId = databaseId;
	}
	
	public int getDatabaseId() {
		return databaseId;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Database, databaseId);
		return filter;
	}

	@Override
	public String serializeAsHistoryToken() {
		StringBuilder sb = new StringBuilder();
		sb.append(TOKEN);
		sb.append("/");
		sb.append(databaseId);
		appendGridStateToken(sb);
		return sb.toString();
	}
}
