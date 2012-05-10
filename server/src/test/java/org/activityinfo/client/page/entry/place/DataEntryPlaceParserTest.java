package org.activityinfo.client.page.entry.place;

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
