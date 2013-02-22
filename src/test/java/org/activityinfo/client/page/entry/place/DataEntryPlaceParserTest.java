package org.activityinfo.client.page.entry.place;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.client.page.entry.grouping.AdminGroupingModel;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.client.page.entry.place.DataEntryPlaceParser;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DimensionType;
import org.junit.Test;

public class DataEntryPlaceParserTest {

	@Test
	public void empty() {
		verifyCorrectSerde(new DataEntryPlace());
	}
	
	@Test
	public void activityFiltered() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		
		verifyCorrectSerde(new DataEntryPlace(filter));
	}
	
	@Test
	public void activityFilteredAndGrouped() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		AdminGroupingModel grouping = new AdminGroupingModel(1);
		
		verifyCorrectSerde(new DataEntryPlace(grouping, filter));
	}
	
	private void verifyCorrectSerde(DataEntryPlace place) {
		String fragment = place.serializeAsHistoryToken();
		System.out.println(place + " => " + fragment);

		DataEntryPlace deserialized = new DataEntryPlaceParser().parse(fragment);
		
		assertThat(deserialized, equalTo(place));
	}
}
