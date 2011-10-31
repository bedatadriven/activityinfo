package org.sigmah.client.page.entry.place;

import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

public class DataEntryPlaceParser implements PageStateParser {
	
	@Override
	public PageState parse(String token) {

		String[] parts = token.split("/");
		if(parts.length > 0) {
			if(ActivityDataEntryPlace.TOKEN.equals(parts[0])) {
				ActivityDataEntryPlace place = new ActivityDataEntryPlace(Integer.parseInt(parts[1]));
				if(parts.length > 1) {
					place.parseGridStateTokens(parts[1]);
				}
			} else if(DatabaseDataEntryPlace.TOKEN.equals(parts[0])) {
				DatabaseDataEntryPlace place = new DatabaseDataEntryPlace(Integer.parseInt(parts[1]));
				if(parts.length > 1) {
					place.parseGridStateTokens(parts[1]);
				}
			}
		}
		return new DataEntryPlace();
	}
}